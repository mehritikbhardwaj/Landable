package com.landable.app.ui.home.project

import android.content.Intent
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentProjectDetailBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.agent.ContactOwnerDialogFragment
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.project.adapter.AmenitiesProjectAdapter
import com.landable.app.ui.home.project.adapter.ConfigurationAdapter
import com.landable.app.ui.home.project.adapter.ProjectsAdapter
import com.landable.app.ui.home.property.AddYourLocationDailogFragment
import com.landable.app.ui.home.property.PostReviewDialogFragment
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.property.adapters.AdvertisementAdapter
import com.landable.app.ui.home.property.adapters.DistanceFromLocationAdapter
import com.landable.app.ui.home.property.adapters.PropertyProjectImagesAdapter
import com.landable.app.ui.home.property.adapters.PropertyReviewAdapter
import com.landable.app.ui.home.shortUrl.ShortUrlFragment
import org.json.JSONObject
import java.io.IOException

class ProjectDetailFragment : Fragment(), ProjectDetailListener, AdvertisementClickListener,
    ConfigurationImageClickListener, OnMapReadyCallback, DocumentClickListener,
    AddYourLocationDailogFragment.IAddLocation,
    AgentProfileListener {

    private lateinit var binding: FragmentProjectDetailBinding
    private var projectDetailInfoModel: ProjectsDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var similarProjectsList = ArrayList<ProjectsDataModel>()
    private var featuredProjectsList = ArrayList<ProjectsDataModel>()
    private var imagesList = ArrayList<Propertyimage>()
    private val imageArrayList = ArrayList<Propertyimage>()
    private val floorArrayList = ArrayList<Propertyimage>()
    private val documentArrayList = ArrayList<Propertyimage>()
    private var projectData: ProjectDetailDataModel? = null
    private var locationList = ArrayList<Distancefromlocation>()
    private var reviewsList = ArrayList<Review>()
    private var advertismentsList = ArrayList<Advertisment>()
    private var configurationList = ArrayList<Projectconfiguration>()
    private var mMap: GoogleMap? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var mediaUrl: String = ""
    private var documentUrl: String = ""

    companion object {
        fun newInstance() = ProjectDetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting album model from other fragments
        projectDetailInfoModel =
            requireArguments().getSerializable("projectDetailModel") as ProjectsDataModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        (activity as HomeActivity).enableBackButton(projectDetailInfoModel!!.projectname)
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_project_detail, container, false)

        updateProjectDetailUI()


        getProjectDetails(projectDetailInfoModel!!.id, projectDetailInfoModel!!.projectid)

        binding.llMedia.setOnClickListener {
            (activity as HomeActivity).callBrowserActivity(LandableConstants.Image_URL + mediaUrl)
        }

        binding.addLocation.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = AddYourLocationDailogFragment(
                this
            )
            dialogFragment.show(fm, "")
        }

        binding.llDocument.setOnClickListener {
            (activity as HomeActivity).callBrowserActivity(LandableConstants.Image_URL + documentUrl)
        }
        binding.shortURl.setOnClickListener {
            loadShortUrlFragment()
        }

        binding.viewOnMap.setOnClickListener {
            val url = "http://maps.google.com/maps?daddr=${projectDetailInfoModel!!.Fulladdress}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        binding.tvImage.setOnClickListener {
            checkSelectedText("image", imageArrayList)
        }
        binding.tvFloorPlan.setOnClickListener {
            checkSelectedText("floorplan", floorArrayList)
        }
        binding.tvDocument.setOnClickListener {
            checkSelectedText("document", documentArrayList)
        }


        binding.llOwnerPRofile.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                (activity as HomeActivity).askForLogin()
                contactOwner()
            } else {
                loadAgentProfileFragment(projectDetailInfoModel!!.addedbyid)
            }
        }

        binding.ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val link = projectDetailInfoModel!!.linkurl
            val name = projectDetailInfoModel!!.projectname

            val shareBody = name + "\n" + LandableConstants.Image_URL + link

            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.share_subject)
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "share"))
        }


        binding.llContactOwner.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
                contactOwner()
            } else {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = ContactOwnerDialogFragment(
                    projectDetailInfoModel!!.addedbyid,
                    projectDetailInfoModel!!.name
                )
                dialogFragment.show(fm, "")
            }
        }
        binding.postReview.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = PostReviewDialogFragment(
                    projectDetailInfoModel!!.projectid, this, "project"
                )
                dialogFragment.show(fm, "")
            }

        }


        binding.ivFavourite.setOnClickListener {

            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                addToFavourite(
                    PropertyDetailFragment.AddtoFavouriteDataModel(
                        projectDetailInfoModel!!.id.toString(),
                        "property"
                    )
                )
            }
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (projectDetailInfoModel!!.lat.isNullOrEmpty() || projectDetailInfoModel!!.lon.isNullOrEmpty()) {
            lat = 28.7041
            lon = 77.1025
        } else {
            lat = projectDetailInfoModel!!.lat.toDouble()
            lon = projectDetailInfoModel!!.lon.toDouble()
        }

        return binding.root
    }

    private fun loadShortUrlFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            ShortUrlFragment.newInstance(),
            ShortUrlFragment::class.java.name
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isCompassEnabled = true
        updateLocation(LatLng(lat, lon), "")
    }


    private fun updateLocation(location: LatLng, markerTitle: String) {
        if (mMap != null) {
            val zoomLevel = 12.0f //This goes up to 21

            mMap!!.addMarker(MarkerOptions().position(location).title(markerTitle))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
        }
    }

    private fun checkSelectedText(type: String, imageList: ArrayList<Propertyimage>) {
        binding.tvImage.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.tvFloorPlan.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.tvDocument.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.tvImage.setTypeface(null, Typeface.NORMAL)
        binding.tvDocument.setTypeface(null, Typeface.NORMAL)
        binding.tvFloorPlan.setTypeface(null, Typeface.NORMAL)

        if (type == "image") {
            binding.tvImage.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colour_app
                )
            )
            binding.tvImage.setTypeface(null, Typeface.BOLD)
        } else if (type == "document") {
            binding.tvDocument.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colour_app
                )
            )
            binding.tvDocument.setTypeface(null, Typeface.BOLD)
        } else if (type == "floorplan") {
            binding.tvFloorPlan.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colour_app
                )
            )
            binding.tvFloorPlan.setTypeface(null, Typeface.BOLD)

        }
        binding.rvPropertyImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPropertyImages.adapter =
            PropertyProjectImagesAdapter(imageList, this)
    }


    private fun contactOwner() {
        (activity as HomeActivity).propertyID = projectDetailInfoModel!!.projectid
        (activity as HomeActivity).contactType = "Project"
        (activity as HomeActivity).agentID = projectDetailInfoModel!!.addedbyid
    }

    private fun loadAgentProfileFragment(
        id: Int,
    ) {
        val bundle = Bundle()
        bundle.putInt("agentID", id)
        val agencyProfileFragment = AgencyProfileFragment.newInstance()
        agencyProfileFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            agencyProfileFragment,
            AgencyProfileFragment::class.java.name
        )
    }
    /* private fun updateProjectsList() {
         binding.rvFeaturedProjects.layoutManager =
             LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
         binding.rvFeaturedProjects.adapter = ProjectsAdapter(projectsList, this)
     }*/

    private fun updateProjectDetailUI() {
        //     binding.tvPropertyName.text = projectDetailInfoModel!!.projectname
        binding.tvPropertyHeading.text = projectDetailInfoModel!!.projectname
        binding.ivPropertyImage.load(LandableConstants.Image_URL + projectDetailInfoModel!!.image1)
/*
*/


        if (projectDetailInfoModel!!.isfavourite == "true") {
            binding.ivFavourite.visibility = View.GONE
            binding.ivUnFavorite.visibility = View.VISIBLE
        } else {
            binding.ivFavourite.visibility = View.VISIBLE
            binding.ivUnFavorite.visibility = View.GONE
        }
        binding.tvDiscription.maxLines = 2
        binding.customerType.text = projectDetailInfoModel!!.customertype
        binding.tvPrice.text =
            "\u20B9 " + projectDetailInfoModel!!.mincost + " - " + projectDetailInfoModel!!.maxcost
        //  binding.tvBathroomCount.text = projectDetailInfoModel!!.bathroom
        //   binding.tvBedroomCount.text = projectDetailInfoModel!!.bedroom
        binding.tvCategoryName.text = projectDetailInfoModel!!.categoryname
        binding.tvPropertyID.text = projectDetailInfoModel!!.projectid
        binding.tvPropertyPrice.text =
            "\u20B9 " + projectDetailInfoModel!!.mincost + " - " + projectDetailInfoModel!!.maxcost
        binding.tvPropertyLocation.text = projectDetailInfoModel!!.Fulladdress
        binding.tvPossession.text = projectDetailInfoModel!!.possessionname
        binding.tvPropertyType.text = projectDetailInfoModel!!.subcategoryname
        binding.tvPossession.text = projectDetailInfoModel!!.possessionname
        binding.tvOwnerName.text = projectDetailInfoModel!!.name
        binding.tvLocation.text = projectDetailInfoModel!!.Fulladdress
        binding.tvRating.text = projectDetailInfoModel!!.rating.toString()
        // binding.propertyStrength.text = projectDetailInfoModel!!.strengthmsg
        binding.tvDiscription.text = projectDetailInfoModel!!.description
        binding.readMore.setOnClickListener {
            binding.tvDiscription.maxLines = 100
            binding.readMore.visibility = View.GONE
        }

        /*   if (projectDetailInfoModel!!.strength > 33 && projectDetailInfoModel!!.strength < 66) {
               binding.firstProgress.progress = 33
               binding.secondProgress.progress = 33
               binding.firstProgressImage.visibility = View.VISIBLE
           } else if (projectDetailInfoModel!!.strength > 66) {
               binding.firstProgress.progress = 33
               binding.secondProgress.progress = 33
               binding.thirdProgress.progress = 33
               binding.secondProgressImage.visibility = View.VISIBLE
               binding.firstProgressImage.visibility = View.VISIBLE
           } else {
               binding.firstProgress.progress = 33
               binding.firstProgressImage.visibility = View.VISIBLE
           }*/

    }

    override fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?) {
        when (action) {
            "selectedProjectDetail" -> {
                loadProjectDetailFragment(projectDataModel)
            }
        }
    }

    private fun addToFavourite(dataModel: PropertyDetailFragment.AddtoFavouriteDataModel) {
        val favoriteResponse = RegisterRepository().addToFavorite(dataModel)
        favoriteResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                if (it.toString() != "null") {
                    val jsonObj = JSONObject(it)
                    when (val status = jsonObj.getString("status")) {
                        "success" -> {
                            binding.ivFavourite.visibility = View.GONE
                            binding.ivUnFavorite.visibility = View.VISIBLE
                        }
                        "exists" -> {
                            binding.ivFavourite.visibility = View.GONE
                            binding.ivUnFavorite.visibility = View.VISIBLE
                        }
                        else -> {
                            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun getProjectDetails(id: Int, projectId: String) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val propertyResponse = RegisterRepository().getProjectDetails(id, projectId)
        propertyResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()
            // PROJ1898121112705
            //12
            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    projectData = ParseResponse.parseProjectDetailResponse(it.toString())
                    locationList = projectData!!.distancefromlocation
                    similarProjectsList = projectData!!.similarprojects
                    featuredProjectsList = projectData!!.featuredprojects
                    reviewsList = projectData!!.review
                    advertismentsList = projectData!!.advertisment
                    configurationList = projectData!!.projectconfiguration
                    imagesList = projectData!!.projectimages
                    updateProjectsData()
                    for (i in 0 until imagesList.size) {
                        when (imagesList[i].imagetype) {
                            "Image" -> {
                                imageArrayList.add(imagesList[i])
                            }
                            "floorplan" -> {
                                floorArrayList.add(imagesList[i])
                            }
                            "document" -> {
                                documentArrayList.add(imagesList[i])
                            }
                        }
                    }

                    if (imageArrayList.size == 0) {
                        binding.tvImage.visibility = View.GONE
                    }
                    if (floorArrayList.size == 0) {
                        binding.tvFloorPlan.visibility = View.GONE
                    }
                    if (documentArrayList.size == 0) {
                        binding.tvDocument.visibility = View.GONE
                    }

                    hideMediaCount()

                    checkSelectedText("image", imageArrayList)
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun hideMediaCount() {
        if (imageArrayList.size > 0) {
            binding.llPhoto.visibility = View.VISIBLE
            mediaUrl = imageArrayList[0].imagepath
            binding.tvImageCount.text = imageArrayList.size.toString()
        } else {
            binding.llPhoto.visibility = View.GONE
        }

        if (documentArrayList.size > 0) {
            binding.llDocument.visibility = View.VISIBLE
            mediaUrl = documentArrayList[0].imagepath
            binding.tvDocumentCount.text = imageArrayList.size.toString()
        } else {
            binding.llDocument.visibility = View.GONE
        }

    }

    private fun updateProjectsData() {

        binding.circleImageView.load(LandableConstants.Image_URL + projectData!!.details.profilepic)

        if (imagesList.size == 0) {
            binding.llImages.visibility = View.GONE
            binding.linewAfterImages.visibility = View.GONE
        }

        if (imageArrayList.size > 0) {
            binding.tvImage.visibility = View.VISIBLE
        }
        if (documentArrayList.size > 0) {
            binding.tvDocument.visibility = View.VISIBLE
        }

        if (floorArrayList.size > 0) {
            binding.tvFloorPlan.visibility = View.VISIBLE
        }

        binding.rvPropertyImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPropertyImages.adapter =
            PropertyProjectImagesAdapter(imagesList, this)

        //    binding.ivBadge.load(LandableConstants.Image_URL + projectData!!.details.badges)

        binding.rvAmenities.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAmenities.adapter = AmenitiesProjectAdapter(projectData!!.Amenitiesmasters)

        if (projectData!!.additionaldetails.isreraverified == "YES") {
            binding.llReraverified.visibility = View.VISIBLE
        }
        binding.rvLocation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLocation.adapter =
            DistanceFromLocationAdapter(locationList)

        if (configurationList.size == 0) {
            binding.tvConfiguration.visibility = View.GONE
            binding.configurationHEader.visibility = View.GONE
        } else {
            binding.rvConfiguration.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvConfiguration.adapter = ConfigurationAdapter(configurationList, this)

        }

        binding.rvReviews.adapter = PropertyReviewAdapter(reviewsList)
        if (reviewsList.size == 0) {
            binding.llNoReviews.visibility = View.VISIBLE
        } else {
            binding.rvReviews.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvReviews.adapter = PropertyReviewAdapter(reviewsList)
        }

        binding.rvFeaturedProjects.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedProjects.adapter =
            ProjectsAdapter(featuredProjectsList, this, requireContext())

        if (projectData!!.similarprojects.size == 0) {
            binding.tvSimilarProjectText.visibility = View.GONE
        } else {
            binding.rvSimilarProjects.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvSimilarProjects.adapter =
                ProjectsAdapter(similarProjectsList, this, requireContext())
        }

        if (projectData!!.Amenitiesmasters.size == 0) {
            binding.tvAmenities.visibility = View.GONE
            binding.linewAfterImages.visibility = View.GONE
        }
        binding.rvAdvertisements.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvAdvertisements.adapter = AdvertisementAdapter(advertismentsList, this)

        binding.tvPropertySize.text = projectData!!.additionaldetails.projectarea.toString()

    }

    private fun loadProjectDetailFragment(
        projectsDataModel: ProjectsDataModel?
    ) {
        val bundle = Bundle()
        bundle.putSerializable("projectDetailModel", projectsDataModel)
        val propertyDetailFragment = newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            ProjectDetailFragment::class.java.name
        )
    }

    override fun onAdvertisemntClick(action: String, advertisementDataModel: Advertisment?) {
        when (action) {
            "advertisementClick" -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(advertisementDataModel!!.link)
                startActivity(openURL)
            }
        }
    }

    override fun onImageClick(action: String, configuration: Projectconfiguration?) {
        when (action) {
            "imageClick" -> {
                (activity as HomeActivity).callBrowserActivity(LandableConstants.Image_URL + configuration!!.uimage)
            }
        }
    }

    override fun onImageClick(action: String, propertyimage: Propertyimage) {
        when (action) {
            "imageClick" -> {
                (activity as HomeActivity).callBrowserActivity(LandableConstants.Image_URL + propertyimage.imagepath)
            }
        }
    }

    override fun onAgentClick(action: String, id: Int) {
        when (action) {
            "success" -> {
                loadProjectDetailFragment(projectDetailInfoModel)
            }
        }
    }

    fun getLocationFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(requireContext())
        val address: List<Address>?
        var p1: LatLng?
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.latitude
            location.longitude
            p1 = LatLng(location.latitude, location.longitude)


            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    override fun onLocationAdded(title: String,address:String, latlong: LatLng) {
        var distance = SphericalUtil.computeDistanceBetween(
            latlong,
            getLocationFromAddress(projectDetailInfoModel!!.Fulladdress)
        )
        distance /= 1000.0

        postAddLocation(
            PropertyDetailFragment.PostAddLocation(
                projectDetailInfoModel!!.projectid, "",
                "", address, distance.toString(), "", title, "Project"
            )
        )
    }

    private fun postAddLocation(dataModel: PropertyDetailFragment.PostAddLocation) {
        val postaddLocationResponse = RegisterRepository().postAddLocationInfo(dataModel)
        postaddLocationResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    if (it.toString() != "null") {
                        Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_LONG).show()
                        getProjectDetails(
                            projectDetailInfoModel!!.id,
                            projectDetailInfoModel!!.projectid
                        )
                        /* val status = jsonObj.getString("status")
                         val msg = jsonObj.getString("msg")
                         val propertyid = jsonObj.getString("propertyid")
                         val id = jsonObj.getInt("id")
                         if (status == "success") {
                             Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                             loadPostPropertyPageTwo(id, propertyid)
                         } else {
                             Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show()
                         }*/
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


}
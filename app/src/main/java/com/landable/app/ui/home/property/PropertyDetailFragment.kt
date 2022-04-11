package com.landable.app.ui.home.property

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
import com.landable.app.databinding.FragmentPropertyDetailBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.agent.ContactOwnerDialogFragment
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.project.adapter.AmenitiesProjectAdapter
import com.landable.app.ui.home.property.adapters.*
import org.json.JSONObject
import java.io.IOException
import kotlin.math.roundToInt


class PropertyDetailFragment : Fragment(), PropertyDetailListener, AdvertisementClickListener,
    OnMapReadyCallback, DocumentClickListener, AgentProfileListener,
    AddYourLocationDailogFragment.IAddLocation {

    private lateinit var binding: FragmentPropertyDetailBinding
    private var propertyDetailInfoModel: FeaturePropertiesDataModel? = null
    private var propertyData: PropertyDetailsModel? = null
    private var locationList = ArrayList<Distancefromlocation>()
    private var progressDialog: CustomProgressDialog? = null
    private var mMap: GoogleMap? = null
    private var imagesList = ArrayList<Propertyimage>()
    private var similarPropertyList = ArrayList<FeaturePropertiesDataModel>()
    private var featuredPropertiesList = ArrayList<FeaturePropertiesDataModel>()
    private var reviewsList = ArrayList<Review>()
    private var advertismentsList = ArrayList<Advertisment>()
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var latitude: String = ""
    private var longitude: String = ""
    private val imageArrayList = ArrayList<Propertyimage>()
    private val floorArrayList = ArrayList<Propertyimage>()
    private val documentArrayList = ArrayList<Propertyimage>()

    companion object {
        fun newInstance() = PropertyDetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        propertyDetailInfoModel =
            requireArguments().getSerializable("propertiesDetailModel") as FeaturePropertiesDataModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as HomeActivity).enableBackButton(propertyDetailInfoModel!!.title)
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_property_detail, container, false)

        binding.llOwnerPRofile.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                (activity as HomeActivity).askForLogin()
                contactOwner()
            } else {
                loadAgentProfileFragment(propertyDetailInfoModel!!.addedbyid)
            }
        }

        binding.addLocation.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = AddYourLocationDailogFragment(
                this
            )
            dialogFragment.show(fm, "")
        }

        if (propertyDetailInfoModel!!.addedbyid.toString() == AppInfo.getUserId()) {
            binding.llPropertyStrength.visibility = View.VISIBLE
        }


        binding.viewOnMap.setOnClickListener {
            val url = "http://maps.google.com/maps?daddr=${propertyDetailInfoModel!!.address}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        binding.ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val link = propertyDetailInfoModel!!.link
            val name = propertyDetailInfoModel!!.title

            val shareBody = name + "\n" + LandableConstants.Image_URL + link
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.share_subject)
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "share"))
        }

        binding.ivFavourite.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                addToFavourite(
                    AddtoFavouriteDataModel(
                        propertyDetailInfoModel!!.propertyid,
                        "property"
                    )
                )
            }
        }

        binding.postReview.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = PostReviewDialogFragment(
                propertyDetailInfoModel!!.propertyid, this, "property"
            )
            dialogFragment.show(fm, "")
        }

        binding.llContactOwner.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                (activity as HomeActivity).askForLogin()
                contactOwner()
            } else {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = ContactOwnerDialogFragment(
                    propertyDetailInfoModel!!.addedbyid,
                    propertyDetailInfoModel!!.name
                )
                dialogFragment.show(fm, "")
            }
        }


        getPropertyDetails(
            propertyDetailInfoModel!!.id,
            propertyDetailInfoModel!!.propertyid
        )

        updatePropertyDetailsUI()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (propertyDetailInfoModel!!.lat.isNullOrEmpty() || propertyDetailInfoModel!!.lon.isNullOrEmpty()) {
            lat = 28.7041
            lon = 77.1025
        } else {
            lat = propertyDetailInfoModel!!.lat.toDouble()
            lon = propertyDetailInfoModel!!.lon.toDouble()
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


        binding.tvImage.setOnClickListener {
            checkSelectedText("image", imageArrayList)
        }
        binding.tvFloorPlan.setOnClickListener {
            checkSelectedText("floorplan", floorArrayList)
        }
        binding.tvDocument.setOnClickListener {
            checkSelectedText("document", documentArrayList)
        }

        return binding.root
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


    private fun contactOwner() {
        (activity as HomeActivity).propertyID = propertyDetailInfoModel!!.propertyid
        (activity as HomeActivity).contactType = "Property"
        (activity as HomeActivity).agentID = propertyDetailInfoModel!!.addedbyid
    }

    private fun updatePropertiesDataAfterAPi() {

        if (imagesList.size == 0) {
            binding.llImages.visibility = View.GONE
            binding.linewAfterImages.visibility = View.GONE

        }
        if (propertyData!!.additionaldetails.Additionalroom == "") {
            binding.llAdditional.visibility = View.GONE
        } else binding.tvAdditionalRooms.text = propertyData!!.additionaldetails.Additionalroom

        binding.circleImageView.load(LandableConstants.Image_URL + propertyData!!.details.profilepic)

        binding.ivBadge.load(LandableConstants.Image_URL + propertyData!!.details.badges)

        binding.rvAmenities.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAmenities.adapter = AmenitiesProjectAdapter(propertyData!!.Amenitiesmasters)

        if (propertyData!!.Amenitiesmasters.size == 0) {
            binding.tvAmenities.visibility = View.GONE
        }
        binding.rvLocation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLocation.adapter =
            DistanceFromLocationAdapter(locationList)

        if (reviewsList.size == 0) {
            binding.llNoReviews.visibility = View.VISIBLE
        } else {
            binding.rvReviews.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvReviews.adapter = PropertyReviewAdapter(reviewsList)
        }

        binding.rvFeaturedProperties.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedProperties.adapter =
            FeaturePropertiesAdapter(featuredPropertiesList, this)

        binding.rvSimilarProperties.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSimilarProperties.adapter = FeaturePropertiesAdapter(similarPropertyList, this)

        binding.rvAdvertisements.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvAdvertisements.adapter = AdvertisementAdapter(advertismentsList, this)


        /*  binding.walkscoretext.text = propertyData!!.additionaldetails.wsdesc
          binding.walkStrength.text = propertyData!!.additionaldetails.wsdesc
          binding.progressWalkScore.progress = propertyData!!.additionaldetails.wscore.toInt()
  */


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

    private fun updatePropertyDetailsUI() {
        //   binding.tvPropertyName.text = propertyDetailInfoModel!!.title
        binding.tvPropertyHeading.text = propertyDetailInfoModel!!.title
        binding.ivPropertyImage.load(LandableConstants.Image_URL + propertyDetailInfoModel!!.image1)
        binding.customerType.text = propertyDetailInfoModel!!.customertype
        binding.tvPrice.text = "\u20B9 " + propertyDetailInfoModel!!.costinword
        binding.tvBathroomCount.text = propertyDetailInfoModel!!.bathroom
        binding.tvBedroomCount.text = propertyDetailInfoModel!!.bedroom
        binding.tvCategoryName.text = propertyDetailInfoModel!!.categoryname
        binding.tvPropertyID.text = propertyDetailInfoModel!!.propertyid
        binding.tvPropertyPrice.text = "\u20B9 " + propertyDetailInfoModel!!.costinword
        binding.tvPropertyLocation.text = propertyDetailInfoModel!!.address
        binding.tvSaleType.text = propertyDetailInfoModel!!.saletypename
        binding.tvPossessionName.text = propertyDetailInfoModel!!.possessionname
        binding.tvPropertyType.text = propertyDetailInfoModel!!.subcategoryname
        binding.tvPossession.text = propertyDetailInfoModel!!.possessionname
        binding.tvOwnerName.text = propertyDetailInfoModel!!.name
        binding.tvLocation.text = propertyDetailInfoModel!!.address
        binding.tvRating.text = propertyDetailInfoModel!!.rating.toString()
        binding.propertyStrength.text = propertyDetailInfoModel!!.strengthmsg
        if (propertyDetailInfoModel!!.strength > 33 && propertyDetailInfoModel!!.strength < 66) {
            binding.firstProgress.progress = 33
            binding.secondProgress.progress = 33
            binding.firstProgressImage.visibility = View.VISIBLE
        } else if (propertyDetailInfoModel!!.strength > 66) {
            binding.firstProgress.progress = 33
            binding.secondProgress.progress = 33
            binding.thirdProgress.progress = 33
            binding.secondProgressImage.visibility = View.VISIBLE
            binding.firstProgressImage.visibility = View.VISIBLE
        } else {
            binding.firstProgress.progress = 33
            binding.firstProgressImage.visibility = View.VISIBLE
        }
        if (propertyDetailInfoModel!!.isfavourite == "true") {
            binding.ivFavourite.visibility = View.GONE
            binding.ivUnFavorite.visibility = View.VISIBLE
        } else {
            binding.ivFavourite.visibility = View.VISIBLE
            binding.ivUnFavorite.visibility = View.GONE
        }

        /*  binding.walkscoretext.text = propertyData!!.additionaldetails.wscore
          binding.progressWalkScore.progress = propertyData!!.additionaldetails.wscore.toInt()
          binding.walkStrength.text = propertyData!!.additionaldetails.wsdesc*/

    }

    override fun onPropertyClick(
        action: String,
        featurePropertiesDataModel: FeaturePropertiesDataModel?
    ) {
        when (action) {
            "selectedPropertyDetail" -> {
                loadPropertyDetailFragment(featurePropertiesDataModel)
            }
        }
    }

    private fun loadPropertyDetailFragment(propertyDataModel: FeaturePropertiesDataModel?) {
        val bundle = Bundle()
        bundle.putSerializable("propertiesDetailModel", propertyDataModel)
        val propertyDetailFragment = newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            PropertyDetailFragment::class.java.name
        )
    }

    private fun addToFavourite(dataModel: AddtoFavouriteDataModel) {
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
                    val status = jsonObj.getString("status")
                    if (status == "success") {
                        binding.ivFavourite.visibility = View.GONE
                        binding.ivUnFavorite.visibility = View.VISIBLE
                    } else if (status == "exists") {
                        binding.ivFavourite.visibility = View.GONE
                        binding.ivUnFavorite.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getPropertyDetails(id: Int, propertyid: String) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val propertyResponse = RegisterRepository().getPropertyDetails(id, propertyid)
        propertyResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    propertyData = ParseResponse.parsePropertyDetailResponse(it.toString())
                    binding.tvDescription.text = propertyData!!.details.description
                    locationList = propertyData!!.distancefromlocation
                    similarPropertyList = propertyData!!.similarproperty
                    featuredPropertiesList = propertyData!!.featuredproperty
                    reviewsList = propertyData!!.review
                    advertismentsList = propertyData!!.advertisment
                    imagesList = propertyData!!.propertyimages
                    for (i in 0 until imagesList.size) {
                        if (imagesList[i].imagetype == "Image") {
                            imageArrayList.add(imagesList[i])
                        } else if (imagesList[i].imagetype == "floorplan") {
                            floorArrayList.add(imagesList[i])
                        } else if (imagesList[i].imagetype == "document") {
                            documentArrayList.add(imagesList[i])
                        }
                    }
                    checkSelectedText("image", imageArrayList)

                    if (imageArrayList.size > 0) {
                        binding.tvImage.visibility = View.VISIBLE
                    }
                    if (documentArrayList.size > 0) {
                        binding.tvDocument.visibility = View.VISIBLE
                    }

                    if (floorArrayList.size > 0) {
                        binding.tvFloorPlan.visibility = View.VISIBLE
                    }
                    //   binding.tvDiscription.text = propertyData!!.details.description
                    updatePropertiesDataAfterAPi()
                } catch (
                    e: Exception
                ) {
                    /*CustomAlertDialog(
                        requireContext(),
                        "Alert",
                        LitConstants.InvalidResponseMessage
                    ).show()*/
                    e.printStackTrace()
                }
            }
        }
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

    class AddtoFavouriteDataModel(
        propertyid: String,
        type: String
    ) {
        private val propertyid: String = propertyid
        private val type: String = type
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
                loadPropertyDetailFragment(propertyDetailInfoModel)
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
            if (address.size == 0) {
                CustomAlertDialog(
                    requireContext(),
                    "Alert",
                    "Please provide correct address."
                ).show()
            } else {
                val location: Address = address[0]
                location.latitude
                location.longitude
                p1 = LatLng(location.latitude, location.longitude)
                return p1
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    override fun onLocationAdded(title: String,address:String, latlong: LatLng) {
        var distance = SphericalUtil.computeDistanceBetween(
            latlong,
            getLocationFromAddress(propertyDetailInfoModel!!.address)
        )

        if (distance != null) {
            distance /= 1000.0.roundToInt()
            postAddLocation(
                PostAddLocation(
                    propertyDetailInfoModel!!.propertyid, "",
                    "", address, "$distance Km", "", title, "Property"
                )
            )
        }
    }

    private fun postAddLocation(dataModel: PostAddLocation) {
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
                        getPropertyDetails(
                            propertyDetailInfoModel!!.id,
                            propertyDetailInfoModel!!.propertyid
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


    class PostAddLocation(
        longid: String,
        lat: String,
        lon: String,
        address: String,
        distance: String,
        distancetime: String,
        title: String,
        type: String
    ) {
        private var longid: String = longid
        private var lat: String = lat
        private var lon: String = lon
        private var address: String = address
        private var distance: String = distance
        private var distancetime: String = distancetime
        private var title: String = title
        private var type: String = type
    }

}
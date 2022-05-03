package com.landable.app.ui.home.supergroups

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentAddSupergroupBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.categories.CategoriesAdapter
import com.landable.app.ui.home.dataModels.CategoriesDataModel
import com.landable.app.ui.home.dataModels.FilterMasterDataModel
import com.landable.app.ui.home.dataModels.PropertyTypeDataModel
import com.landable.app.ui.home.postProjectProperty.filterAdapters.PropertyTypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SaletypeAdapter
import com.landable.app.ui.home.postProjectProperty.project.ProjectUploadDocumentsFragment
import org.json.JSONObject
import java.io.IOException


class AddSuperGroupFragment : Fragment(), AgentProfileListener, CategoryTypeClickListener,
    PropertyTypeClickListener, OnMapReadyCallback {

    private lateinit var binding: FragmentAddSupergroupBinding
    private var categoryList = ArrayList<CategoriesDataModel>()
    private var filterData: FilterMasterDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var categoryID: Int = 0
    private var subCategoryID: Int = 0
    private var saleType: Int = 0
    private var possetionType: Int = 0
    private var arbitrage: Int = 0
    private var mMap: GoogleMap? = null
    private var lat: String = ""
    private var lon: String = ""

    companion object {
        fun newInstance() = AddSuperGroupFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Add Supergroup")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_supergroup, container, false)

        getFilterInfo()

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Add Supergroup Fragment", null);

        binding.buttonContinue.setOnClickListener {
            if (binding.edTitle.text.toString().isNullOrEmpty() ||
                binding.edAddress.text.toString()
                    .isNullOrEmpty() || binding.tvDescription.text.toString().isNullOrEmpty() ||
                binding.tvCOst.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(requireContext(), "Please fill all the columns", Toast.LENGTH_LONG)
                    .show()
            } else {
                postSuperGroup(
                    PostSuperGroup(
                        0,
                        binding.edTitle.text.toString(),
                        arbitrage,
                        categoryID,
                        subCategoryID,
                        saleType,
                        possetionType,
                        binding.edAddress.text.toString(),
                        binding.tvDescription.text.toString(),
                        lat,
                        lon,
                        binding.tvCOst.text.toString().toDouble()
                    )
                )
            }

        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.edAddress.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
            } else {
                getLocationFromAddress(binding.edAddress.text.toString())
                binding.edLatitude.text = lat
                binding.edLongitude.text = lon
            }
        }
        return binding.root
    }

    private fun loadPostSuperGroupMediaUpload(id: Int, projectid: String, comingFromMedia: String) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("projectID", projectid)
        bundle.putString("comingFromMedia", comingFromMedia)
        val postProjectConfigurationFragment = ProjectUploadDocumentsFragment.newInstance()
        postProjectConfigurationFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            postProjectConfigurationFragment,
            ProjectUploadDocumentsFragment::class.java.name

        )
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
            if (address.isEmpty()) {
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

                lat = location.latitude.toString()
                lon = location.latitude.toString()
                updateLocation(p1, "")

                return p1
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isCompassEnabled = true
        //  updateLocation(getLocationFromAddress( "New delhi")!!, "")
    }

    private fun updateLocation(location: LatLng, markerTitle: String) {
        if (mMap != null) {
            val zoomLevel = 12.0f //This goes up to 21
            mMap!!.addMarker(MarkerOptions().position(location).title(markerTitle))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
        }
    }

    private fun postSuperGroup(dataModel: PostSuperGroup) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val postPropertyStep1Response =
            RegisterRepository().post_Supergroup(dataModel)
        postPropertyStep1Response.observe(viewLifecycleOwner) {
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
                    if (it.toString() != "null") {
                        val jsonObj = JSONObject(it)
                        val status = jsonObj.getString("status")
                        val threadId = jsonObj.getInt("id")
                        if (status == "success") {
                            Toast.makeText(
                                requireContext(),
                                "Supergroup posted successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            loadPostSuperGroupMediaUpload(threadId, "", "supergroup")
                            /*{
                                "id": 3089,
                                "status": "success"
                            }*/
                            //   FragmentHelper().popBackStackImmediate(activity as HomeActivity)
                            //  loadPostSuperGroupMediaUpload(0,"","supergroup")
                        } else {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun getFilterInfo() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val filterResponse = RegisterRepository().getFilterMaster()
        filterResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()

            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    filterData =
                        ParseResponse.parsePostPropertyBasicInfoFilterResponse(it.toString())
                    updateDashboardInfo()
                    updateFilterInfo()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateFilterInfo() {

        binding.rvArbitrage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArbitrage.adapter =
            SaletypeAdapter(filterData!!.Arbitragemaster, this, "arbitrageClick", arbitrage)

        binding.rvSaleType.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSaleType.adapter =
            SaletypeAdapter(filterData!!.saletypemaster, this, "saleTypeClick", saleType)

        binding.rvPossessionStatus.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPossessionStatus.adapter =
            SaletypeAdapter(filterData!!.possessionmaster, this, "possessionCLick", possetionType)
    }

    private fun updateDashboardInfo() {
        categoryList = filterData!!.categorymaster
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = CategoriesAdapter(categoryList, this, categoryID)

    }

    private fun updatePropertyTypeList(categoryType: String) {
        when (categoryType) {
            "Residential" -> {
                binding.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.residentialTypeLinkedHashMap[categoryType]!!,
                        this, subCategoryID
                    )
            }
            "Commercial" -> {
                binding.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.commercialTypeLinkedList[categoryType]!!,
                        this, subCategoryID
                    )
            }
            "Agricultural" -> {
                binding.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.agriculturalTypeLinkedList[categoryType]!!,
                        this, subCategoryID
                    )
            }
        }
    }

    override fun onCategoryClick(action: String, categoryDataModel: CategoriesDataModel?) {
        when (action) {
            "categoryClick" -> {
                categoryID = categoryDataModel!!.id
                binding.rvPropertyType.visibility = View.VISIBLE
                updatePropertyTypeList(categoryDataModel.codevalue)
            }
            "removeCategoryClicked" -> {
                categoryID = 0
                binding.rvPropertyType.visibility = View.GONE
            }
        }
    }

    override fun onTypeClick(action: String, propertyDataModel: PropertyTypeDataModel?) {

        when (action) {
            "typeClick" -> {
                subCategoryID = propertyDataModel!!.id
            }
            "deleteTypeClick" -> subCategoryID = 0

        }
    }

    class PostSuperGroup(
        id: Int,
        title: String,
        arbitrage: Int,
        category: Int,
        subcategory: Int,
        saletype: Int,
        possession: Int,
        locality: String,
        description: String,
        lat: String,
        lon: String,
        price: Double
    ) {
        private var id: Int = id
        private var title: String = title
        private var arbitrage: Int = arbitrage
        private var category: Int = category
        private var subcategory: Int = subcategory
        private var saletype: Int = saletype
        private var possession: Int = possession
        private var locality: String = locality
        private var description: String = description
        private var lat: String = lat
        private var lon: String = lon
        private var price: Double = price
    }

    override fun onAgentClick(action: String, id: Int) {
        when (action) {
            "saleTypeClick" -> {
                saleType = id
            }
            "possessionCLick" -> {
                possetionType = id
            }
            "deleteSaleTypeClick" -> {
                saleType = 0
            }
            "deletePossessionCLick" -> {
                possetionType = 0
            }
            "arbitrageClick" -> {
                arbitrage = id
            }
            "deleteArbitrageClick" -> {
                arbitrage = 0
            }
        }
    }
}

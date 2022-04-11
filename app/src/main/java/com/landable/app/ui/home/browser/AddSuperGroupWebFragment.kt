package com.landable.app.ui.home.browser

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
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.CategoryTypeClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.common.PropertyTypeClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.ContentSupergroupWebBinding
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.categories.CategoriesAdapter
import com.landable.app.ui.home.dataModels.CategoriesDataModel
import com.landable.app.ui.home.dataModels.FilterMasterDataModel
import com.landable.app.ui.home.dataModels.PropertyTypeDataModel
import com.landable.app.ui.home.postProjectProperty.filterAdapters.PropertyTypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SaletypeAdapter
import java.io.IOException

class AddSuperGroupWebFragment : Fragment(), AgentProfileListener, CategoryTypeClickListener,
    PropertyTypeClickListener, OnMapReadyCallback {

    private lateinit var binding: ContentSupergroupWebBinding
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
        fun newInstance() = AddSuperGroupWebFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.content_supergroup_web, container, false)

        getFilterInfo()

        binding.ivBack.setOnClickListener {
            (activity as ChatActivity).finish()
        }

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

    private fun clearPostForm() {
        binding.edTitle.text.clear()
        arbitrage = 0
        categoryID = 0
        subCategoryID = 0
        saleType = 0
        possetionType = 0
        binding.edAddress.text.clear()
        binding.tvDescription.text.clear()
        lat = ""
        lon = ""
        binding.tvCOst.text.clear()
        getFilterInfo()
        binding.rvPropertyType.visibility = View.GONE
        binding.edLatitude.text = ""
        binding.edLongitude.text = ""
        CustomAlertDialog(requireContext(), "Alert", "Posted Successfully.").show()
    }

    /*  private fun loadPostSuperGroupMediaUpload(id: Int, projectid: String, comingFromMedia: String) {
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
      }*/


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
                CustomAlertDialog(requireContext(), "Alert", "Address not found!").show()
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
            RegisterRepository().post_WebSupergroup(dataModel)
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
                    if (it != "null") {
                        if (it.contains("success")) {
                            clearPostForm()
                            //  loadPostSuperGroupMediaUpload(0,"","supergroup")
                        } else {
                            CustomAlertDialog(requireContext(), "Alert", it).show()
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
        binding.rvPropertyType.visibility = View.VISIBLE
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

package com.landable.app.ui.home.postProjectProperty.project

import android.app.DatePickerDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
import com.landable.app.databinding.FragmentPostProjectBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.categories.CategoriesAdapter
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.postProjectProperty.filterAdapters.AmenitiesAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.PropertyTypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SaletypeAdapter
import com.landable.app.ui.home.profile.CityAdapter
import com.landable.app.ui.home.profile.StateAdapter
import org.json.JSONObject
import java.io.IOException
import java.util.*

class PostProjectBasicInfoFragment : Fragment(), PropertyTypeClickListener,
    CategoryTypeClickListener,
    AgentProfileListener, AmenitiesClickListener, OnMapReadyCallback {

    private lateinit var binding: FragmentPostProjectBinding
    private var progressDialog: CustomProgressDialog? = null
    private var stateList = ArrayList<Statemaster>()
    private var cityArrayAdapter: CityAdapter? = null
    private var stateData: FilterMasterDataModel? = null
    private var stateId: Int = 0
    private var cityId: Int = 0
    private var unitId: Int = 0
    private var categoryID: Int = 0
    private var subCategoryID: Int = 0
    private var filterData: FilterMasterDataModel? = null
    private var categoryList = ArrayList<CategoriesDataModel>()
    private var possetionType: Int = 0
    private var amenitiesArray = ArrayList<Amenitiesmaster>()
    private var lat: String = ""
    private var lon: String = ""
    private var mMap: GoogleMap? = null
    private var isComingForWhichEditType: String? = null
    private var projectInfo: ProjectsDataModel? = null
    private var isComingForEdit: Boolean = false
    private var projectData: ProjectDetailDataModel? = null

    companion object {
        fun newInstance() = PostProjectBasicInfoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isComingForWhichEditType = requireArguments().getString("isComingForWhichEditType")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_post_project, container,
                false
            )

        (activity as HomeActivity).hideBottomNavigation()
        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Post Project Fragment", null);

        if (isComingForWhichEditType == "ProjectEdit") {
            projectInfo =
                requireArguments().getSerializable("projectsDataModel") as ProjectsDataModel?

            isComingForEdit = true

            getProjectDetails(projectInfo!!.id, projectInfo!!.projectid)

        }
        getFilterInfo()


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding.buttonContinue.setOnClickListener {
       //     loadPostProjectConfigurationPage(66, "PROJ22231704")
          if (binding.tvLaunchDate.text.toString().isNullOrEmpty()
                 || binding.edTitle.text.toString().isNullOrEmpty() ||
                 binding.tvPossessionDate.text.toString()
                     .isNullOrEmpty() || binding.edAddress.text.toString()
                     .isNullOrEmpty() ||
                 binding.edAddress.text.toString()
                     .isNullOrEmpty() || binding.edPincode.text.toString().isNullOrEmpty()
             ) {
                 Toast.makeText(requireContext(), "Please fill all the columns", Toast.LENGTH_LONG)
                     .show()
             } else {
                 postProjectStep1(
                     PostProjectBasicDataModel(
                         0,
                         "",
                         binding.edTitle.text.toString(),
                         possetionType,
                         binding.tvLaunchDate.text.toString(),
                         binding.tvPossessionDate.text.toString(),
                         binding.edUnits.text.toString().toInt(),
                         binding.edTotalTowers.text.toString().toInt(),
                         categoryID,
                         subCategoryID,
                         binding.edTotalArea.text.toString().toDouble(),
                         stateId,
                         cityId,
                         binding.edAddress.text.toString(),
                         binding.edPincode.text.toString(),
                         getIdsWithString(),
                         binding.edDescription.text.toString(),
                         binding.edLatitude.text.toString(),
                         binding.edLongitude.text.toString(),
                         binding.edRera.text.toString()
                     )
                 )
             }

        }

        binding.edAddress.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
            } else {
                getLocationFromAddress(binding.edAddress.text.toString())
                binding.edLatitude.text = lat
                binding.edLongitude.text = lon
            }
        }

        // update current date
        val mCalendar = Calendar.getInstance()
        val year = mCalendar[Calendar.YEAR]
        val month = mCalendar[Calendar.MONTH]
        val date = mCalendar[Calendar.DAY_OF_MONTH]

        val finalDate =
            "" + year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", date)
        binding.tvLaunchDate.text = finalDate
        binding.tvPossessionDate.text = finalDate

        binding.tvLaunchDate.setOnClickListener {
            val mDate: Int = (binding.tvLaunchDate.text.substring(8, 10)).toInt()
            val mMonth = (binding.tvLaunchDate.text.substring(5, 7)).toInt()
            val mYear = (binding.tvLaunchDate.text.substring(0, 4)).toInt()
            updateDateMonthYear(binding.tvLaunchDate, mDate, mMonth - 1, mYear, false)
        }

        binding.tvPossessionDate.setOnClickListener {
            val mDate: Int = (binding.tvPossessionDate.text.substring(8, 10)).toInt()
            val mMonth = (binding.tvPossessionDate.text.substring(5, 7)).toInt()
            val mYear = (binding.tvPossessionDate.text.substring(0, 4)).toInt()
            updateDateMonthYear(binding.tvPossessionDate, mDate, mMonth - 1, mYear, true)
        }

        return binding.root
    }

    private fun updateAvailableUI() {
        binding.edTitle.setText(projectData!!.details.projectname)
        binding.tvLaunchDate.text = projectData!!.additionaldetails.launchdate.take(9)
        binding.tvPossessionDate.text = projectData!!.additionaldetails.launchdate.take(9)
        binding.edUnits.setText(projectData!!.additionaldetails.totalunits)
        binding.edTotalTowers.setText(projectData!!.additionaldetails.totaltowers)
        binding.edRera.setText(projectData!!.additionaldetails.rera)
        binding.edAddress.setText(projectData!!.details.Fulladdress)
        binding.edLatitude.text = projectData!!.details.lat
        binding.edLongitude.text = projectData!!.details.lon
        binding.edDescription.setText(projectData!!.details.description)
    }

    private fun getProjectDetails(id: Int, projectId: String) {
        val propertyResponse = RegisterRepository().getProjectDetails(id, projectId)
        propertyResponse.observe(viewLifecycleOwner) {
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
                    updateAvailableUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
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
                lon = location.longitude.toString()
                updateLocation(p1, "")

                return p1
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    private fun postProjectStep1(dataModel: PostProjectBasicDataModel) {
        val postProjectStep1Response = RegisterRepository().post_AddUpdateProjectstep1(dataModel)
        postProjectStep1Response.observe(viewLifecycleOwner) {

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
                        val msg = jsonObj.getString("msg")
                        val projectid = jsonObj.getString("propertyid")
                        val id = jsonObj.getInt("id")
                        if (status == "success") {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                            loadPostProjectConfigurationPage(id, projectid)
                        } else {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
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
                    stateData = ParseResponse.parseStateCityResponse(it.toString())
                    filterData =
                        ParseResponse.parsePostPropertyBasicInfoFilterResponse(it.toString())
                    stateList = stateData!!.statemaster
                    updateDashboardInfo()
                    ///Added city dropdown in post property package
                    updateStateCityDropDown()
                    //updateUnitDropdown()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateDashboardInfo() {

        binding.rvAmenities.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAmenities.adapter =
            AmenitiesAdapter(stateData!!.Amenitiesmaster, this)

        categoryList = filterData!!.categorymaster
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = CategoriesAdapter(categoryList, this,categoryID)

        binding.rvPossessionStatus.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPossessionStatus.adapter =
            SaletypeAdapter(filterData!!.possessionmaster, this, "possessionCLick",possetionType)

    }

    private fun updatePropertyTypeList(categoryType: String) {
        when (categoryType) {
            "Residential" -> {
                binding.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.residentialTypeLinkedHashMap[categoryType]!!,
                        this,subCategoryID
                    )
            }
            /* "Commercial" -> {
                 binding.rvPropertyType.layoutManager =
                     LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                 binding.rvPropertyType.adapter =
                     PropertyTypeAdapter(
                         filterData!!.commercialTypeLinkedList[categoryType]!!,
                         this
                     )
             }
             "Agricultural" -> {
                 binding.rvPropertyType.layoutManager =
                     LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                 binding.rvPropertyType.adapter =
                     PropertyTypeAdapter(
                         filterData!!.agriculturalTypeLinkedList[categoryType]!!,
                         this
                     )
             }*/
        }
    }

    /*  private fun updateUnitDropdown() {
          // create an array adapter and pass the required parameter
          // in our case pass the context, drop down layout , and array.
          val arrayAdapter = UnitDropdownAdapter(requireActivity(), filterData!!.Unitmaster)

          // set adapter to the autocomplete tv to the arrayAdapter
          binding.autoCompleteTextViewForUnit.setAdapter(arrayAdapter)

          binding.autoCompleteTextViewForUnit.onItemClickListener =
              AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                  //this is the way to find selected object/item
                  unitId = stateData!!.Unitmaster[pos].id

                  binding.tvAreaUnit.text = stateData!!.Unitmaster[pos].codevalue


              }
      }*/

    private fun updateStateCityDropDown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = StateAdapter(requireActivity(), stateList)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.autoCompleteTextViewForState.setAdapter(arrayAdapter)

        binding.autoCompleteTextViewForState.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                stateId = stateList[pos].id
                updateCityList(stateId)
            }
    }

    private fun updateCityList(stateId: Int) {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        if (cityArrayAdapter == null) {
            cityArrayAdapter = CityAdapter(requireActivity(), stateData!!.cityHashMap[stateId])
        } else {
            cityArrayAdapter!!.updateList(stateData!!.cityHashMap[stateId])
        }
        // set adapter to the autocomplete tv to the arrayAdapter
        binding.autoCompleteTextViewForCity.setAdapter(cityArrayAdapter)

        binding.autoCompleteTextViewForCity.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                cityId = (stateData!!.cityHashMap[stateId] as ArrayList<Citymaster>)[pos].id
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

    override fun onAgentClick(action: String, id: Int) {
        when (action) {
            "possessionCLick" -> {
                possetionType = id
            }
            "deletePossessionCLick" -> {
                possetionType = 0
            }
        }
    }

    private fun updateAmenitiesList(list: ArrayList<Amenitiesmaster>) {
        amenitiesArray = list
    }

    private fun getIdsWithString(): String {
        var amentiesId = ""
        for (i in 0 until amenitiesArray.size) {
            if (amentiesId.isBlank()) {
                amentiesId = amenitiesArray[i].id.toString()
            } else {
                amentiesId = amentiesId + "," + amenitiesArray[i].id.toString()
            }
        }
        return amentiesId
    }

    override fun onAmenitiesSelected(
        action: String?,
        selectedAmenitiesList: ArrayList<Amenitiesmaster>
    ) {
        when (action) {
            "addAmenities" -> {
                updateAmenitiesList(selectedAmenitiesList)
            }
        }

    }

    private fun updateDateMonthYear(
        dateView: TextView,
        date: Int,
        month: Int,
        year: Int,
        isSetMinimumDate: Boolean
    ) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Dialog,
            { _, year, month, date ->
                val _date = String.format("%02d", date) + "-" + String.format(
                    "%02d",
                    (month + 1)
                ) + "-" + year
                dateView.text = _date
            }, year, month, date
        )
        if (isSetMinimumDate) {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        }
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        datePickerDialog.setTitle("Select Date")
        datePickerDialog.show()
    }

    private fun loadPostProjectConfigurationPage(id: Int, projectid: String) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("projectID", projectid)
        bundle.putBoolean("isComingForEdit", isComingForEdit)
        if (isComingForEdit) {
            bundle.putSerializable("projectDataModel", projectData)
        }
        val postProjectConfigurationFragment = PostProjectConfigurationFragment.newInstance()
        postProjectConfigurationFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            postProjectConfigurationFragment,
            PostProjectConfigurationFragment::class.java.name

        )
    }

    class PostProjectBasicDataModel(
        id: Int,
        projectid: String,
        projectname: String,
        possessionstatus: Int,
        launchdate: String,
        possessionstartdate: String,
        totalunits: Int,
        totaltowers: Int,
        category: Int,
        subcategory: Int,
        projectarea: Double,
        state: Int,
        city: Int,
        fulladdress: String,
        pincode: String,
        amenities: String,
        description: String,
        lat: String,
        lon: String,
        rera: String
    ) {
        private val id: Int = id
        private val projectid: String = projectid
        private val category: Int = category
        private val subcategory: Int = subcategory
        private val projectname: String = projectname
        private val description: String = description
        private val totalunits: Int = totalunits
        private val projectarea: Double = projectarea
        private val totaltowers: Int = totaltowers
        private val possessionstatus: Int = possessionstatus
        private val launchdate: String = launchdate
        private val possessionstartdate: String = possessionstartdate
        private val pincode: String = pincode
        private val state: Int = state
        private val city: Int = city
        private val rera: String = rera
        private val amenities: String = amenities
        private val fulladdress: String = fulladdress
        private val lat: String = lat
        private val lon: String = lon
    }
}
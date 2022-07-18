package com.landable.app.ui.home.postProjectProperty.property

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostPropertyBasicInfoBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.categories.CategoriesAdapter
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.postProjectProperty.filterAdapters.BedsDropdownAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.PropertyTypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SaletypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.UnitDropdownAdapter
import org.json.JSONObject
import java.io.IOException


class PostPropertyBasicInfoFragment : Fragment(), CategoryTypeClickListener,
    PropertyTypeClickListener, AdapterView.OnItemSelectedListener,
    AgentProfileListener {

    private lateinit var binding: FragmentPostPropertyBasicInfoBinding
    private var categoryList = ArrayList<CategoriesDataModel>()
    private var filterData: FilterMasterDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var bedsArray = ArrayList<BedsDataModel>()
    private var unitId: Int = 0
    private var categoryID: Int = 0
    private var subCategoryID: Int = 0
    private var saleType: Int = 0
    private var possetionType: Int = 0
    private var bedsCOunt: Int = 0
    private var mCurrLocationMarker: Marker? = null
    // private var mMap: GoogleMap? = null

    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var propertyData: PropertyRawDataModel? = null
    private var propertyInfo: FeaturePropertiesDataModel? = null
    private var isComingForWhichEditType: String? = null
    private var isComingForEdit: Boolean = false
    private var propertyId: Int = 0
    private var propertyFullId: String = ""


    var description = ""
    var cost = 0.0
    var title = ""
    var totalArea = 0.0
    var address = ""
    var buildUpArea = 0.0
    var carpetArea = 0.0
    var selectedUnit:String =""

    companion object {
        fun newInstance() = PostPropertyBasicInfoFragment()
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

        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_post_property_basic_info,
                container,
                false)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Post property basic info page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.llPostPropertyLayout1)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Post Property Fragment", null)

        if (isComingForWhichEditType == "PropertyEdit") {
            propertyInfo =
                requireArguments().getSerializable("propertiesDataModel") as FeaturePropertiesDataModel?

            isComingForEdit = true

            propertyId = propertyInfo!!.id
            propertyFullId = propertyInfo!!.propertyid
            getPropertyDetails(
                propertyInfo!!.id,
            )
        } else {
            getFilterInfo()
        }


        for (i in 1 until 20) {
            bedsArray.add(BedsDataModel(i, i.toString()))
        }

        val bedsadapter = BedsDropdownAdapter(requireActivity(), bedsArray)
        // set adapter to the autocomplete tv to the arrayAdapter
        binding.spinnerBeds.setAdapter(bedsadapter)

        binding.spinnerBeds.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                bedsCOunt = pos + 1
            }

        if (binding.carpetArea.text.toString().isNotEmpty()) {
            carpetArea = binding.carpetArea.text.toString().toDouble()
        }

        binding.buttonExit.setOnClickListener {
            /*   //PROP22222602
            //88715
            loadPostPropertyPageTwo(88715,"PROP22222602")*/
            description = binding.tvDescription.text.toString()
            if (binding.tvCOst.text.toString().isNotEmpty()) {
                cost = binding.tvCOst.text.toString().toDouble()
            }


            if (binding.edTotalArea.text.toString().isNotEmpty()) {
                totalArea = binding.edTotalArea.text.toString().toDouble()
            }

            address = binding.edAddress.text.toString()

            if (binding.buildUpArea.text.toString().isNotEmpty()) {
                buildUpArea = binding.buildUpArea.text.toString().toDouble()
            }
            if (binding.carpetArea.text.toString().isNotEmpty()) {
                carpetArea = binding.carpetArea.text.toString().toDouble()
            }

            if (binding.edTitle.text.toString().isNullOrEmpty()) {
                CustomAlertDialog(requireContext(), "Alert", "Please fill Title").show()
            } else {
                title = binding.edTitle.text.toString()
                postPropertyStep1(
                    PostPropertyBasicInfo(
                        propertyId,
                        propertyFullId,
                        "",
                        categoryID,
                        subCategoryID,
                        saleType,
                        possetionType,
                        bedsCOunt,
                        description,
                        cost,
                        title,
                        getModifiedArea(totalArea),
                        address,
                        lat,
                        lon,
                        getModifiedArea(buildUpArea),
                        getModifiedArea(carpetArea),
                    ), true
                )
            }
        }



        binding.buttonContinue.setOnClickListener {
            /*   //PROP22222602
               //88715
               loadPostPropertyPageTwo(88715,"PROP22222602")*/
            description = binding.tvDescription.text.toString()
            if (binding.tvCOst.text.toString().isNotEmpty()) {
                cost = binding.tvCOst.text.toString().toDouble()
            }
            title = binding.edTitle.text.toString()

            if (binding.edTotalArea.text.toString().isNotEmpty()) {
                totalArea = binding.edTotalArea.text.toString().toDouble()
            }
            address = binding.edAddress.text.toString()
            if (binding.buildUpArea.text.toString().isNotEmpty()) {
                buildUpArea = binding.buildUpArea.text.toString().toDouble()
            }
            if (binding.carpetArea.text.toString().isNotEmpty()) {
                carpetArea = binding.carpetArea.text.toString().toDouble()
            }

            postPropertyStep1(
                PostPropertyBasicInfo(
                    propertyId,
                    propertyFullId,
                    "",
                    categoryID,
                    subCategoryID,
                    saleType,
                    possetionType,
                    bedsCOunt,
                    description,
                    cost,
                    title,
                    getModifiedArea(totalArea),
                    address,
                    lat,
                    lon,
                    getModifiedArea(buildUpArea),
                    getModifiedArea(carpetArea),
                ), false
            )
        }

        binding.tvCOst.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (binding.tvCOst.text.toString().isEmpty()) {
                        binding.tvPriceIndicator.text = "\u20B9 0"
                    } else setPriceText(binding.tvCOst.text.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    // set oid value now

                }
            })

//        val mapFragment =
//            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        binding.edLatitude.setOnClickListener {
            if (lat == 0.0) {
                if (binding.edAddress.text.toString().isNotEmpty()) {
                    getLocationFromAddress(binding.edAddress.text.toString())
                    binding.edLatitude.text = lat.toString()
                    binding.edLongitude.text = lon.toString()
                } else {
                    Toast.makeText(requireContext(), "Please fill your address", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        binding.edLongitude.setOnClickListener {
            if (lat == 0.0) {
                if (binding.edAddress.text.toString().isNotEmpty()) {
                    getLocationFromAddress(binding.edAddress.text.toString())
                    binding.edLatitude.text = lat.toString()
                    binding.edLongitude.text = lon.toString()
                } else {
                    Toast.makeText(requireContext(), "Please fill your address", Toast.LENGTH_LONG)
                        .show()
                }
            }

        }
        binding.edAddress.setOnFocusChangeListener { v, hasFocus ->
            when {
                hasFocus -> {
                }
                else -> {
                    getLocationFromAddress(binding.edAddress.text.toString())
                    binding.edLatitude.text = lat.toString()
                    binding.edLongitude.text = lon.toString()
                }
            }
        }

        return binding.root
    }


    private fun getModifiedArea(area:Double):Double{

        var modifiedArea = 0.0
        if(selectedUnit=="Sq-ft"){
            modifiedArea = area
        }else if(selectedUnit=="Sq-mtr"){
            modifiedArea = area * 10.76391042
        }else if(selectedUnit=="Acre"){
            modifiedArea = area * 43560.00001
        }else if(selectedUnit=="Hectare"){
            modifiedArea = area * 107639.1042
        }else if(selectedUnit=="Sq-kmtr"){
            modifiedArea = area * 10763910.42
        }else if(selectedUnit=="Sq-Gaj"){
            modifiedArea = area * 8.9999251
        }else if(selectedUnit=="Bigha"){
            modifiedArea = area * 27225
        }else if(selectedUnit=="Sq-yrd(Gaj)"){
            modifiedArea = area * 9.000000878
        }

        return modifiedArea
    }
    private fun setPriceText(priceInWords: String) {
        val first = priceInWords[0]

        when {
            priceInWords.length < 4 -> {
                binding.tvPriceIndicator.text = "\u20B9 $priceInWords"
            }
            priceInWords.length == 4 -> {
                val second = priceInWords[1]
                binding.tvPriceIndicator.text = "\u20B9 $first.$second k"
            }
            priceInWords.length == 5 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first$second.$third k"
            }
            priceInWords.length == 6 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first.$second$third L"
            }
            priceInWords.length == 7 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first$second.$third L"
            }
            priceInWords.length == 8 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first.$second$third Cr"
            }
            priceInWords.length == 9 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first$second.$third Cr"
            }
            priceInWords.length == 10 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                val fourth = priceInWords[3]
                binding.tvPriceIndicator.text = "\u20B9 $first$second$third.$fourth Cr"
            }
            priceInWords.length == 11 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                val fourth = priceInWords[3]
                val fifth = priceInWords[4]
                binding.tvPriceIndicator.text = "\u20B9 $first$second$third$fourth.$fifth Cr"
            }
        }
    }


    private fun updateDataForEdit() {
        binding.edTitle.setText(propertyInfo!!.title)
        binding.edTotalArea.setText(propertyInfo!!.totalarea)
        val constChanged = propertyData!!.propertyraw[0].cost.toString().substringBefore(".")

        binding.tvCOst.setText(constChanged)
        setPriceText(constChanged)
        binding.edAddress.setText(propertyInfo!!.address)
        binding.edLatitude.text = propertyInfo!!.lat
        binding.edLongitude.text = propertyInfo!!.lon
        binding.tvDescription.setText(propertyInfo!!.description)
        binding.spinnerBeds.setText(propertyInfo!!.bedroom)
        bedsCOunt = propertyInfo!!.bedroom.toInt()
        binding.tvAreaUnit.text = "Square Feet"
        binding.tvCarpetAreaUnit.text = "Square Feet"
        binding.tvBuiltUpAreaUnit.text = "Square Feet"
        binding.autoCompleteTextViewForUnit.setText("Square Feet")

        binding.buildUpArea.setText(propertyData!!.propertyraw[0].builduparea.toString())
        binding.carpetArea.setText(propertyData!!.propertyraw[0].carpetarea.toString())
//        lat = propertyData!!.propertyraw[0].lat.toDouble()
        //   lon = propertyData!!.propertyraw[0].lon.toDouble()

        if (propertyInfo!!.lat == "") {
            getLocationFromAddress(propertyInfo!!.address)
            binding.edLatitude.text = lat.toString()
            binding.edLongitude.text = lon.toString()
        } else {
            lat = propertyData!!.propertyraw[0].lat.toDouble()
            lon = propertyData!!.propertyraw[0].lon.toDouble()
        }


    }


    private fun getPropertyDetails(id: Int) {
        val propertyResponse = RegisterRepository().getPropertyforeditByID(id)
        propertyResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    propertyData = ParseResponse.parsePropertyRawDataModel(it.toString())
                    updateDataForEdit()
                    categoryID = propertyData!!.propertyraw[0].category
                    subCategoryID = propertyData!!.propertyraw[0].subcategory
                    saleType = propertyData!!.propertyraw[0].saletype
                    possetionType = propertyData!!.propertyraw[0].possessionstatus
                    getFilterInfo()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        Toast.makeText(
            requireContext(),
            "You Select: " + (position + 1).toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun loadPostPropertyPageTwo(id: Int, propertyid: String) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("propertyid", propertyid)
        bundle.putString("address", address)
        bundle.putBoolean("isComingForEdit", isComingForEdit)
        if (isComingForWhichEditType == "PropertyEdit") {
            bundle.putSerializable("propertyRaw", propertyData)
        }

        val postPropertyLocationFragment = PostPropertyLocationFragment.newInstance()
        postPropertyLocationFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            postPropertyLocationFragment,
            PostPropertyLocationFragment::class.java.name

        )
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
                    updateUnitDropdown()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun updateUnitDropdown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = UnitDropdownAdapter(requireActivity(), filterData!!.Unitmaster)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.autoCompleteTextViewForUnit.setAdapter(arrayAdapter)

        binding.autoCompleteTextViewForUnit.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                unitId = filterData!!.Unitmaster[pos].id

                selectedUnit = filterData!!.Unitmaster[pos].codevalue
                binding.tvAreaUnit.text = filterData!!.Unitmaster[pos].codevalue
                binding.tvBuiltUpAreaUnit.text = filterData!!.Unitmaster[pos].codevalue
                binding.tvCarpetAreaUnit.text = filterData!!.Unitmaster[pos].codevalue
            }
    }

    private fun updateFilterInfo() {
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

    private fun postPropertyStep1(dataModel: PostPropertyBasicInfo, isClicedExit: Boolean) {
        val postPropertyStep1Response = RegisterRepository().postPropertyStep1(dataModel)
        postPropertyStep1Response.observe(viewLifecycleOwner) {

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
                        val propertyid = jsonObj.getString("propertyid")
                        val id = jsonObj.getInt("id")
                        if (status == "success" || status == "updated") {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                            if (isClicedExit) {
                                loadHomeFragment()
                            } else loadPostPropertyPageTwo(id, propertyid)
                        } else {
                            Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun loadHomeFragment() {
        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            HomeFragment.newInstance(),
            HomeFragment::class.java.name
        )
    }


    class PostPropertyBasicInfo(
        id: Int,
        propertyid: String,
        projectid: String,
        category: Int,
        subcategory: Int,
        saletype: Int,
        possessionstatus: Int,
        bed: Int,
        description: String,
        cost: Double,
        title: String,
        totalarea: Double,
        address1: String,
        lat: Double,
        lon: Double,
        builduparea: Double,
        caretareaarea: Double,

        ) {
        private val id: Int = id
        private val propertyid: String = propertyid
        private val projectid: String = projectid
        private val category: Int = category
        private val subcategory: Int = subcategory
        private val title: String = title
        private val description: String = description
        private val bed: Int = bed
        private val cost: Double = cost
        private val totalarea: Double = totalarea
        private val builduparea: Double = builduparea
        private val caretareaarea: Double = caretareaarea
        private val saletype: Int = saletype
        private val possessionstatus: Int = possessionstatus
        private val address1: String = address1
        private val lat: Double = lat
        private val lon: Double = lon
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

                lat = location.latitude
                lon = location.longitude
                // updateLocation(p1, "")

                return p1
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
//        mMap!!.uiSettings.isCompassEnabled = true
//        //  updateLocation(getLocationFromAddress( "New delhi")!!, "")
//    }
//
//    private fun updateLocation(location: LatLng, markerTitle: String) {
//        if (mMap != null) {
//            val zoomLevel = 12.0f //This goes up to 21
//            mMap!!.addMarker(MarkerOptions().position(location).title(markerTitle))
//            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
//        }
//    }


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
        }
    }
}


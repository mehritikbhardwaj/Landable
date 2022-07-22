package com.landable.app.ui.home.supergroups

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
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
import com.landable.app.ui.home.dataModels.Arbitragemaster
import com.landable.app.ui.home.dataModels.CategoriesDataModel
import com.landable.app.ui.home.dataModels.FilterMasterDataModel
import com.landable.app.ui.home.dataModels.PropertyTypeDataModel
import com.landable.app.ui.home.postProjectProperty.filterAdapters.PropertyTypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SaletypeAdapter
import com.landable.app.ui.home.postProjectProperty.project.ProjectUploadDocumentsFragment
import org.json.JSONObject
import java.io.IOException


class AddSuperGroupFragment : Fragment(), AgentProfileListener, CategoryTypeClickListener,
    PropertyTypeClickListener, ArbitrageTypeClick {

    private lateinit var binding: FragmentAddSupergroupBinding
    private var categoryList = ArrayList<CategoriesDataModel>()
    private var filterData: FilterMasterDataModel? = null
    private var progressDialog: CustomProgressDialog? = null

    private var details = ArrayList<SupergroupDetailModel>()

    private var categoryID: Int = 0
    private var subCategoryID: Int = 0
    private var saleType: Int = 0
    private var possetionType: Int = 0
    private var arbitrage: Int = 0

    //  private var mMap: GoogleMap? = null
    private var lat: String = ""
    private var lon: String = ""
    private var hideContactInfo: Int = 0
    private var commission: Int = 0
    private var cost: Int = 0
    private var threadId: Int = 0
    private var isComingForEdit: Boolean = true

    companion object {
        fun newInstance() = AddSuperGroupFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isComingForEdit = requireArguments().getBoolean("isComingForEdit")
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


        if (isComingForEdit) {
            threadId = requireArguments().getInt("threadId")

            getsupergroupDetail(threadId)
        } else {
            getFilterInfo()
        }

        binding.hideInfo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                hideContactInfo = 1
                //Toast.makeText(requireContext(),"InfoHide",Toast.LENGTH_LONG).show()
            } else hideContactInfo = 0
        }

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Add Supergroup Fragment", null)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Add thread page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        binding.tvCOst.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.tvCOst.text.toString().isEmpty()) {
                    binding.tvPriceIndicator.text = "\u20B9 0"
                } else setPriceText(binding.tvCOst.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                // set oid value now
            }
        })

        binding.buttonContinue.setOnClickListener {
            if (binding.edTitle.text.toString().isNullOrEmpty() ||
                binding.edAddress.text.toString()
                    .isNullOrEmpty() || binding.tvDescription.text.toString().isNullOrEmpty() ||
                binding.tvCOst.text.toString().isNullOrEmpty()
            ) {
                Toast.makeText(requireContext(), "Please fill all the columns", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (binding.edCommission.text.toString() == "0" || binding.edCommission.text.toString()
                        .isEmpty()
                ) {
                    commission = 0
                } else {
                    commission = binding.edCommission.text.toString().toInt()
                }
                postSuperGroup(
                    PostSuperGroup(
                        threadId,
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
                        binding.tvCOst.text.toString().toDouble(),
                        hideContactInfo, commission, ""
                    )
                )
            }

        }
//        val mapFragment =
//            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
//        mapFragment.getMapAsync(this)


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
                lon = location.longitude.toString()
                //      updateLocation(p1, "")

                return p1
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

//
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
            SaletypeAdapter(filterData!!.Arbitragemaster, this, "arbitrageClick", arbitrage,"")

        binding.rvSaleType.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSaleType.adapter =
            SaletypeAdapter(filterData!!.saletypemaster, this, "saleTypeClick", saleType,"")

        binding.rvPossessionStatus.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPossessionStatus.adapter =
            SaletypeAdapter(filterData!!.possessionmaster, this, "possessionCLick", possetionType,"")
    }

    private fun updateDashboardInfo() {
        categoryList = filterData!!.categorymaster
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = CategoriesAdapter(categoryList, this, categoryID,"")

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
                    ,"")
            }
            "Commercial" -> {
                binding.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.commercialTypeLinkedList[categoryType]!!,
                        this, subCategoryID
                    ,"")
            }
            "Agricultural" -> {
                binding.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.agriculturalTypeLinkedList[categoryType]!!,
                        this, subCategoryID
                    ,"")
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
        price: Double,
        hidecontact: Int,
        commision: Int,
        hashtags: String
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
        private var hidecontact: Int = hidecontact
        private var commision: Int = commision
        private var hashtags: String = hashtags

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

    private fun getsupergroupDetail(id: Int) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val userDataResponse = RegisterRepository().getSupergroupDetails(id)
        userDataResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancel()
            if (it.toString() != "null") {
                try {
                    details = ParseResponse.parsesupergroupDetailResponse(it.toString())
                    categoryID = details[0].category
                    subCategoryID = details[0].subcategory
                    saleType = details[0].saletype
                    possetionType = details[0].possession
                    arbitrage = details[0].arbitrage
                    updateUi(details)

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUi(details: ArrayList<SupergroupDetailModel>) {

        getFilterInfo()
        binding.edTitle.setText(details[0].title)

        val constChanged = details[0].price.toString().substringBefore(".")

        binding.tvCOst.setText(constChanged)
        setPriceText(constChanged)
        binding.edAddress.setText(details[0].locality)
        getLocationFromAddress(details[0].locality)
        binding.tvDescription.setText(details[0].description)
        if (hideContactInfo == 1) {
            binding.hideInfo.isChecked = true
        }
        binding.edCommission.setText(details[0].commission.toString())

    }

    override fun onArbitrageClick(action: String, activity: Arbitragemaster) {
        when (action) {
            "saleTypeClick" -> {
                saleType = activity.id
            }
            "possessionCLick" -> {
                possetionType = activity.id
            }
            "deleteSaleTypeClick" -> {
                saleType = 0
            }
            "deletePossessionCLick" -> {
                possetionType = 0
            }
            "arbitrageClick" -> {
                arbitrage = activity.id
            }
            "deleteArbitrageClick" -> {
                arbitrage = 0
            }
        }    }
}

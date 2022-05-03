package com.landable.app.ui.home.auction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentAuctionsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.auction.filterAdapters.BankNameAdapter
import com.landable.app.ui.home.auction.filterAdapters.BorrowerNameAdapter
import com.landable.app.ui.home.categories.CategoriesAdapter
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.postProjectProperty.filterAdapters.MonthsDropdownAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.PropertyTypeAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.UnitDropdownAdapter
import com.landable.app.ui.home.profile.CityAdapter
import com.landable.app.ui.home.profile.StateAdapter
import com.landable.app.ui.home.property.adapters.AdvertisementAdapter
import com.landable.app.ui.home.search.SearchAuctionAdapter

class FragmentAuction : Fragment(),
    AuctionDetailClickListener, PropertyTypeClickListener,
    CategoryTypeClickListener, AdvertisementClickListener/*, DatePickerDialog.OnDateSetListener*/ {

    private lateinit var binding: FragmentAuctionsBinding
    private var searchData: SearchAuctionDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var categoryList = ArrayList<CategoriesDataModel>()
    private var unitId: Int = 0
    private var categoryID: Int = 0
    private var subCategoryID: Int = 0
    private var filterData: FilterMasterDataModel? = null
    private var stateList = ArrayList<Statemaster>()
    private var cityArrayAdapter: CityAdapter? = null
    private var stateId: Int = 0
    private var cityId: Int = 0
    private var borrowerName: String = ""
    private var priceRange = ArrayList<MonthsDataModel>()
    private var statusArray = ArrayList<MonthsDataModel>()
    private var status: String = "A"
    private var bankName: String = ""
    private var advertismentsList = ArrayList<Advertisment>()
    private var price: Int = 2

    private var costToPrice: Int = 0
    private var costFromPrice: Int = 0

    private var areaTo: Int = 0
    private var areaFrom: Int = 0
    private var searchId: Int = 0
    private var searchDescription: String = ""

    companion object {
        fun newInstance() = FragmentAuction()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Auctions")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_auctions, container, false)

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Auction Search Fragment", null);

        getFiltersList()
        updatePriceUnitDopDown()
        updateStatusUnitDopDown()

        binding.layoutFilter.status.setText("Active")
        binding.ivSaveResult.setOnClickListener {
            if (AppInfo.getUserId() == "0" || AppInfo.getSCode() == "") {
                (activity as HomeActivity).askForLogin()
            } else {
                saveSearchFunction(SaveSearch(searchDescription, searchId, 0, "Auction"))
            }
        }
        binding.filterIcon.setOnClickListener {
            binding.tvNoResult.visibility = View.GONE
            binding.llFilter.visibility = View.VISIBLE
            binding.filterIcon.visibility = View.GONE
            binding.rvAuctions.visibility = View.GONE
            binding.rvAdvertisements.visibility = View.GONE

        }
        binding.layoutFilter.buttonClearFilter.setOnClickListener {
            clearFilter()
            getFiltersList()
            updatePriceUnitDopDown()
            updateStatusUnitDopDown()
            binding.layoutFilter.rvPropertyType.visibility = View.GONE
        }


        callDefaultApi()

        binding.ivSearchMap.setOnClickListener {
            var url = "https://www.landable.in/auctionmap.aspx?key=&ct=0&st=0&" +
                    "city=,status=Active,locality=,locality=,beforedate=,bankname=," +
                    "borrower=,costfrom=0,costto=0,areafrom=0,areato=300000,emddate="
            (activity as HomeActivity).callBrowserActivity(url,"Search Map")
        }

        binding.layoutFilter.buttonSearch.setOnClickListener {
            callAPI()
        }

        binding.ivSearch.setOnClickListener {
            callAPI()
        }


        (binding.root.findViewById<RangeSlider>(R.id.priceRangeSeekbar)).addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    //Log.d("cost", slider.values[0].toString())
                    if (price == 2) {
                        CustomAlertDialog(
                            requireContext(),
                            "Alert",
                            "Please select unit first"
                        ).show()
                    }
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    Log.d("cost To", slider.values[1].toString())
                    Log.d("cost From", slider.values[0].toString())

                    costToPrice = slider.values[1].toInt()
                    costFromPrice = slider.values[0].toInt()
                    if (costToPrice < 0) {
                        costToPrice = 0
                        costFromPrice = 0
                    }
                }
            })

        (binding.root.findViewById<RangeSlider>(R.id.areaRangeSeekbar)).addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    //Log.d("area", slider.values[0].toString())
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    Log.d("area To", slider.values[1].toString())
                    Log.d("area From", slider.values[1].toString())

                    areaTo = slider.values[1].toInt()
                    areaFrom = slider.values[0].toInt()
                }
            })

        return binding.root
    }

    private fun clearFilter() {
        categoryList.clear()
        unitId = 0
        categoryID = 0
        subCategoryID = 0
        stateList.clear()
        stateId = 0
        cityId = 0
        borrowerName = ""
        priceRange.clear()
        statusArray.clear()
        bankName = ""
        price = 2
        costToPrice = 0
        costFromPrice = 0
        areaTo = 0
        areaFrom = 0
        binding.layoutFilter.autoCompleteTextViewForPriceValue.setText("Select Unit")

    }

    private fun costFrom(selectedUnit: Int): Int {
        if (selectedUnit == 0) {
            costFromPrice *= 100000
        } else if (selectedUnit == 1) {
            costFromPrice *= 10000000
        } else {
            costFromPrice = 0
        }
        return costFromPrice
    }

    private fun costTo(selectedUnit: Int): Int {
        if (selectedUnit == 0) {
            costToPrice *= 100000
        } else if (selectedUnit == 1) {
            costToPrice *= 10000000
        } else {
            costToPrice = 0
        }
        return costToPrice
    }

    private fun callDefaultApi() {
        getAuctionSearch(
            SearchAuctionRequestDataModel(
                "",
                "",
                "",
                "",
                "", "",
                0,
                0, 0,
                0,
                "",
                status,
                "",
                "",
                1
            )
        )

    }

    private fun callAPI() {
        binding.llFilter.visibility = View.GONE
        binding.filterIcon.visibility = View.VISIBLE
        binding.tvNoResult.visibility = View.GONE
        binding.rvAuctions.visibility = View.VISIBLE
        binding.rvAdvertisements.visibility = View.GONE
        searchData!!.Auction.clear()
        getAuctionSearch(
            SearchAuctionRequestDataModel(
                binding.editText.text.toString(),
                categoryID.toString(),
                subCategoryID.toString(),
                "",
                bankName, borrowerName,
                costFrom(price),
                costTo(price), areaFrom,
                areaTo,
                "",
                status,
                stateId.toString(),
                cityId.toString(),
                1
            )
        )
    }


    private fun getFiltersList() {
        // Show progress bar
        val filterResponse = RegisterRepository().getFilterMaster()
        filterResponse.observe(viewLifecycleOwner) {
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    filterData = ParseResponse.parseAuctionFilterMasterResponse(it.toString())
                    updateDashboardInfo()
                    updateUnitDropdown()
                    updateBankDropdown()
                    updateBorrowerDropdown()
                    stateList = filterData!!.statemaster
                    updateStateCityDropDown()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateStateCityDropDown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = StateAdapter(requireActivity(), stateList)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.autoCompleteTextViewForState.setAdapter(arrayAdapter)

        binding.layoutFilter.autoCompleteTextViewForState.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                stateId = stateList[pos].id
                updateCityList(stateId)
            }
    }


    private fun updateStatusUnitDopDown() {
        statusArray.add(MonthsDataModel(1, "Active"))
        statusArray.add(MonthsDataModel(2, "Inactive"))
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val statusAdapter = MonthsDropdownAdapter(requireActivity(), statusArray)
        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.status.setAdapter(statusAdapter)
        binding.layoutFilter.status.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                //unitId = filterData!!.Unitmaster[pos].id
                if (pos == 0) {
                    status = "A"
                } else status = "N"
            }
    }

    private fun updatePriceUnitDopDown() {
        priceRange.add(MonthsDataModel(1, "Lakh"))
        priceRange.add(MonthsDataModel(2, "Crore"))
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val monthsAdapter = MonthsDropdownAdapter(requireActivity(), priceRange)
        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.autoCompleteTextViewForPriceValue.setAdapter(monthsAdapter)
        binding.layoutFilter.autoCompleteTextViewForPriceValue.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                //unitId = filterData!!.Unitmaster[pos].id
                price = pos
            }
    }

    private fun updateCityList(stateId: Int) {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        if (cityArrayAdapter == null) {
            cityArrayAdapter = CityAdapter(requireActivity(), filterData!!.cityHashMap[stateId])
        } else {
            cityArrayAdapter!!.updateList(filterData!!.cityHashMap[stateId])
        }
        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.autoCompleteTextViewForCity.setAdapter(cityArrayAdapter)

        binding.layoutFilter.autoCompleteTextViewForCity.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                cityId = (filterData!!.cityHashMap[stateId] as ArrayList<Citymaster>)[pos].id
            }
    }

    private fun updateUnitDropdown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = UnitDropdownAdapter(requireActivity(), filterData!!.Unitmaster)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.autoCompleteTextViewForUnit.setAdapter(arrayAdapter)

        binding.layoutFilter.autoCompleteTextViewForUnit.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                unitId = filterData!!.Unitmaster[pos].id

            }
    }

    private fun updateBankDropdown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = BankNameAdapter(requireActivity(), filterData!!.banknamemaster)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.autoCompleteTextViewForBankName.setAdapter(arrayAdapter)

        binding.layoutFilter.autoCompleteTextViewForBankName.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                bankName = filterData!!.banknamemaster[pos].bankname
            }
    }

    private fun updateBorrowerDropdown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = BorrowerNameAdapter(requireActivity(), filterData!!.borrowernamemaster)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.layoutFilter.autoCompleteTextViewForBorrowerName.setAdapter(arrayAdapter)

        binding.layoutFilter.autoCompleteTextViewForBorrowerName.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                borrowerName = filterData!!.borrowernamemaster[pos].borrowername
            }
    }


    private fun updateDashboardInfo() {
        categoryList = filterData!!.categorymaster
        binding.layoutFilter.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvCategory.adapter = CategoriesAdapter(categoryList, this,categoryID)

    }

    private fun updatePropertyTypeList(categoryType: String) {
        binding.layoutFilter.rvPropertyType.visibility = View.VISIBLE
        when (categoryType) {
            "Residential" -> {
                binding.layoutFilter.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.layoutFilter.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.residentialTypeLinkedHashMap[categoryType]!!,
                        this,subCategoryID
                    )
            }
            "Commercial" -> {
                binding.layoutFilter.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.layoutFilter.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.commercialTypeLinkedList[categoryType]!!,
                        this,subCategoryID
                    )
            }
            "Agricultural" -> {
                // set adapter to the autocomplete tv to the arrayAdapter
                binding.layoutFilter.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.layoutFilter.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.agriculturalTypeLinkedList[categoryType]!!,
                        this,subCategoryID
                    )
            }
        }
    }

    override fun onCategoryClick(action: String, categoryDataModel: CategoriesDataModel?) {
        when (action) {
            "categoryClick" -> {
                categoryID = categoryDataModel!!.id
                updatePropertyTypeList(categoryDataModel.codevalue)
            }
            "removeCategoryClicked" -> {
                categoryID = 0
                binding.layoutFilter.rvPropertyType.visibility = View.GONE
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

    /* private fun updateDateMonthYear() {
         val datePickerDialog = DatePickerDialog(
             requireContext(),
             android.R.style.Theme_Holo_Dialog, this,  (binding.tvYear.text.toString()).toInt(), (binding.tvMonth.text.toString()).toInt(), (binding.tvDate.text.toString()).toInt()
         )
         datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
         datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         datePickerDialog.setTitle("Select Date")
         datePickerDialog.show()
     }

     override fun onDateSet(p0: DatePicker?, mYear: Int, mMonth: Int, mDate: Int) {
         binding.tvDate.text = "$mDate"
         binding.tvMonth.text = "${mMonth + 1}"
         binding.tvYear.text = "$mYear"
     }*/

    private fun getAuctionSearch(dataModel: SearchAuctionRequestDataModel) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val searchResponse = RegisterRepository().getAuctionSearch(dataModel)
        searchResponse.observe(viewLifecycleOwner) {
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
                    searchData = ParseResponse.parseAuctionSearchResponse(it.toString())
                    advertismentsList = searchData!!.advertisment
                    searchId = searchData!!.searchid
                    searchDescription = searchData!!.searchdescription
                    updateUi()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUi() {
        if (searchData!!.Auction.size == 0) {
            binding.tvNoResult.visibility = View.VISIBLE
            binding.rvAuctions.visibility = View.GONE
            binding.rvAdvertisements.visibility = View.VISIBLE

            binding.rvAdvertisements.layoutManager = GridLayoutManager(requireContext(), 3)
            binding.rvAdvertisements.adapter = AdvertisementAdapter(advertismentsList, this)
        } else {
            binding.rvAuctions.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvAuctions.adapter = SearchAuctionAdapter(searchData!!.Auction, this)
        }
    }

    override fun onAuctionClick(
        action: String,
        auctionSearchInfoModelDataModel: AuctionSearchInfoModel
    ) {
        when (action) {
            "selectedAuctionDetail" -> {
                loadAuctionDetail(auctionSearchInfoModelDataModel)
            }
        }
    }

    private fun loadAuctionDetail(auctionSearchInfoModelDataModel: AuctionSearchInfoModel) {
        val bundle = Bundle()
        bundle.putSerializable("auctionSearchInfoModelDataModel", auctionSearchInfoModelDataModel)
        val auctionDetailFragment = AuctionDetailPageFragment.newInstance()
        auctionDetailFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            auctionDetailFragment,
            AuctionDetailPageFragment::class.java.name
        )
    }


    class SearchAuctionRequestDataModel(
        locality: String,
        category: String,
        subcategory: String,
        beforedate: String,
        bankname: String,
        borrower: String,
        costfrom: Int,
        costto: Int,
        areafrom: Int,
        areato: Int,
        emddate: String,
        status: String,
        state: String,
        city: String,
        pagecount: Int
    ) {
        private val locality: String = locality
        private val category: String = category
        private val subcategory: String = subcategory
        private val beforedate: String = beforedate
        private val bankname: String = bankname
        private val borrower: String = borrower
        private val costfrom: Int = costfrom
        private val costto: Int = costto
        private val areafrom: Int = areafrom
        private val areato: Int = areato
        private val emddate: String = emddate
        private val status: String = status
        private val state: String = state
        private val city: String = city
        private val pagecount: Int = pagecount

    }

    private fun saveSearchFunction(dataModel: SaveSearch) {
        val savesearchResponse =
            RegisterRepository().post_Addsavedsearch(dataModel)
        savesearchResponse.observe(viewLifecycleOwner) {

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
                        binding.ivSaveResult.visibility = View.GONE
                        binding.unsave.visibility = View.VISIBLE

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    class SaveSearch(
        searchname: String,
        searchid: Int,
        searchresult: Int,
        searchtype: String
    ) {
        private var searchname: String = searchname
        private var searchid: Int = searchid
        private var searchresult: Int = searchresult
        private var searchtype: String = searchtype
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


}
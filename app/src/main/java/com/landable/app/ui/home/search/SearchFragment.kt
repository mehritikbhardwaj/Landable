package com.landable.app.ui.home.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentSearchBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.agent.ContactOwnerDialogFragment
import com.landable.app.ui.home.auction.FragmentAuction
import com.landable.app.ui.home.categories.CategoriesAdapter
import com.landable.app.ui.home.chats.ChatsFragment
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.postProjectProperty.filterAdapters.*
import com.landable.app.ui.home.project.ProjectDetailFragment
import com.landable.app.ui.home.property.PropertyDetailFragment


class SearchFragment : Fragment(), CategoryTypeClickListener, PropertyTypeClickListener,
    AgentProfileListener, PropertyDetailListener, ProjectDetailListener, FloorClickListener,
    PostedDateClickListener,
    AmenitiesClickListener, SelectedFilterSearch {

    private lateinit var binding: FragmentSearchBinding
    private var selectedHighlightedText: String = "Property"
    private var filterData: FilterMasterDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var categoryList = ArrayList<CategoriesDataModel>()
    private var categoryID: Int = 0
    private var subCategoryID: Int = 0
    private var saleType: Int = 0
    private var possetionType: Int = 0
    private var furnishedType: Int = 0
    private var bedroomCount: Int = 0
    private var bathroomCount: Int = 0
    private var balconyCount: Int = 0
    private var parkingCount: Int = 0
    private var unitId: Int = 0
    private var floorValue: String = ""
    private var searchPropertyData: SearchPropertyDataModel? = null
    private var searchProjectData: SearchProjectDataModel? = null
    private var searchAgencyData: SearchAgencyDataModel? = null
    private var postedSince = ArrayList<BedsDataModel>()
    private var priceRange = ArrayList<MonthsDataModel>()
    private var postedDate: String = ""
    private var price: Int = 2
    private var amenitiesArray = ArrayList<Amenitiesmaster>()

    private var costToPrice: Int = 0
    private var costFromPrice: Int = 0

    private var areaTo: Int = 0
    private var areaFrom: Int = 0
    private var searchId: Int = 0
    private var searchDescription: String = ""


    var selectedFiltersList = ArrayList<String>()
    private var selectedFiltersAdapter: SelectedFiltersAdapter? = null


    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search, container,
                false
            )

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Search")
        (activity as HomeActivity).hideBottomNavigation()


        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        changeSelectedTextColourAndCallAPi(selectedHighlightedText)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Search page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        binding.ivSearchMap.setOnClickListener {

            updateSelectedFilterRecycler()
            val url = "https://www.landable.in/property-list.aspx?city=37" +
                    "&key=&ct=&st=&stype=0&ps=0&cfrom=0&cto=0&bed=&bath=0&bal=0&furnished=&parking" +
                    "=0&amenities=&postedon=0&floor=&lat=0&lon=0&sort=New&areafrom=0&areato=100000#."

            (activity as HomeActivity).callBrowserActivity(url,"Search Map Page")
        }

        binding.ivSaveResult.setOnClickListener {
            if (AppInfo.getUserId() == "0" || AppInfo.getSCode() == "") {
                (activity as HomeActivity).askForLogin()
            } else {
                saveSearchFunction(
                    FragmentAuction.SaveSearch(
                        searchDescription,
                        searchId,
                        0,
                        selectedHighlightedText
                    )
                )
                CustomAlertDialog(requireContext(),
                    "Message","You will be notified when there are new results for the search tags").show()
            }
        }


        binding.layoutFilter.buttonClearFilter.setOnClickListener {
            selectedFiltersList.clear()
         //   updateSelectedFilterRecycler()
            clearFilter()
            getFilterInfo()
            updatePriceUnitDopDown()
            addPostedSinceDataToList()
            binding.layoutFilter.rvPropertyType.visibility = View.GONE
        }

        binding.ivSearch.setOnClickListener {
            binding.llFilter.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.topSearchTypes.visibility = View.VISIBLE
            binding.filterIcon.visibility = View.VISIBLE
                changeSelectedTextColourAndCallAPi(selectedHighlightedText)
        }
        addPostedSinceDataToList()
        //change seleted top option given for search like properties, projects, auction, agencies        

        getFilterInfo()
        updatePriceUnitDopDown()

        binding.filterIcon.setOnClickListener {
            binding.recyclerView.visibility = View.GONE
            binding.llFilter.visibility = View.VISIBLE
            binding.filterIcon.visibility = View.GONE
            binding.topSearchTypes.visibility = View.GONE
        }

        binding.layoutFilter.buttonSearch.setOnClickListener {
           // updateSelectedFilterRecycler()
            binding.llFilter.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.topSearchTypes.visibility = View.VISIBLE
            binding.filterIcon.visibility = View.VISIBLE
            if (!binding.editText.text.isNullOrEmpty()) {
                search(selectedHighlightedText)
            } else toast()
        }

        binding.tvProperty.setOnClickListener { changeSelectedTextColourAndCallAPi("Property") }
        binding.tvProjects.setOnClickListener { changeSelectedTextColourAndCallAPi("Project") }
        binding.tvAgency.setOnClickListener { changeSelectedTextColourAndCallAPi("Agency") }

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

    private fun updateSelectedFilterRecycler() {
        binding.rvSelectedFilters.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedFilters.itemAnimator = DefaultItemAnimator()
        selectedFiltersAdapter = SelectedFiltersAdapter(activity, selectedFiltersList, this)
        binding.rvSelectedFilters.adapter = selectedFiltersAdapter
    }

    private fun clearFilter() {
        categoryID = 0
        subCategoryID = 0
        saleType = 0
        possetionType = 0
        furnishedType = 0
        bedroomCount = 0
        bathroomCount = 0
        balconyCount = 0
        parkingCount = 0
        unitId = 0
        floorValue = ""
        postedSince.clear()
        priceRange.clear()
        postedDate = ""
        price = 2
        amenitiesArray.clear()
        costToPrice = 0
        costFromPrice = 0
        areaTo = 0
        areaFrom = 0
        binding.layoutFilter.autoCompleteTextViewForPriceValue.setText("Select Unit")

    }

    private fun saveSearchFunction(dataModel: FragmentAuction.SaveSearch) {
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

    private fun addPostedSinceDataToList() {
        postedSince.add(BedsDataModel(1, "All"))
        postedSince.add(BedsDataModel(2, "Yesterday"))
        postedSince.add(BedsDataModel(3, "Last Week"))
        postedSince.add(BedsDataModel(4, "Last 2 Weeks"))
        postedSince.add(BedsDataModel(5, "Last 3 Weeks"))
        postedSince.add(BedsDataModel(6, "Last Month"))
        postedSince.add(BedsDataModel(7, "Last 2 Month"))
        postedSince.add(BedsDataModel(8, "Last 4 Month"))
        binding.layoutFilter.rvPostedSince.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvPostedSince.adapter =
            PostedSinceAdapter(postedSince, this)
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


    //function to change text color and hit api on the basis of text selected
    private fun changeSelectedTextColourAndCallAPi(selectedText: String) {
        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity),
            "Search $selectedText Fragment", null);

        selectedHighlightedText = selectedText
        binding.tvProperty.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.tvProjects.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        binding.tvAgency.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        when (selectedText) {
            "Property" -> {
                binding.tvProperty.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colour_app
                    )
                )
                binding.recyclerView.visibility = View.GONE
                    search(selectedText)
            }
            "Project" -> {
                binding.tvProjects.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colour_app
                    )
                )
                binding.recyclerView.visibility = View.GONE
                    search(selectedText)
            }
            "Agency" -> {
                binding.tvAgency.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colour_app
                    )
                )
                    search(selectedText)
            }
        }
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

    private fun search(type: String) {
        when (type) {
            "Property" -> {
                getPropertySearch(
                    SearchRequestDataModel(
                        binding.editText.text.toString(), categoryID.toString(),
                        subCategoryID.toString(), saleType.toString(),
                        possetionType.toString(), bedroomCount.toString(),
                        bathroomCount.toString(), balconyCount.toString(),
                        furnishedType.toString(), parkingCount.toString(),
                        getIdsWithString(), postedDate,
                        floorValue, "",
                        "", costFrom(price),
                        costTo(price), areaFrom,
                        areaTo, 1,
                        0.0, 0.0
                    )
                )
            }
            "Project" -> {
                getProjectSearch(
                    SearchRequestDataModel(
                        binding.editText.text.toString(), categoryID.toString(),
                        subCategoryID.toString(), saleType.toString(),
                        possetionType.toString(), bedroomCount.toString(),
                        bathroomCount.toString(), balconyCount.toString(),
                        furnishedType.toString(), parkingCount.toString(),
                        getIdsWithString(), postedDate,
                        floorValue, "",
                        "", costFrom(price),
                        costTo(price), areaFrom,
                        areaTo, 1,
                        0.0, 0.0
                    )
                )
            }
            "Agency" -> {
                getAgencySearch(
                    SearchRequestDataModel(
                        binding.editText.text.toString(), categoryID.toString(),
                        subCategoryID.toString(), saleType.toString(),
                        possetionType.toString(), bedroomCount.toString(),
                        bathroomCount.toString(), balconyCount.toString(),
                        furnishedType.toString(), parkingCount.toString(),
                        getIdsWithString(), postedDate,
                        floorValue, "",
                        "", costFrom(price),
                        costTo(price), areaFrom,
                        areaTo, 1,
                        0.0, 0.0
                    )
                )
            }
        }
    }

    //function for get filter data
    private fun getFilterInfo() {
        // Show progress bar
        /*  progressDialog = CustomProgressDialog(requireContext())
          progressDialog!!.show()*/
        val filterResponse = RegisterRepository().getFilterMaster()
        filterResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            // progressDialog!!.cancelProgress()

            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    filterData =
                        ParseResponse.parseFilterMasterResponse(it.toString())

                    categoryList = filterData!!.categorymaster
                    updateFilterUI()
                    updateUnitDropdown()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    //funtion to update dropdown for built area unit and get response after clicking
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


    private fun updateFilterUI() {
        binding.layoutFilter.rvAmenities.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.layoutFilter.rvAmenities.adapter =
            AmenitiesAdapter(filterData!!.Amenitiesmaster, this)

        binding.layoutFilter.rvFloor.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvFloor.adapter =
            FloorAdapter(filterData!!.floormaster, this)


        binding.layoutFilter.rvBHK.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvBHK.adapter =
            BedBathBalParAdapter(filterData!!.bedroom, this, "bedroom")

        binding.layoutFilter.rvBathroom.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvBathroom.adapter =
            BedBathBalParAdapter(filterData!!.bathroom, this, "bathroom")

        binding.layoutFilter.rvBalcony.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvBalcony.adapter =
            BedBathBalParAdapter(filterData!!.balconey, this, "balcony")

        binding.layoutFilter.rvParking.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvParking.adapter =
            BedBathBalParAdapter(filterData!!.parking, this, "parking")

        //binding recycler view for furnished type filter
        binding.layoutFilter.rvFurnishedType.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvFurnishedType.adapter =
            SaletypeAdapter(filterData!!.furnishedmaster, this, "furnishedType", furnishedType)

        //binding recycler view for category type filter
        binding.layoutFilter.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvCategory.adapter = CategoriesAdapter(categoryList, this, categoryID)

        //binding recycler view for sale type filter
        binding.layoutFilter.rvSaleType.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvSaleType.adapter =
            SaletypeAdapter(filterData!!.saletypemaster, this, "saleTypeClick", saleType)

        //binding recycler view for possession type filter
        binding.layoutFilter.rvPossessionStatus.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.layoutFilter.rvPossessionStatus.adapter =
            SaletypeAdapter(filterData!!.possessionmaster, this, "possessionCLick", possetionType)
    }

    //DAta model for search
    class SearchRequestDataModel(
        keyword: String,
        category: String,
        subcategory: String,
        salestype: String,
        possession: String,
        bed: String,
        bathroom: String,
        balconey: String,
        furnished: String,
        parking: String,
        amenities: String,
        postedbetweendays: String,
        floor: String,
        sort: String,
        state: String,
        costfrom: Int,
        costto: Int,
        areafrom: Int,
        areato: Int,
        pagecount: Int,
        lat: Double,
        lon: Double

    ) {
        private val keyword: String = keyword
        private val category: String = category
        private val subcategory: String = subcategory
        private val salestype: String = salestype
        private val possession: String = possession
        private val bed: String = bed
        private val bathroom: String = bathroom
        private val balconey: String = balconey
        private val furnished: String = furnished
        private val parking: String = parking
        private val amenities: String = amenities
        private val postedbetweendays: String = postedbetweendays
        private val floor: String = floor
        private val sort: String = sort
        private val state: String = state
        private val costfrom: Int = costfrom
        private val costto: Int = costto
        private val areafrom: Int = areafrom
        private val areato: Int = areato
        private val pagecount: Int = pagecount
        private val lat: Double = lat
        private val lon: Double = lon
    }

    //function for updating property type list on the basis of category selected in filter
    private fun updatePropertyTypeList(categoryType: String) {
        binding.layoutFilter.rvPropertyType.visibility = View.VISIBLE
        when (categoryType) {
            "Residential" -> {
                subCategoryID = filterData!!.residentialTypeLinkedHashMap[categoryType]!![0].id
                binding.layoutFilter.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.layoutFilter.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.residentialTypeLinkedHashMap[categoryType]!!,
                        this, subCategoryID
                    )
            }
            "Commercial" -> {
                binding.layoutFilter.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.layoutFilter.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.commercialTypeLinkedList[categoryType]!!,
                        this, subCategoryID
                    )
            }
            "Agricultural" -> {
                binding.layoutFilter.rvPropertyType.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.layoutFilter.rvPropertyType.adapter =
                    PropertyTypeAdapter(
                        filterData!!.agriculturalTypeLinkedList[categoryType]!!,
                        this, subCategoryID
                    )
            }
        }
    }

    //this function will be used to call funtion to change property type list in filter
    override fun onCategoryClick(action: String, categoryDataModel: CategoriesDataModel?) {
        when (action) {
            "categoryClick" -> {
                categoryID = categoryDataModel!!.id
                updatePropertyTypeList(categoryDataModel.codevalue)
                selectedFiltersList.add(categoryDataModel.codevalue)

            }
            "removeCategoryClicked" -> {
                categoryID = 0
                binding.layoutFilter.rvPropertyType.visibility = View.GONE
                selectedFiltersList.remove(categoryDataModel!!.codevalue)

            }
        }
    }

    //this function is for the type selected in filter
    override fun onTypeClick(action: String, propertyDataModel: PropertyTypeDataModel?) {
        when (action) {
            "typeClick" -> {
                subCategoryID = propertyDataModel!!.id
                selectedFiltersList.add(propertyDataModel.codevalue)

            }
            "deleteTypeClick" -> {
                subCategoryID = 0
                selectedFiltersList.remove(propertyDataModel!!.codevalue)

            }

        }
    }

    //this function is for getting selected data of sale type click and possesion type click
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
            "deleteFurnishedType" -> {
                possetionType = 0
            }
            "agencyID" -> {
                loadAgentProfileFragment(id)
            }
            "chatClicked" -> {
                loadChatsFragment("Agency", AppInfo.getUserId().toInt(), id)
            }
            "furnishedType" -> {
                furnishedType = id
            }
            "bedroom" -> {
                bedroomCount = id
            }
            "bathroom" -> {
                bathroomCount = id
            }
            "balcony" -> {
                balconyCount = id
            }
            "parking" -> {
                parkingCount = id
            }

            "Nobedroom" -> {
                bedroomCount = 0
            }
            "Nobathroom" -> {
                bathroomCount = 0
            }
            "Nobalcony" -> {
                balconyCount = 0
            }
            "Noparking" -> {
                parkingCount = 0
            }
        }
    }


    private fun getAgencySearch(dataModel: SearchRequestDataModel) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val searchResponse = RegisterRepository().getAgencySearch(dataModel)
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
                    searchAgencyData = ParseResponse.parseAgencySearchResponse(it.toString())
                    searchDescription = searchAgencyData!!.searchdescription
                    updateAgencyUi()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadAgentProfileFragment(id: Int) {
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

    private fun loadChatsFragment(type: String, id: Int, toUserID: Int) {
        val bundle = Bundle()
        bundle.putString("type", type)
        bundle.putInt("id", id)
        bundle.putInt("toUserID", toUserID)
        bundle.putBoolean("comingfromchat", false)

        val chatsFragment = ChatsFragment.newInstance()
        chatsFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            chatsFragment,
            ChatsFragment::class.java.name
        )
    }


    //function to get data for project Search
    private fun getProjectSearch(dataModel: SearchRequestDataModel) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val searchResponse = RegisterRepository().getProjectSearch(dataModel)
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
                    searchProjectData = ParseResponse.parseProjectSearchResponse(it.toString())
                    searchDescription = searchProjectData!!.searchdescription

                    updateProjectUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadProjectDetailFragment(
        projectsDataModel: ProjectsDataModel?,
    ) {
        val bundle = Bundle()
        bundle.putSerializable("projectDetailModel", projectsDataModel)
        val propertyDetailFragment = ProjectDetailFragment.newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            ProjectDetailFragment::class.java.name
        )
    }

    override fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?) {
        when (action) {
            "selectedProjectDetail" -> {
                loadProjectDetailFragment(projectDataModel)
            }
        }
    }

    //function to get data for propertySearch
    private fun getPropertySearch(dataModel: SearchRequestDataModel) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val searchResponse = RegisterRepository().getPropertySearch(dataModel)
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
                    searchPropertyData = ParseResponse.parsePropertySearchResponse(it.toString())
                    searchDescription = searchPropertyData!!.searchdescription
                    updatePropertiesUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onPropertyClick(
        action: String,
        featurePropertiesDataModel: FeaturePropertiesDataModel?
    ) {
        when (action) {
            "selectedPropertyDetail" -> {
                loadPropertyDetailFragment(featurePropertiesDataModel)
            }
            "contactOwner" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                    (activity as HomeActivity).askForLogin()
                    contactOwner(featurePropertiesDataModel!!)
                } else {
                    val fm = requireActivity().supportFragmentManager
                    val dialogFragment = ContactOwnerDialogFragment(
                        "",
                        featurePropertiesDataModel!!.name,
                        "", featurePropertiesDataModel.addedbyid

                    )
                    dialogFragment.show(fm, "")
                }
            }
            "ViewAgencyProfile" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                    (activity as HomeActivity).askForLogin()
                    contactOwner(featurePropertiesDataModel!!)
                } else {
                    loadAgentProfileFragment(featurePropertiesDataModel!!.addedbyid)
                }
            }
        }
    }

    private fun contactOwner(propertyDetailInfoModel: FeaturePropertiesDataModel) {
        (activity as HomeActivity).propertyID = propertyDetailInfoModel.propertyid
        (activity as HomeActivity).contactType = "Property"
        (activity as HomeActivity).agentID = propertyDetailInfoModel.addedbyid
    }

    private fun loadPropertyDetailFragment(propertyDataModel: FeaturePropertiesDataModel?) {
        val bundle = Bundle()
        bundle.putSerializable("propertiesDetailModel", propertyDataModel)
        val propertyDetailFragment = PropertyDetailFragment.newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            PropertyDetailFragment::class.java.name
        )
    }

    private fun toast() {
        Toast.makeText(
            requireContext(),
            "Please type something to search!!",
            Toast.LENGTH_LONG
        ).show()
    }

    //function to udpate recycler view data
    private fun updatePropertiesUI() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = SearchPropertyAdapter(searchPropertyData!!.Properties, this)
    }

    private fun updateProjectUI() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter =
            SearchProjectAdapter(searchProjectData!!.projects, this, requireContext())
    }

    private fun updateAgencyUi() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = SearchAgencyAdapter(searchAgencyData!!.userProfiles, this)
    }


    override fun onFloorClick(action: String, floormaster: Floormaster) {
        when (action) {
            "floor" -> {
                floorValue = floormaster.floorvalue
            }
            "Nofloor" -> floorValue = ""
        }
    }

    override fun onPostedDateClick(action: String, posted: String) {
        when (action) {
            "postedDate" -> {
                postedDate = posted
            }
            "removepostedDate" -> {
                postedDate = ""
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

    override fun onFilterSelected(action: String, name: String) {
        when(action){

        }
    }

}
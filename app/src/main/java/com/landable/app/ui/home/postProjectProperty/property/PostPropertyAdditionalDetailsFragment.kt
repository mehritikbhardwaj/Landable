package com.landable.app.ui.home.postProjectProperty.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.landable.app.R
import com.landable.app.common.AmenitiesClickListener
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.PropertyTypeClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostPropertyAdditionalBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomConfirmationDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.postProjectProperty.filterAdapters.AmenitiesAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.BathroomDropdownAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.FurnishedDropdownAdapter
import com.landable.app.ui.home.postProjectProperty.filterAdapters.MonthsDropdownAdapter
import org.json.JSONObject

class PostPropertyAdditionalDetailsFragment : Fragment(), PropertyTypeClickListener,
    AmenitiesClickListener, CustomConfirmationDialog.ICustomConfirmationDialogListener {

    private lateinit var binding: FragmentPostPropertyAdditionalBinding
    private var propertyId: String = ""
    private var _id: Int = 0
    private var progressDialog: CustomProgressDialog? = null
    private var filterData: FilterMasterDataModel? = null
    private var bathromCount: Int = 0
    private var balconyCount: Int = 0
    private var parkingCount: Int = 0
    private var furnishedType: Int = 0
    private var openSidesCount: Int = 0
    private var amenitiesArray = ArrayList<Amenitiesmaster>()
    private var isComingForEdit: Boolean = false
    private var propertyData: PropertyRawDataModel? = null
    private var yesNoArray = ArrayList<MonthsDataModel>()
    private var corneredArray = ArrayList<MonthsDataModel>()
    private var isGatedColony: String = "YES"
    private var cornered: String = "YES"

    companion object {
        fun newInstance() = PostPropertyAdditionalDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        propertyId = requireArguments().getString("propertyid")!!
        _id = requireArguments().getInt("id")
        isComingForEdit = requireArguments().getBoolean("isComingForEdit")
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
                R.layout.fragment_post_property_additional,
                container,
                false
            )

        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.cancelProgress()

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Post property additional page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        getFilterInfo()
        if (isComingForEdit) {
            getPropertyDetails(_id)
        }
        updateIsCertifiedDropDown()
        updateIsCornerdDropDown()

        binding.buttonPost.setOnClickListener {
            if (binding.edMaintenanceCharge.text.toString().isNullOrEmpty() ||
                binding.securityDeposit.text.toString()
                    .isNullOrEmpty() || binding.edDepositPErcentage.text.toString().isNullOrEmpty()
                || binding.poolSize.text.toString()
                    .isNullOrEmpty() || binding.edAdditionalRooms.text.toString().isNullOrEmpty()
                || binding.edRemodelYear.text.toString().isNullOrEmpty()
            ) {
                CustomAlertDialog(requireContext(), "Alert", "Please fill all the columns.").show()
            } else {

                val maintenancecharge =
                    binding.edMaintenanceCharge.text.toString().toDouble().toInt()
                val securityDeposit = binding.securityDeposit.text.toString().toDouble().toInt()
                val attachedbathroom = binding.attachedBathroom.text.toString().toInt()

                postPropertyAdditionalDetailsUpdate(
                    PostPropertyAdditionalInfo(
                        _id,
                        propertyId,
                        maintenancecharge,
                        securityDeposit,
                        bathromCount,
                        attachedbathroom,
                        balconyCount,
                        parkingCount,
                        cornered,
                        0,
                        openSidesCount,
                        binding.construction.text.toString(),
                        "",
                        "",
                        isGatedColony,
                        furnishedType,
                        binding.edDepositPErcentage.text.toString(),
                        binding.poolSize.text.toString(),
                        binding.edAdditionalRooms.text.toString(),
                        binding.edRemodelYear.text.toString(),
                        getIdsWithString()
                    )
                )
            }

        }

        return binding.root
    }

    private fun updateIsCertifiedDropDown() {

        yesNoArray.add(MonthsDataModel(1, "Yes"))
        yesNoArray.add(MonthsDataModel(2, "No"))

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val monthsAdapter = MonthsDropdownAdapter(requireActivity(), yesNoArray)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.isGatedCOlony.setAdapter(monthsAdapter)
        binding.isGatedCOlony.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                if (pos == 0) {
                    isGatedColony == "YES"
                } else isGatedColony == "NO"
            }
    }

    private fun updateIsCornerdDropDown() {

        corneredArray.add(MonthsDataModel(1, "Yes"))
        corneredArray.add(MonthsDataModel(2, "No"))

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val monthsAdapter = MonthsDropdownAdapter(requireActivity(), corneredArray)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.isCornerd.setAdapter(monthsAdapter)
        binding.isCornerd.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                //unitId = filterData!!.Unitmaster[pos].id
                if (pos == 0) {
                    cornered == "YES"
                } else cornered == "NO"
            }
    }

/*
    private fun getPropertyAdditionalDetails(id: Int, propertyid: String) {
        val propertyResponse = RegisterRepository().getPropertyDetails(id, propertyid)
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
                    propertyData = ParseResponse.parsePropertyAdditionalResponse(it.toString())
                    updateDataForEdit()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }
*/

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
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateDataForEdit() {
        binding.edMaintenanceCharge.setText(propertyData!!.propertyraw[0].maintenancecharge.toString())
        binding.securityDeposit.setText(propertyData!!.propertyraw[0].securitydeposite.toString())
        binding.attachedBathroom.setText(propertyData!!.propertyraw[0].attachedbathroom.toString())
        binding.spinnerBalconey.setText(propertyData!!.propertyraw[0].balcony.toString())
        binding.spinnerParking.setText(propertyData!!.propertyraw[0].parking.toString())
        binding.poolSize.setText(propertyData!!.propertyraw[0].poolsize)
        binding.edAdditionalRooms.setText(propertyData!!.propertyraw[0].Additionalroom)
        binding.edRemodelYear.setText(propertyData!!.propertyraw[0].lastremodalyear)
        binding.spinnerBathroom.setText(propertyData!!.propertyraw[0].bathroom.toString())
        binding.isCornerd.setText(propertyData!!.propertyraw[0].iscorner)
        binding.isGatedCOlony.setText(propertyData!!.propertyraw[0].isingatedcolony)
        binding.construction.setText(propertyData!!.propertyraw[0].anyconstruction)
        openSidesCount = propertyData!!.propertyraw[0].openside
        binding.spinnerOpenSides.setText(propertyData!!.propertyraw[0].openside.toString())
        binding.edDepositPErcentage.setText(propertyData!!.propertyraw[0].depositepercentage.toString())
        bathromCount = propertyData!!.propertyraw[0].bathroom

        balconyCount = propertyData!!.propertyraw[0].balcony
        parkingCount = propertyData!!.propertyraw[0].parking
        furnishedType = propertyData!!.propertyraw[0].furnished
        openSidesCount = propertyData!!.propertyraw[0].openside
    }


    private fun postPropertyAdditionalDetailsUpdate(dataModel: PostPropertyAdditionalInfo) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val postPropertyStep1Response =
            RegisterRepository().postPropertyAdditionalDetailsInfo(dataModel)
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
                    //{"status":"updated","msg":"Property Updated successfully!.","propertyid":"PROP19934224704","id":88743}
                    if (it.toString() != "null") {
                        val jsonObj = JSONObject(it)
                        val status = jsonObj.getString("status")
                        val msg = jsonObj.getString("msg")
                        if (status == "updated") {
                            CustomConfirmationDialog(
                                requireContext(),
                                "Alert",
                                msg,
                                "success",
                                true,
                                this
                            ).show()
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


    private fun loadHomeFragment() {
        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            HomeFragment.newInstance(),
            HomeFragment::class.java.name
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
                        ParseResponse.parseAmenitiesResponse(it.toString())

                    updateBathroomDropDown()
                    updateBalconyDropDown()
                    updateParkingDropDown()
                    updateFurnishedDropDown()
                    updateOpenSidesDropDown()

                    binding.rvAmenities.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.rvAmenities.adapter =
                        AmenitiesAdapter(filterData!!.Amenitiesmaster, this)

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onTypeClick(action: String, propertyDataModel: PropertyTypeDataModel?) {
    }

    private fun updateBathroomDropDown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = BathroomDropdownAdapter(requireActivity(), filterData!!.bathroom)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.spinnerBathroom.setAdapter(arrayAdapter)

        binding.spinnerBathroom.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item

                bathromCount = filterData!!.bathroom[pos].id

            }
    }

    private fun updateBalconyDropDown() {
        val arrayAdapter = BathroomDropdownAdapter(requireActivity(), filterData!!.balconey)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.spinnerBalconey.setAdapter(arrayAdapter)

        binding.spinnerBalconey.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item

                balconyCount = filterData!!.balconey[pos].id
            }
    }

    private fun updateParkingDropDown() {
        val arrayAdapter = BathroomDropdownAdapter(requireActivity(), filterData!!.parking)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.spinnerParking.setAdapter(arrayAdapter)

        binding.spinnerParking.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                parkingCount = filterData!!.parking[pos].id
            }
    }

    private fun updateFurnishedDropDown() {
        val arrayAdapter = FurnishedDropdownAdapter(requireActivity(), filterData!!.furnishedmaster)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.spinnerFurnished.setAdapter(arrayAdapter)

        var furnished = ""
        if (furnishedType != 0) {
            for (i in 0 until filterData!!.furnishedmaster.size) {
                if (furnishedType == filterData!!.furnishedmaster[i].id)
                    furnished = filterData!!.furnishedmaster[i].codevalue
            }
            binding.spinnerFurnished.setText(furnished)
        }
            binding.spinnerFurnished.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                    //this is the way to find selected object/item

                    furnishedType = filterData!!.furnishedmaster[pos].id


        }
    }

    private fun updateOpenSidesDropDown() {
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = BathroomDropdownAdapter(requireActivity(), filterData!!.bathroom)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.spinnerOpenSides.setAdapter(arrayAdapter)

        binding.spinnerOpenSides.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item

                openSidesCount = filterData!!.bathroom[pos].id

            }
    }

    class PostPropertyAdditionalInfo(
        id: Int,
        propertyid: String,
        maintenancecharge: Int,
        securitydeposite: Int,
        bathroom: Int,
        attachedbathroom: Int,
        balcony: Int,
        parking: Int,
        iscorner: String,
        floorforconstruction: Int,
        openside: Int,
        anyconstruction: String,
        boundrywall: String,
        roadfacing: String,
        isingatedcolony: String,
        furnished: Int,
        depositpercentage: String,
        poolsize: String,
        additionalroom: String,
        remodalyear: String,
        amenities: String
    ) {
        private val id: Int = id
        private val propertyid: String = propertyid
        private val maintenancecharge: Int = maintenancecharge
        private val securitydeposite: Int = securitydeposite
        private val bathroom: Int = bathroom
        private val attachedbathroom: Int = attachedbathroom
        private val balcony: Int = balcony
        private val parking: Int = parking
        private val iscorner: String = iscorner
        private val floorforconstruction: Int = floorforconstruction
        private val openside: Int = openside
        private val anyconstruction: String = anyconstruction
        private val boundrywall: String = boundrywall
        private val roadfacing: String = roadfacing
        private val isingatedcolony: String = isingatedcolony
        private val furnished: Int = furnished
        private val depositpercentage: String = depositpercentage
        private val poolsize: String = poolsize
        private val additionalroom: String = additionalroom
        private val remodalyear: String = remodalyear
        private val amenities: String = amenities
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

    override fun onPressedCustomDialogButton(pressedButtonName: String?, action: String?) {
        when (action) {
            "success" -> {
                loadHomeFragment()
            }
        }
    }
}


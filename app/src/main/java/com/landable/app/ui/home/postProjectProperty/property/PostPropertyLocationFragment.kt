package com.landable.app.ui.home.postProjectProperty.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostPropertyLocationInfoBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.Citymaster
import com.landable.app.ui.home.dataModels.FilterMasterDataModel
import com.landable.app.ui.home.dataModels.PropertyRawDataModel
import com.landable.app.ui.home.dataModels.Statemaster
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.profile.CityAdapter
import com.landable.app.ui.home.profile.StateAdapter
import org.json.JSONObject

class PostPropertyLocationFragment : Fragment() {

    private lateinit var binding: FragmentPostPropertyLocationInfoBinding
    private var propertyId: String = ""
    private var address: String = ""
    private var _id: Int = 0
    private var filterData: FilterMasterDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var stateList = ArrayList<Statemaster>()
 //   private var cityList = ArrayList<Citymaster>()

   // private var cityArrayAdapter: CityAdapter? = null
    private var stateId: Int = 37
    private var cityId: Int = 5338
    private var isComingForEdit:Boolean =false
    private var propertyData: PropertyRawDataModel? = null

    companion object {
        fun newInstance() = PostPropertyLocationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        propertyId = requireArguments().getString("propertyid")!!
        _id = requireArguments().getInt("id")
        address = requireArguments().getString("address")!!
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
                R.layout.fragment_post_property_location_info,
                container,
                false
            )

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Post property location page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        getFilterInfo()

       // binding.autoCompleteTextViewForState.setText("Delhi NCR")
        if (isComingForEdit){
            propertyData = requireArguments().getSerializable("propertyRaw") as PropertyRawDataModel
            binding.edPin.setText(propertyData!!.propertyraw[0].pincode)
            binding.edLandmark.setText(propertyData!!.propertyraw[0].Landmark)
            binding.edAddress.setText(propertyData!!.propertyraw[0].address1)
            stateId = propertyData!!.propertyraw[0].state
            cityId = propertyData!!.propertyraw[0].city
        }

        binding.buttonExit.setOnClickListener {
            if(binding.edAddress.text.toString().isNullOrEmpty()){
                CustomAlertDialog(requireContext(),"Alert", "Please fill all the fields").show()
            }else{
                val landmark = binding.edLandmark.text.toString()
                val Pincode = binding.edPin.text.toString()
                postPropertyLocationUpdate(
                    PostPropertyLocationInfo(
                        _id,
                        propertyId,
                        37,
                        5338,
                        address,
                        landmark,
                        Pincode
                    ),true
                )
            }
        }
        binding.buttonContinue.setOnClickListener {

                postPropertyLocationUpdate(
                    PostPropertyLocationInfo(
                        _id,
                        propertyId,
                        37,
                        5338,
                        address,
                        binding.edLandmark.text.toString(),
                        binding.edPin.text.toString()
                    ),false
                )
        }

        return binding.root
    }

    private fun loadHomeFragment() {
        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            HomeFragment.newInstance(),
            HomeFragment::class.java.name
        )
    }

    private fun postPropertyLocationUpdate(dataModel: PostPropertyLocationInfo,forExit:Boolean) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val postPropertyStep1Response = RegisterRepository().postPropertyLocationInfo(dataModel)
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
                        val msg = jsonObj.getString("msg")
                        val propertyid = jsonObj.getString("propertyid")
                        val id = jsonObj.getInt("id")
                        if (status == "updated") {
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                            if(forExit){
                                loadHomeFragment()
                            }else loadPostPropertyPageThree(id, propertyid)
                        }
                        else{
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun loadPostPropertyPageThree(id: Int, propertyid: String) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("propertyid", propertyid)
        bundle.putBoolean("isComingForEdit",isComingForEdit)
        val postPropertyUploadImageFragment = PostPropertyUploadImage.newInstance()
        postPropertyUploadImageFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            postPropertyUploadImageFragment,
            PostPropertyUploadImage::class.java.name

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
                    filterData = ParseResponse.parseStateCityResponse(it.toString())
                    stateList = filterData!!.statemaster
                    //Added city dropdown in post property package
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
        binding.autoCompleteTextViewForState.setAdapter(arrayAdapter)

        var stateName = ""
        if(stateId!= 0) {
            for (i in 0 until stateList.size) {
                if (stateId == stateList[i].id)
                    stateName = stateList[i].state
            }
            binding.autoCompleteTextViewForState.setText(stateName)
//            updateCityList(stateId)
        }else{
            binding.autoCompleteTextViewForState.setText("Delhi NCR")
//            updateCityList(stateId)
        }


        binding.autoCompleteTextViewForState.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                stateId = stateList[pos].id
              //  updateCityList(stateId)
            }
    }

//    private fun updateCityList(stateId: Int) {
//        // create an array adapter and pass the required parameter
//        // in our case pass the context, drop down layout , and array.
//        if (cityArrayAdapter == null) {
//            cityArrayAdapter = CityAdapter(requireActivity(), filterData!!.cityHashMap[stateId])
//            cityList = filterData!!.cityHashMap[stateId]!!
//        } else {
//            cityArrayAdapter!!.updateList(filterData!!.cityHashMap[stateId])
//            cityList = filterData!!.cityHashMap[stateId]!!
//        }
//        // set adapter to the autocomplete tv to the arrayAdapter
//        binding.autoCompleteTextViewForCity.setAdapter(cityArrayAdapter)
//
//        var cityNAme=""
//        if(cityId!= 0){
//            for(i in 0 until cityList.size){
//                if(cityId==cityList[i].id){
//                    cityNAme = cityList[i].city
//                }
//            }
//            binding.autoCompleteTextViewForCity.setText(cityNAme)
//        }else{
//
//        }
//
//        binding.autoCompleteTextViewForCity.onItemClickListener =
//            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
//                //this is the way to find selected object/item
//                cityId = (filterData!!.cityHashMap[stateId] as ArrayList<Citymaster>)[pos].id
//            }
//    }


    class PostPropertyLocationInfo(
        id: Int,
        propertyid: String,
        state: Int,
        city: Int,
        address: String,
        landmark: String,
        pincode: String

    ) {
        private val id: Int = id
        private val propertyid: String = propertyid
        private val state: Int = state
        private val city: Int = city
        private val address: String = address
        private val landmark: String = landmark
        private val pincode: String = pincode

    }
}
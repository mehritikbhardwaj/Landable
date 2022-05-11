package com.landable.app.ui.home.agent

import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
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
import com.landable.app.databinding.FragmentAddAgentBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.*
import com.landable.app.ui.home.postProjectProperty.filterAdapters.MonthsDropdownAdapter
import com.landable.app.ui.home.profile.CityAdapter
import com.landable.app.ui.home.profile.StateAdapter
import org.json.JSONObject
import java.io.File
import java.io.IOException

class AddAgentFragment : Fragment(), UploadImageDialogFragment.IUploadImageListener,
    OnMapReadyCallback {
    private lateinit var binding: FragmentAddAgentBinding
    private var filterData: FilterMasterDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var stateList = ArrayList<Statemaster>()
    private var cityArrayAdapter: CityAdapter? = null
    private var uri: Uri? = null
    private var stateId: Int = 0
    private var cityId: Int = 0
    private var cityName: String = ""
    private var lat: String = ""
    private var lon: String = ""
    private var mMap: GoogleMap? = null
    private var yesNoArray = java.util.ArrayList<MonthsDataModel>()
    private var isCertified: String = ""
    private var agentId: Int = 0
    private var profileData = UserDetailDataModel()

    companion object {
        fun newInstance() = AddAgentFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agentId =
            requireArguments().getInt("agentID")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideTopbar()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_agent, container, false)

        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Add Agent")
        (activity as HomeActivity).hideBottomNavigation()

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Add Agent Fragment", null)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Add agent page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )
        getFilterInfo()

        if (agentId != 0) {
            getAgencyProfileDetails(agentId)
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

        binding.edLongitude.setOnClickListener {
            if (binding.edAddress.text.toString().isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please fill address", Toast.LENGTH_LONG).show()
            } else {
                getLocationFromAddress(binding.edAddress.text.toString())
                binding.edLatitude.text = lat
                binding.edLongitude.text = lon
            }
        }
        binding.buttonUpdate.setOnClickListener {
            if (binding.contactPersonName.text.toString().isNullOrEmpty() ||
                binding.number.text.toString().isNullOrEmpty() ||
                binding.email.text.toString().isNullOrEmpty()
            ) {
                CustomAlertDialog(requireContext(), "Alert", "Please enter all the details").show()
            } else {
                addAgentFunction(
                    AddUpdateAgent(
                        agentId,
                        binding.contactPersonName.text.toString(),
                        binding.email.text.toString(),
                        binding.number.text.toString(),
                        "",
                        stateId,
                        cityId,
                        binding.edAddress.text.toString(),
                        isCertified,
                        "Pending",
                        binding.rera.text.toString(),
                        lat,
                        lon
                    )
                )
            }
        }

        binding.ivUpdateFace.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = UploadImageDialogFragment(this, "openForUploadImageForFace")
            dialogFragment.show(fm, "")
        }

        updateIsCertifiedDropDown()
        return binding.root
    }

    private fun getAgencyProfileDetails(id: Int) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val propertyResponse = RegisterRepository().getUserDetails(id)
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
                    profileData = ParseResponse.parseUserDetailsResponse(it.toString())

                    updateUIData()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUIData() {
        binding.ivProfileImage.load(LandableConstants.Image_URL + profileData.profile.profilepic)
        binding.contactPersonName.setText(profileData.profile.name)
        binding.edAddress.setText(profileData.profile.address)
        binding.number.setText(profileData.profile.mobile)
        binding.email.setText(profileData.profile.email)
        binding.rera.setText(profileData.profile.rera)
        binding.edLatitude.text = profileData.profile.lat
        binding.edLongitude.text = profileData.profile.lon
        getLocationFromAddress(profileData.profile.address)
        binding.edLatitude.text = lat
        binding.edLongitude.text = lon
    }


    private fun updateIsCertifiedDropDown() {

        yesNoArray.add(MonthsDataModel(1, "Yes"))
        yesNoArray.add(MonthsDataModel(2, "No"))

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val monthsAdapter = MonthsDropdownAdapter(requireActivity(), yesNoArray)

        // set adapter to the autocomplete tv to the arrayAdapter
        binding.autoCompleteTextViewForRera.setAdapter(monthsAdapter)
        binding.autoCompleteTextViewForRera.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                //unitId = filterData!!.Unitmaster[pos].id
                if (pos == 0) {
                    isCertified == "YES"
                } else isCertified == "NO"
            }
    }

    fun getLocationFromAddress(strAddress: String?): LatLng? {
        Toast.makeText(requireContext(), "Please wait getting lat lon...", Toast.LENGTH_SHORT)
            .show()
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


    private fun updateSelectedImageBitmap(uri: Uri) {
        this.uri = uri
    }

    private fun getFilterInfo() {

        val filterResponse = RegisterRepository().getFilterMaster()
        filterResponse.observe(viewLifecycleOwner) {
            // hide progress bar

            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    filterData = ParseResponse.parseStateCityResponse(it.toString())
                    stateList = filterData!!.statemaster

                    ///Added city dropdown in post property package
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

        binding.autoCompleteTextViewForState.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                stateId = stateList[pos].id
                binding.autoCompleteTextViewForCity.setText("Select City")
                updateCityList(stateId)
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
        binding.autoCompleteTextViewForCity.setAdapter(cityArrayAdapter)

        binding.autoCompleteTextViewForCity.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                //this is the way to find selected object/item
                cityId = (filterData!!.cityHashMap[stateId] as ArrayList<Citymaster>)[pos].id
                cityName = (filterData!!.cityHashMap[stateId] as ArrayList<Citymaster>)[pos].city
            }
    }


    override fun onUploadImageDialogButtonClick(
        isClickedCameraButton: Boolean,
        selectedPhotoList: ArrayList<String>,
        action: String
    ) {
        if (selectedPhotoList.size == 1) {
            updateSelectedImageBitmap(Uri.parse(selectedPhotoList[0]))
            binding.ivProfileImage.load(Uri.parse(selectedPhotoList[0]))
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        progressDialog!!.updateProgress(percentage)
    }

    override fun onError(errorMessage: String) {
        progressDialog!!.cancelProgress()
        CustomAlertDialog(requireContext(), "Alert", "$errorMessage Please try again").show()
    }

    override fun onFinish(response: String) {
        progressDialog!!.cancelProgress()
        Toast.makeText(requireContext(), "Added Successfully", Toast.LENGTH_LONG).show()
        FragmentHelper().popBackStackImmediate(activity as HomeActivity)
    }

    override fun uploadStart() {
        progressDialog!!.updateProgress(0)
    }

    private fun addAgentFunction(dataModel: AddUpdateAgent) {
        val addAgentResponse =
            RegisterRepository().post_AddupdateAgent(dataModel)
        addAgentResponse.observe(viewLifecycleOwner) {

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
                        val status = jsonObj.getString("Message")
                        agentId = jsonObj.getInt("userid")
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        if (uri != null) {
                            updateUserProfilePicture()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun updateUserProfilePicture() {
        if (uri == null) {
            CustomAlertDialog(
                requireContext(),
                "Alert",
                "Please upload agent profile picture."
            ).show()
        } else {
            val filePath = if ("content" == uri!!.scheme) {
                Log.e("contains", "getUri.getScheme()")
                ClsGlobal.getPathFromUri(context, uri)
            } else {
                uri!!.path
            }

            progressDialog = CustomProgressDialog(requireContext())
            progressDialog!!.show()

            UploadImage(
                "profileUpdate",
                File(filePath!!),
                this, "", agentId, "", "", "", "", "", ""
            )
        }
    }


    class AddUpdateAgent(
        id: Int,
        name: String,
        email: String,
        mobile: String,
        image: String,
        state: Int,
        city: Int,
        address: String,
        iscertified: String,
        status: String,
        rera: String,
        lat: String,
        lon: String,
    ) {
        private var id: Int = id
        private var name: String = name
        private var email: String = email
        private var mobile: String = mobile
        private var image: String = image
        private var state: Int = state
        private var city: Int = city
        private var address: String = address
        private var iscertified: String = iscertified
        private var status: String = status
        private var rera: String = rera
        private var lat: String = lat
        private var lon: String = lon
    }
}

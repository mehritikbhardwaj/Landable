package com.landable.app.ui.home.profile

import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
import com.bumptech.glide.Glide
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
import com.landable.app.databinding.FragmentEditProfileBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomConfirmationDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.Citymaster
import com.landable.app.ui.home.dataModels.FilterMasterDataModel
import com.landable.app.ui.home.dataModels.Statemaster
import com.landable.app.ui.home.dataModels.UserProfileDataModel
import org.json.JSONObject
import java.io.File
import java.io.IOException


class EditProfileFragment : Fragment(), UploadImageDialogFragment.IUploadImageListener,
    OnMapReadyCallback, CustomConfirmationDialog.ICustomConfirmationDialogListener {
    private lateinit var binding: FragmentEditProfileBinding
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
    private var userData = ArrayList<UserProfileDataModel>()

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userData =
            requireArguments().getSerializable("profileData") as ArrayList<UserProfileDataModel>
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideTopbar()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)

        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Edit Profile")
        (activity as HomeActivity).hideBottomNavigation()
        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Edit Profile Fragment", null);

        getFilterInfo()

        Glide.with(binding.ivProfileImage.context)
            .load(AppInfo.getUserImage())
            .placeholder(R.drawable.user_circle)
            .into(binding.ivProfileImage)

        binding.contactPersonName.setText(userData[0].name)
        binding.edAgencyName.setText(userData[0].agencyname)
        binding.email.setText(userData[0].email)
        binding.number.setText(userData[0].mobile)
        binding.edAddress.setText(userData[0].address)
        binding.edLatitude.text = userData[0].lat
        binding.edLongitude.text = userData[0].lon
        binding.description.setText(userData[0].description)


        if (AppInfo.getCustomerType() == "Individual") {
            binding.llAgencyProfile.visibility = View.GONE
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.contactPersonName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
            } else {
                Toast.makeText(
                    requireContext(),
                    binding.contactPersonName.text.toString(),
                    Toast.LENGTH_LONG
                ).show()
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

        binding.buttonUpdate.setOnClickListener {
            if (binding.contactPersonName.text.toString().isNullOrEmpty() ||
                binding.number.text.toString().isNullOrEmpty() ||
                binding.email.text.toString().isNullOrEmpty()
            ) {
                CustomAlertDialog(requireContext(), "Alert", "Please enter all the details").show()
            } else {
                updateProfileFunction(
                    UserProfileUpdate(
                        binding.contactPersonName.text.toString(),
                        binding.number.text.toString(),
                        binding.email.text.toString(),
                        stateId,
                        cityId,
                        binding.edAddress.text.toString(),
                        cityName,
                        binding.edAgencyName.text.toString(),
                        binding.description.text.toString(),
                        "",
                        binding.edLatitude.text.toString().toDouble(),
                        binding.edLongitude.text.toString().toDouble()
                    )
                )
            }

        }

        binding.ivUpdateFace.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = UploadImageDialogFragment(this, "openForUploadImageForFace")
            dialogFragment.show(fm, "")
        }

        return binding.root
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
            OnItemClickListener { adapterView, view, pos, id ->
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
            OnItemClickListener { adapterView, view, pos, id ->
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
        Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_LONG).show()
        FragmentHelper().popBackStackImmediate(activity as HomeActivity)
    }

    override fun uploadStart() {
        progressDialog!!.updateProgress(0)
    }

    private fun updateProfilePicture() {
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
            if (binding.contactPersonName.text.toString().isNullOrEmpty()
                || binding.number.text.toString().isNullOrEmpty() ||
                binding.email.text.toString().isNullOrEmpty() || uri == null
            ) {
                CustomAlertDialog(
                    requireContext(),
                    "Alert",
                    "Please fill all the fields or select picture."
                ).show()
            } else {
                progressDialog = CustomProgressDialog(requireContext())
                progressDialog!!.show()
                UploadImage(
                    "profileUpdate", File(filePath!!),
                    this, "", AppInfo.getUserId().toInt(), "", "", "", "", "", ""
                )
            }
        }
    }

    private fun updateProfileFunction(dataModel: UserProfileUpdate) {
        val addAgentResponse =
            RegisterRepository().postUserProfileUpdate(dataModel)
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
                        if (uri != null) {
                            updateProfilePicture()
                        } else {
                            if (status == "success") {
                                CustomConfirmationDialog(
                                    requireContext(),
                                    "Alert",
                                    "Profile Updated Successfully",
                                    "update",
                                    true,
                                    this
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    class UserProfileUpdate(
        name: String,
        mobile: String,
        email: String,
        state: Int,
        city: Int,
        address: String,
        cityname: String,
        agencyname: String,
        description: String,
        profilepic: String,
        lat: Double,
        lon: Double,
    ) {
        private var name: String = name
        private var mobile: String = mobile
        private var email: String = email
        private var state: Int = state
        private var city: Int = city
        private var address: String = address
        private var cityname: String = cityname
        private var agencyname: String = agencyname
        private var description: String = description
        private var profilepic: String = profilepic
        private var lat: Double = lat
        private var lon: Double = lon
    }

    override fun onPressedCustomDialogButton(pressedButtonName: String?, action: String?) {
        when (action) {
            "update" -> {
                FragmentHelper().popBackStackImmediate(activity as HomeActivity)
            }
        }
    }


}
package com.landable.app.ui.home.property

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.model.LatLng
import com.landable.app.R
import com.landable.app.databinding.DialogAddLocationBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import java.io.IOException

class AddYourLocationDailogFragment(
    private var listener: IAddLocation
) : DialogFragment() {

    private var binding: DialogAddLocationBinding? = null
    private var progressDialog: CustomProgressDialog? = null
    private var latlong:LatLng? = null
    override fun onStart() {
        super.onStart()

        if (dialog!!.window != null) {
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog!!.window!!.setGravity(Gravity.CENTER)
            dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_location, container, false)

        binding!!.closeButton.setOnClickListener {
            dismiss()
        }


        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Add location page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        binding!!.buttonPostReview.setOnClickListener {
            if(binding!!.edAddress.text.toString().isNullOrEmpty()){
                CustomAlertDialog(requireContext(),"Alert","Please fill address.").show()
            }else {
                latlong = getLocationFromAddress(binding!!.edAddress.text.toString())
                if(latlong!=null ){
                    listener.onLocationAdded(
                        binding!!.edTitle.text.toString(),
                        binding!!.edAddress.text.toString(),
                        latlong!!
                    )
                }
                dismiss()

            }
        }
        return binding!!.root
    }

    interface IAddLocation {
        fun onLocationAdded(title: String, address:String,latlong: LatLng)
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
                return p1
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
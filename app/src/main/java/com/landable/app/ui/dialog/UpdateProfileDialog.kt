package com.landable.app.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.DialogProfileUpdateBinding
import com.landable.app.ui.HomeActivity
import org.json.JSONObject

class UpdateProfileDialog(
    val activity: Activity,
    /*val listener: UploadTypeListener*/
) :
    Dialog(activity) {

    var binding: DialogProfileUpdateBinding? = null
    var name = ""
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_profile_update,
            null,
            false
        )
        setContentView(binding!!.root)

        if (window != null) {
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window!!.setGravity(Gravity.CENTER)
            window!!.setBackgroundDrawableResource(R.color.transparent)
        }

        binding!!.edMobile.setText(AppInfo.getUserMobile())
        binding!!.edName.setText(AppInfo.getName())
        binding!!.edAddress.setText(AppInfo.getUserEmail())

        binding!!.closeButton.setOnClickListener {
            dismiss()
        }
        binding!!.buttonUpdate.setOnClickListener {
             name = binding!!.edName.text.toString()
             email = binding!!.edAddress.text.toString()
            val mobile = binding!!.edMobile.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(
                    activity as HomeActivity,
                    "Please Enter your name",
                    Toast.LENGTH_LONG
                ).show()
            }else updateProfile(ProfileDataModel(name, email, mobile))
            // listener.onClickButtonForUploadType("llPostProperty")
            dismiss()
        }

    }

    private fun updateProfile(dataModel: ProfileDataModel) {
        val updateResponse = RegisterRepository().post_shortprofileupdate(dataModel)
        updateResponse.observe(activity as HomeActivity) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    context,
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                if (it.toString() != "null") {
                    val jsonObj = JSONObject(it)
                    val status = jsonObj.getString("status")
                    if (status == "update") {
                        AppInfo.setName(name)
                        AppInfo.setUserEmail(email)
                        Toast.makeText(context, "Profile Updated Successfully.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    interface UploadTypeListener {
        fun onClickButtonForUploadType(typeOfUpload: String)
    }


    class ProfileDataModel(
        name: String,
        email: String,
        mobile: String
    ) {
        private val name: String = name
        private val email: String = email
        private val mobile: String = mobile
    }
}
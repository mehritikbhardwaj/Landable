package com.landable.app.ui.home.agent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.DialogContactOwnerBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.UserDetailDataModel

class ContactOwnerDialogFragment(
    private var agentNumber: String,
    private var agentName: String,
    private var agentMail: String,
    private var agentID: Int
) : DialogFragment() {

    private var binding: DialogContactOwnerBinding? = null
    private var progressDialog: CustomProgressDialog? = null
    private var profileData = UserDetailDataModel()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_contact_owner, container, false)


        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Contact Agent Fragment", null)

        binding!!.closeButton.setOnClickListener {
            dismiss()
        }

        binding!!.tvName.text = agentName

        getAgencyProfileDetails(agentID)

        binding!!.buttonCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel: $agentNumber")
            startActivity(intent)
        }

        binding!!.buttonMail.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$agentMail"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "email_subject")
            intent.putExtra(Intent.EXTRA_TEXT, "email_body")
            startActivity(intent)
        }


        return binding!!.root
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
                    profileData = ParseResponse.parseContactOwnerDetails(it.toString())
                    agentNumber = profileData.profile.mobile
                    agentMail = profileData.profile.email
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
        binding!!.ivProfileImage.load(LandableConstants.Image_URL + profileData.profile.profilepic)
        if(profileData.profile.agencyname.isEmpty()){
            binding!!.tvAgency.text = "Individual"
        }else binding!!.tvAgency.text = profileData.profile.agencyname
    }

}
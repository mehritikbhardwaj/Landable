package com.landable.app.ui.home.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.IListener
import com.landable.app.common.LandableConstants
import com.landable.app.common.Utility
import com.landable.app.databinding.FragmentAgencySignUpBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.verifyOTP.VerifyOtpFragment
import org.json.JSONObject

class AgencySignUpFragment : Fragment(), IListener {

    private lateinit var binding: FragmentAgencySignUpBinding
    private var progressDialog: CustomProgressDialog? = null
    private var number: String? = null

    companion object {
        fun newInstance() = AgencySignUpFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        number = arguments?.getString("number")

    }


    private var viewModel: AgencySignUpViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_agency_sign_up, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Agency Signup Fragment", null);

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        binding.edEmail.setText(number)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Agency signup page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        binding.checkboxSubscribe.setOnCheckedChangeListener { _, b ->
            if (viewModel != null) {
                if (b) {
                    viewModel!!.isAcceptedSubscription = "Yes"
                } else {
                    viewModel!!.isAcceptedSubscription = "No"
                }
            }
        }
        binding.checkboxTMC.setOnCheckedChangeListener { _, b ->
            if (viewModel != null) {
                if (b) {
                    viewModel!!.isAcceptedTermAndCondition = "Yes"
                } else {
                    viewModel!!.isAcceptedTermAndCondition = "No"
                }
            }
        }
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AgencySignUpViewModel::class.java)
        binding.agencySignUpViewModel = viewModel
        viewModel!!.listener = this
        viewModel!!.phone = number
    }

    override fun onStarted(action: String) {
        if (action == "signUpButtonClick") {
            // Show progress bar
            progressDialog = CustomProgressDialog(requireContext())
            progressDialog!!.show()
        }
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        when (action) {
            "loginButtonClick" -> {
                FragmentHelper().popBackStackImmediate(requireActivity())
            }
            "signUpButtonClick" -> {
                if (action == "signUpButtonClick") {
                    response.observe(this) {
                        // hide progress bar
                        progressDialog!!.cancelProgress()

                        if (it == LandableConstants.noInternetErrorMessage) {
                            //print NOInternet Error Message
                            CustomAlertDialog(
                                requireContext(),
                                LandableConstants.noInternetErrorTitle,
                                it
                            ).show()
                        } else if (it.isEmpty()) {
                            CustomAlertDialog(
                                requireContext(),
                                "Alert",
                                "Please try again."
                            ).show()
                        } else {
                            val jsonObj = JSONObject(it)
                            val status = jsonObj.getString("status")
                            val otp = jsonObj.getInt("otp")
                            val userid = jsonObj.getInt("userid")
                            val userstatus = jsonObj.getString("userstatus")

                            if (status == "This mobile number is already registered on landable, try to login or reset password.") {
                                CustomAlertDialog(requireContext(), "Alert", status).show()
                            } else if (userstatus == "Pending" && status == "success") {
                                val bundle = Bundle()
                                bundle.putInt("otp", otp)
                                bundle.putInt("userid", userid)
                                bundle.putString("email", binding.edEmail.text.toString())
                                val verifyOtpFragment = VerifyOtpFragment.newInstance()
                                verifyOtpFragment.arguments = bundle
                                FragmentHelper().replaceFragmentAddToBackstack(
                                    requireActivity().supportFragmentManager,
                                    (activity as HomeActivity).getHomePageContainerId(),
                                    verifyOtpFragment,
                                    VerifyOtpFragment::class.java.name
                                )
                            } else {
                                CustomAlertDialog(requireContext(), "Alert", status).show()
                            }

                        }
                    }
                }
            }
        }
    }

    override fun onFailure(message: String, action: String) {
        if (action == "signUpButtonClick")
            CustomAlertDialog(requireContext(), "Alert", message).show()
    }

}
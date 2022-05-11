package com.landable.app.ui.home.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.databinding.FragmentLoginBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.forgotPassword.ForgotPasswordFragment
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.signUp.SignUpFragment
import com.landable.app.ui.home.verifyOTP.VerifyOtpFragment
import org.json.JSONObject

class LoginFragment : Fragment(), IListener {

    private lateinit var binding: FragmentLoginBinding

    private var progressDialog: CustomProgressDialog? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).hideBottomNavigation()

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Login Fragment", null);

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Login page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )
        binding.loginWithOTP.setOnClickListener {
            openLoginOTPFragment()
        }

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.loginViewModel = loginViewModel
        loginViewModel.listener = this
    }

    override fun onStarted(action: String) {
        if (action == "loginButtonClick") {
            // Show progress bar
            progressDialog = CustomProgressDialog(requireContext())
            progressDialog!!.show()
        }
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        if (action == "loginButtonClick") {
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
                    try {
                        val userstatus = jsonObj.getString("userstatus")
                        val otp = jsonObj.getInt("otp")
                        val userid = jsonObj.getInt("userid")
                        val email = jsonObj.getString("email")
                        val scode = jsonObj.getString("scode")
                        when {
                            status.equals("Invalid") -> {
                                CustomAlertDialog(requireContext(), "Alert", status).show()
                            }
                            userstatus.equals("Pending") -> {
                                val bundle = Bundle()
                                bundle.putInt("otp", otp)
                                bundle.putInt("userid", userid)
                                bundle.putString("email", email)
                                val verifyOtpFragment = VerifyOtpFragment.newInstance()
                                verifyOtpFragment.arguments = bundle
                                FragmentHelper().replaceFragment(
                                    requireActivity().supportFragmentManager,
                                    (activity as HomeActivity).getHomePageContainerId(),
                                    verifyOtpFragment,
                                    VerifyOtpFragment::class.java.name
                                )
                            }
                            status.equals("success") && userstatus.equals("Active") -> {

                                updateSharedPreference(
                                    "Normal",
                                    userid.toString(),
                                    scode,
                                    email
                                )
                                callHomeFragment()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } else if (action == "signUpButtonClick") {
            openSignUpFragment()
        } else if (action == "forgotPasswordButtonClick") {
            openForgotPasswordFragment()
        } else if (action == "skipButtonClick") {
            (activity as HomeActivity).propertyID = ""
            callHomeFragment()
        }
    }

    override fun onFailure(message: String, action: String) {
        if (action == "loginButtonClick")
            CustomAlertDialog(requireContext(), "Alert", message).show()
    }

    private fun openSignUpFragment() {
        activity?.let {
            FragmentHelper().replaceFragmentAddToBackstack(
                it.supportFragmentManager,
                (activity as HomeActivity).getHomePageContainerId(),
                SignUpFragment.newInstance(),
                SignUpFragment::class.java.name
            )
        }
    }


    private fun openForgotPasswordFragment() {
        activity?.let {
            FragmentHelper().replaceFragmentAddToBackstack(
                it.supportFragmentManager,
                (activity as HomeActivity).getHomePageContainerId(),
                ForgotPasswordFragment.newInstance(),
                ForgotPasswordFragment::class.java.name
            )
        }
    }

    private fun openLoginOTPFragment() {
        activity?.let {
            FragmentHelper().replaceFragmentAddToBackstack(
                it.supportFragmentManager,
                (activity as HomeActivity).getHomePageContainerId(),
                OTPLoginFragment.newInstance(),
                OTPLoginFragment::class.java.name
            )
        }
    }

    private fun callHomeFragment() {
        activity?.let {
            FragmentHelper().replaceFragment(
                it.supportFragmentManager,
                (activity as HomeActivity).getHomePageContainerId(),
                HomeFragment.newInstance(),
                HomeFragment::class.java.name
            )
        }
    }


    private fun updateSharedPreference(
        loginType: String,
        userId: String,
        scode: String,
        email: String
    ) {
        AppInfo.setLoginType(loginType)
        AppInfo.setUserId(userId)
        AppInfo.setSCode(scode)
        AppInfo.setUserEmail(email)
    }


}
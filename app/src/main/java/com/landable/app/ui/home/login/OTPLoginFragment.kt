package com.landable.app.ui.home.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.FragmentLoginWithOtpBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.signUp.SignUpFragment
import com.landable.app.ui.home.verifyOTP.VerifyOtpFragment
import org.json.JSONObject

class OTPLoginFragment : Fragment(), MyCountDownTimer.ICompleteTimerListener {

    private lateinit var binding: FragmentLoginWithOtpBinding
    private var countDownTimer: MyCountDownTimer? = null
    private var userid: Int = 0

    companion object {
        fun newInstance() = OTPLoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_with_otp, container, false)

        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).hideBottomNavigation()

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

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        binding.skip.setOnClickListener {
            callHomeFragment()
        }
        binding.skipicon.setOnClickListener {
            callHomeFragment()
        }
        binding.tvResendOtp.isClickable = false
        binding.tvResendOtp.setTextColor(resources.getColor(R.color.progressBarBgColor))

        binding.signUpButton.setOnClickListener {
            openSignUpFragment("")
        }

/*binding.loginWithPassword.setOnClickListener {
            openLoginFragment()}*/

        binding.sendOTP.setOnClickListener {
            if (binding.edMobile.text.toString().isNullOrEmpty()) {
                CustomAlertDialog(
                    requireContext(),
                    "Alert",
                    "Please enter mobile number."
                ).show()
            } else {
                startTimerAndUpdateUi()
                binding.sendOTP.visibility = View.GONE
                binding.edPin.visibility = View.VISIBLE
                binding.loginButton.visibility = View.VISIBLE
                postLoginOTP(
                    OTPDataModel(
                        binding.edMobile.text.toString(),
                        LandableConstants.fcmToken,
                        LandableConstants.deviceType
                    )
                )
                (activity as HomeActivity).postUserTrackingModel(
                    HomeActivity.PostUserTrackingModel(
                        "Otp verification page",
                        "Visit",
                        "Visit",
                        "Visit",
                        "",
                        ""
                    )
                )
            }
        }

        binding.tvResendOtp.setOnClickListener {
            if (binding.edMobile.text.toString().isNullOrEmpty()) {
                CustomAlertDialog(
                    requireContext(),
                    "Alert",
                    "Please enter mobile number."
                ).show()
            } else {
                startTimerAndUpdateUi()
                binding.sendOTP.visibility = View.GONE
                binding.edPin.visibility = View.VISIBLE
                binding.loginButton.visibility = View.VISIBLE
                postLoginOTP(
                    OTPDataModel(
                        binding.edMobile.text.toString(),
                        LandableConstants.fcmToken,
                        LandableConstants.deviceType
                    )
                )
            }
        }

        binding.loginButton.setOnClickListener {
            if (binding.edPin.text.toString().isNullOrEmpty()) {
                CustomAlertDialog(
                    requireContext(),
                    "Alert",
                    "Please enter otp."
                ).show()
            } else {
                verifyOTp(
                    VerifyOtpDataModel(
                        userid,
                        binding.edPin.text.toString().toInt(),
                        LandableConstants.fcmToken,
                        LandableConstants.deviceType
                    )
                )
            }
        }

        return binding.root
    }

    private fun openSignUpFragment(number: String) {
        val bundle = Bundle()
        bundle.putString("number", number)
        val signupFragment = SignUpFragment.newInstance()
        signupFragment.arguments = bundle

        activity?.let {
            FragmentHelper().replaceFragmentAddToBackstack(
                it.supportFragmentManager,
                (activity as HomeActivity).getHomePageContainerId(),
                signupFragment,
                SignUpFragment::class.java.name
            )
        }
    }

    private fun startTimerAndUpdateUi() {
        // update button UI
        binding.tvResendOtp.isClickable = false
        binding.tvResendOtp.setTextColor(resources.getColor(R.color.progressBarBgColor))

        // call timer function
        countDownTimer = MyCountDownTimer(30000, 1000, binding.tvTimer, this)
        countDownTimer!!.start()
    }

    override fun onCompleteTimer(action: String) {
        binding.tvResendOtp.isClickable = true
        binding.tvResendOtp.setTextColor(resources.getColor(R.color.black))
    }

    private fun postLoginOTP(dataModel: OTPDataModel) {
        val loginResponse = RegisterRepository().loginwithOTP(dataModel)
        loginResponse.observe(viewLifecycleOwner) {

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
                        val userstatus = jsonObj.getString("userstatus")
                        val otp = jsonObj.getInt("otp")
                        val userid = jsonObj.getInt("userid")
                        val email = jsonObj.getString("email")
                        val scode = jsonObj.getString("scode")

                        if (status == "success") {
                            this@OTPLoginFragment.userid = userid
                            Toast.makeText(
                                requireContext(),
                                "Otp has been sent.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else if (status == "not exists") {
                            Toast.makeText(
                                requireContext(),
                                "Not a valid user.",
                                Toast.LENGTH_LONG
                            ).show()
                            openSignUpFragment(binding.edMobile.text.toString())
                        } else {
                            Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun verifyOTp(dataModel: VerifyOtpDataModel) {
        val loginResponse = RegisterRepository().post_loginOTPVerification(dataModel)
        loginResponse.observe(viewLifecycleOwner) {

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
                        val userstatus = jsonObj.getString("userstatus")
                        val userid = jsonObj.getInt("userid")
                        val scode = jsonObj.getString("scode")
                        val customertype = jsonObj.getString("customertype")
                        if (status == "success") {
                           // Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show()
                            AppInfo.setSCode(scode)
                            AppInfo.setUserId(userid.toString())
                            AppInfo.setCustomerType(customertype)
                            countDownTimer!!.cancel()
                            callHomeFragment()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "$status. Please try again.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    class OTPDataModel(
        username: String,
        fcmid: String,
        device: String
    ) {
        private var username: String = username
        private var fcmid: String = fcmid
        private var device: String = device
    }

    class VerifyOtpDataModel(
        userid: Int,
        otp: Int,
        fcmid: String,
        device: String
    ) {
        private var userid: Int = userid
        private var otp: Int = otp
        private var fcmid: String = fcmid
        private var device: String = device

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
}

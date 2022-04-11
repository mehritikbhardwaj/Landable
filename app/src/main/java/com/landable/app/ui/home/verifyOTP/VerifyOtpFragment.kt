package com.landable.app.ui.home.verifyOTP

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.FragmentVerifyEmailBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.homeUI.HomeFragment
import org.json.JSONObject

class VerifyOtpFragment : Fragment(), IListener, MyCountDownTimer.ICompleteTimerListener {

    private lateinit var binding: FragmentVerifyEmailBinding
    private var email: String? = null
    private var userid: Int? = null

    private var countDownTimer: MyCountDownTimer? = null

    private var progressDialog: CustomProgressDialog? = null


    companion object {
        fun newInstance() = VerifyOtpFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = arguments?.getString("email")
        userid = arguments?.getInt("userid")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email, container, false)

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        binding.tvEmail.text = email


        binding.tvResendOtp.setOnClickListener {
            if ((binding.tvTimer.text.toString()).toInt() == 0) {
                // update button UI & start timer
                startTimerAndUpdateUi()
                // call resend OTP API
                resendOtp(ResendOtpRequestDataModel(userid!!))
            }

        }

        startTimerAndUpdateUi()

        return binding.root
    }

    private fun startTimerAndUpdateUi() {
        // update button UI
        binding.tvResendOtp.isClickable = false
        binding.tvResendOtp.setTextColor(resources.getColor(R.color.progressBarBgColor))

        // call timer function
        countDownTimer = MyCountDownTimer(10000, 1000, binding.tvTimer, this@VerifyOtpFragment)
        countDownTimer!!.start()
    }

    private  fun stopTimerAndUpdateUi(){
        binding.tvResendOtp.isClickable = true
        binding.tvResendOtp.setTextColor(resources.getColor(R.color.black))
    }

    override fun onCompleteTimer(action: String) {
        binding.tvResendOtp.isClickable = true
        binding.tvResendOtp.setTextColor(resources.getColor(R.color.black))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(VerifyOtpViewModel::class.java)
        binding.verifyOtpViewModel = viewModel

        viewModel.listener = this
        viewModel.userid = userid
        viewModel.email = email

    }

    override fun onStarted(action: String) {
        if (action == "submitButtonClick") {
            // Show progress bar
            progressDialog = CustomProgressDialog(requireContext())
            progressDialog!!.show()
        }
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        when (action) {
            "submitButtonClick" -> {
                response.observe(this) {

                    // hide progress bar
                    progressDialog!!.cancelProgress()

                    // stop timer
                    if (countDownTimer != null)
                        countDownTimer!!.cancel()

                    when {
                        it == LandableConstants.noInternetErrorMessage -> {
                            //print NOInternet Error Message
                            CustomAlertDialog(
                                requireContext(),
                                LandableConstants.noInternetErrorTitle,
                                it
                            ).show()
                        }
                        it.isEmpty() -> {
                            CustomAlertDialog(
                                requireContext(),
                                "Alert",
                                "Please try again."
                            ).show()
                        }
                        else -> {

                            try {
                                val jsonObj = JSONObject(it)
                                val status = jsonObj.getString("status")
                                val userid = jsonObj.getInt("userid")
                                val scode = jsonObj.getString("scode")
                                val customertype = jsonObj.getString("customertype")
                                if (status == "Invalid OTP") {
                                    stopTimerAndUpdateUi()
                                    CustomAlertDialog(requireContext(), "Alert", status).show()

                                } else {
                                    updateSharedPreference(
                                        "Normal",
                                        userid.toString(),
                                        email!!,
                                        scode
                                    )
                                    AppInfo.setCustomerType(customertype)
                                    callHomeActivity()

                                }
                            } catch (e: Exception) {
                                CustomAlertDialog(
                                    requireContext(),
                                    "Alert",
                                    "Please try again."
                                ).show()
                                startTimerAndUpdateUi()
                            }
                        }
                    }
                }
            }
            "resendOTPButtonClick" -> {
                startTimerAndUpdateUi()
                resendOtp(ResendOtpRequestDataModel(userid!!))
            }
            "backButtonClick" -> {
                FragmentHelper().popBackStackImmediate(requireActivity())
            }
        }
    }


    private fun resendOtp(dataModel: ResendOtpRequestDataModel) {
        val resendOtpResponse = RegisterRepository().resendSignupOtp(dataModel)
        resendOtpResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                if (it.toString() != "null") {
                    val jsonObj = JSONObject(it)
                    val status = jsonObj.getString("status")
                    if (status == "success") {
                        Toast.makeText(context, "Otp has been Sent.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onFailure(message: String, action: String) {
        if (action == "submitButtonClick")
            CustomAlertDialog(requireContext(), "Alert", message).show()
    }

    private fun callHomeActivity() {
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
        email: String,
        scode: String
    ) {
        AppInfo.setLoginType(loginType)
        AppInfo.setUserId(userId)
        AppInfo.setUserEmail(email)
        AppInfo.setSCode(scode)
    }

    class ResendOtpRequestDataModel(
        UserId: Int
    ) {
        private val userid: Int = UserId
    }
}



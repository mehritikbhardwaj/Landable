package com.landable.app.ui.home.verifyOTP

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener
import com.landable.app.common.LandableConstants
import com.landable.app.common.NoInternetException
import com.landable.app.data.repositories.RegisterRepository

class VerifyOtpViewModel  : ViewModel() {

    var email: String? = null
    var otp: String? = null
    var resendOtpCounter: String? = null

    var listener: IListener? = null

    var userid: Int? = null

    private val dummyLiveData = MutableLiveData<String>()

    fun onSubmitButtonClick(view: View) {
        when {
            otp.isNullOrEmpty() -> {
                listener?.onFailure("Please enter OTP.", "submitButtonClick")
                return
            }

            otp!!.length > 4 || otp.toString().length < 4 -> {
                listener?.onFailure(
                    "Please enter valid OTP.",
                    "submitButtonClick"
                )
                return
            }
        }

        listener!!.onStarted("submitButtonClick")

        val oTP:Int = otp!!.toString().toInt()
        try {
            val verifyOtpResponse = RegisterRepository().verifyOtp(
                VerifyOtpRequestDataModel(
                    userid!!,
                    oTP,
                    LandableConstants.fcmToken,
                    LandableConstants.deviceType,
                )
            )
            listener!!.onSuccess(verifyOtpResponse, "submitButtonClick")
        } catch (e: NoInternetException) {
            listener!!.onFailure(e.message.toString(), "submitButtonClick")
        }
    }

    fun onResendButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "resendOTPButtonClick")
    }

    fun onBackButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "backButtonClick")
    }


    class VerifyOtpRequestDataModel(
        UserId: Int,
        OTP: Int,
        Fcm: String,
        Device: String,
    ) {
        private val userid: Int = UserId
        private val otp: Int = OTP
        private val fcmid: String = Fcm
        private val device: String = Device
    }
}
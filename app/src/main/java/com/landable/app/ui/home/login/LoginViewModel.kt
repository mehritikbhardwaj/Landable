package com.landable.app.ui.home.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener
import com.landable.app.common.NoInternetException
import com.landable.app.data.repositories.RegisterRepository

class LoginViewModel : ViewModel() {

    var username: String? = null
    var password: String? = null
    var listener: IListener? = null
    private val dummyLiveData = MutableLiveData<String>()

    fun onLoginButtonClick(view: View) {

        when {
            username.isNullOrEmpty() -> {
                listener?.onFailure("Please enter email/Mobile Number.", "loginButtonClick")
                return
            }
            username!!.length < 10 -> {
                listener?.onFailure(
                    "Please enter valid mobile number or email id.",
                    "loginButtonClick"
                )
                return
            }

            password.isNullOrEmpty() -> {
                listener?.onFailure("Please enter password.", "loginButtonClick")
                return
            }
            password!!.length < 2 -> {
                listener?.onFailure(
                    "Please enter password at least 6 character.",
                    "loginButtonClick"
                )
                return
            }
        }

        listener!!.onStarted("loginButtonClick")

        try {
            val loginUserResponse = RegisterRepository().userLogin(
                LoginUserRequestDataModel(
                    username!!,
                    password!!,
                    "Test FCM",
                    "Android"
                )
            )

            listener!!.onSuccess(
                loginUserResponse, "loginButtonClick")
        } catch (e: NoInternetException) {
            listener!!.onFailure(e.message.toString(), "loginButtonClick")
        }
    }

    fun onFacebookButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "facebookButtonClick")
    }

    fun onSkipButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "skipButtonClick")
    }

    fun onSkipTextClick(view: View){
        listener?.onSuccess(dummyLiveData, "skipButtonClick")
    }

    fun onGoogleButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "googleButtonClick")
    }

    fun onSignUpButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "signUpButtonClick")

    }

    fun onForgotPasswordButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "forgotPasswordButtonClick")
    }

    class LoginUserRequestDataModel(
        Username: String,
        Password: String,
        FCM: String,
        Device: String
    ) {
        private val username: String = Username
        private val password: String = Password
        private val fcmid: String = FCM
        private val device: String = Device
    }
}
package com.landable.app.ui.home.signUp

import android.util.Patterns
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener
import com.landable.app.common.LandableConstants
import com.landable.app.common.NoInternetException
import com.landable.app.data.repositories.RegisterRepository

class IndividualSignUpViewModel : ViewModel() {

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var phone: String? = null
    var isAcceptedTermAndCondition: String = "No"
    var isAcceptedSubscription: String = "No"
    var listener: IListener? = null

    private val dummyLiveData = MutableLiveData<String>()


    fun onSignUpButtonClick(view: View) {
        when {
            name.isNullOrEmpty() -> {
                listener?.onFailure("Please enter name.", "signUpButtonClick")
                return
            }

            name?.length!! > 40 -> {
                listener?.onFailure(
                    "Please enter valid name. It should not more than 25 character.",
                    "signUpButtonClick"
                )
                return
            }

            email.isNullOrEmpty() -> {
                listener?.onFailure("Please enter email.", "signUpButtonClick")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email!!.trim()).matches() -> {
                listener?.onFailure("Please enter valid email.", "signUpButtonClick")
                return
            }
            phone.isNullOrEmpty() -> {
                listener?.onFailure("Please enter mobile number.", "signUpButtonClick")
                return
            }
            !Patterns.PHONE.matcher(phone!!.trim()).matches() -> {
                listener?.onFailure("Please enter valid mobile number.", "signUpButtonClick")
                return
            }
          /*  password.isNullOrEmpty() -> {
                listener?.onFailure("Please enter password.", "signUpButtonClick")
                return
            }
            password!!.length < 8 -> {
                listener?.onFailure(
                    "Please enter password at least 8 character.",
                    "signUpButtonClick"
                )
                return
            }*/
            isAcceptedTermAndCondition=="No" ->{
                listener?.onFailure(
                    "Please accept term & condition.",
                    "signUpButtonClick"
                )
                return
            }
        }
        listener!!.onStarted("signUpButtonClick")

        try {
            val registerUserResponse = RegisterRepository().individualUserserRegister(
                RegisterUserRequestDataModel(
                    "Individual",
                    name!!,
                    phone!!,
                    email!!,
                    "",
                    "",
                    isAcceptedSubscription,
                    isAcceptedTermAndCondition,
                    LandableConstants.fcmToken,
                    LandableConstants.deviceType
                )
            )
             listener!!.onSuccess(registerUserResponse, "signUpButtonClick")
        } catch (e: NoInternetException) {
            listener!!.onFailure(e.message.toString(), "signUpButtonClick")
        }
    }

    fun onLoginButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "loginButtonClick")
    }

    class RegisterUserRequestDataModel(
        CustomerType: String,
        Name: String,
        Mobile: String,
        Email: String,
        AgencyName: String,
        Password: String,
        Subscribe: String,
        TMC: String,
        FCM: String,
        Device: String,

        ) {
        private val customertype: String = CustomerType
        private val name: String = Name
        private val mobile: String = Mobile
        private val email: String = Email
        private val agencyname: String = AgencyName
        private val password: String = Password
        private val subscribe: String = Subscribe
        private val tmc: String = TMC
        private val fcmid: String = FCM
        private val device: String = Device
    }
}
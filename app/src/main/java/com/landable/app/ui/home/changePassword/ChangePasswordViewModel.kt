package com.landable.app.ui.home.changePassword

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener
import com.landable.app.common.NoInternetException

class ChangePasswordViewModel : ViewModel() {

    var password: String? = null
    var confirmPassword: String? = null


    var listener: IListener? = null
    private val dummyLiveData = MutableLiveData<String>()

    fun onSubmitButtonClick(view: View) {
        when {
            password.isNullOrEmpty() -> {
                listener?.onFailure("Please enter password.", "submitButtonClick")
                return
            }
            password!!.length < 8 -> {
                listener?.onFailure(
                    "Please enter password at least 8 character.",
                    "submitButtonClick"
                )
                return
            }

            confirmPassword.isNullOrEmpty() -> {
                listener?.onFailure("Please enter confirm password.", "submitButtonClick")
                return
            }
            password?.trim().toString() != confirmPassword?.trim().toString() -> {
                listener?.onFailure(
                    "Confirmation password does not match new password.",
                    "submitButtonClick"
                )
                return
            }
        }

        listener!!.onStarted("submitButtonClick")
        try {
            /*  val resetUserResponse = RegisterRepository().resetPassword(password!!)
              listener!!.onSuccess(resetUserResponse, "submitButtonClick")
          */} catch (e: NoInternetException) {
            listener!!.onFailure(e.message.toString(), "submitButtonClick")
        }
    }


    class ResetPasswordRequestDataModel(password: String) {
        private val password: String = password
    }

    fun onBackButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "backButtonClick")
    }


}


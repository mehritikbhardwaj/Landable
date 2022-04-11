package com.landable.app.ui.home.forgotPassword

import android.util.Patterns
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener
import com.landable.app.common.NoInternetException
import com.landable.app.data.repositories.RegisterRepository

class ForgotPasswordViewModel : ViewModel() {

    var email: String? = null
    var listener: IListener? = null
    private val dummyLiveData = MutableLiveData<String>()

    fun onSendPasswordButtonClick(view: View) {
        when {
            email.isNullOrEmpty() -> {
                listener?.onFailure("Please enter email.", "sendPasswordClick")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email!!.trim()).matches() -> {
                listener?.onFailure("Please enter valid email.", "sendPasswordClick")
                return
            }

        }

        listener!!.onStarted("sendPasswordClick")

         try {
              val forgotPasswordResponse = RegisterRepository().forgetPassword(email!!)
              listener!!.onSuccess(forgotPasswordResponse, "sendPasswordClick")
          } catch (e: NoInternetException) {
              listener!!.onFailure(e.message.toString(), "sendPasswordClick")
          }
    }

    fun onBackButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "backButtonClick")
    }
}
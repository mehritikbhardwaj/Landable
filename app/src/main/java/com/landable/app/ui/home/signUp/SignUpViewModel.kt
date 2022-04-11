package com.landable.app.ui.home.signUp

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener

class SignUpViewModel : ViewModel() {

    var listener: IListener? = null

    private val dummyLiveData = MutableLiveData<String>()


    fun onBackButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "backButtonClick")
    }

    fun onAgencyButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "agencyButtonClick")
    }

    fun onIndividualButtonClick(view: View) {
        listener?.onSuccess(dummyLiveData, "individualButtonClick")
    }

}
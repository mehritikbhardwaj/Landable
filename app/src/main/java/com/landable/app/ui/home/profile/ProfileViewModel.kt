package com.landable.app.ui.home.profile

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener

class ProfileViewModel : ViewModel() {

    var listener: IListener? = null

    private val dummyLiveData = MutableLiveData<String>()

    fun onEditProfileClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onEditProfileClick")
    }

    fun onAgentClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onAgentClick")
    }

    fun onShortURLClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onShortURLClick")
    }

    fun onPostPropertyProjectClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onPostPropertyProjectClick")
    }

    fun onPostedPropertyProjectClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onPostedPropertyProjectClick")
    }

    fun onMyListingClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onMyListingClick")
    }

    fun onLeadsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onLeadsClick")
    }

    fun onFavoritesClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onFavoritesClick")
    }

    fun onActivityClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onActivityClick")
    }

    fun onSavedSearchesClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onSavedSearchesClick")
    }

    fun onChangePasswordClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onChangePasswordClick")
    }

    fun onLogoutClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onLogoutClick")
    }

}
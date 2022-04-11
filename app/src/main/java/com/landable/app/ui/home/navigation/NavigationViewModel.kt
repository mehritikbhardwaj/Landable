package com.landable.app.ui.home.navigation

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.landable.app.common.IListener

class NavigationViewModel : ViewModel() {

    var listener: IListener? = null

    private val dummyLiveData = MutableLiveData<String>()


    fun onDashboardClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onDashboardClick")
    }

    fun onSearchPropertyClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onSearchPropertyClick")
    }

    fun onAuctionsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onAuctionsClick")
    }

    fun onAddPosts(view: View) {
        listener?.onSuccess(dummyLiveData, "onAddPosts")

    }

    fun onSearchMapClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onSearchMapClick")
    }

    fun onSearchPostClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onSearchPostClick")
    }

    fun onMyPostsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onMyPostsClick")
    }

    fun onChatClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onChatClick")
    }

    fun onPropertyRegistrationLookup(view: View) {
        listener?.onSuccess(dummyLiveData, "onPropertyRegistrationLookup")
    }

    fun onAnalyzeTrendsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onAnalyzeTrendsClick")
    }

    fun onNewsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onNewsClick")
    }

    fun onBlogsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onBlogsClick")
    }

    fun onNotificationsClick(view: View) {
        listener?.onSuccess(dummyLiveData, "onNotificationsClick")
    }

    fun onProfileCLick(view: View) {
        listener?.onSuccess(dummyLiveData, "onProfileCLick")
    }

}
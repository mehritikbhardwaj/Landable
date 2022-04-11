package com.landable.app.common

import androidx.lifecycle.LiveData

interface IListener {
    
    fun onStarted(action: String)
    fun onSuccess(response: LiveData<String>, action: String)
    fun onFailure(message: String, action: String)
}
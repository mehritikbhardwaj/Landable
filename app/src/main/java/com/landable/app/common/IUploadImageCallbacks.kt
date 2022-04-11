package com.landable.app.common

interface IUploadImageCallbacks {
    fun onProgressUpdate(percentage: Int)
    fun onError()
    fun onFinish()
    fun uploadStart()
}
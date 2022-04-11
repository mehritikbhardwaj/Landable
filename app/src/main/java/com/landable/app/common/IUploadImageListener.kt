package com.landable.app.common

interface IUploadImageListener {
    fun onProgressUpdate(percentage: Int)
    fun onError(errorMessage: String)
    fun onFinish(response: String)
    fun uploadStart()
}
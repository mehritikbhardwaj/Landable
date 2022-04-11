package com.landable.app.common

import com.landable.app.ui.home.dataModels.Propertyimage

interface DocumentClickListener {
    fun onImageClick(action: String, propertyimage: Propertyimage)
}
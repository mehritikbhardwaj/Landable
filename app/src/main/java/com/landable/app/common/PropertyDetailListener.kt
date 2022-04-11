package com.landable.app.common

import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel

interface PropertyDetailListener {
    fun onPropertyClick(action: String, featurePropertiesDataModel : FeaturePropertiesDataModel?)
}
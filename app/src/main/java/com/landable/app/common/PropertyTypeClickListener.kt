package com.landable.app.common

import com.landable.app.ui.home.dataModels.CategoriesDataModel
import com.landable.app.ui.home.dataModels.PropertyTypeDataModel

interface PropertyTypeClickListener {
    fun onTypeClick(action: String, propertyDataModel: PropertyTypeDataModel?)

}
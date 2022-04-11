package com.landable.app.common

import com.landable.app.ui.home.dataModels.WhyLandableDataModel

interface WhyLandableClickListener {
    fun onWhyLandableClick(action: String, whyLandableDataModel: WhyLandableDataModel?)

}
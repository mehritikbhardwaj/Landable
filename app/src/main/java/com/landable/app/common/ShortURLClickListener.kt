package com.landable.app.common

import com.landable.app.ui.home.dataModels.ActivityDataModel
import com.landable.app.ui.home.dataModels.ShortUrlDataModelItem

interface ShortURLClickListener {
    fun onShortUrlClick(action: String, shortUrlDataModelItem: ShortUrlDataModelItem)

}
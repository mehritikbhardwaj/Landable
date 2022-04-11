package com.landable.app.common

import com.landable.app.ui.home.dataModels.NewsDataModelItem

interface NewsClickListener {
    fun onNewsClick(action: String, newsDataModel: NewsDataModelItem)

}
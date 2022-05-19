package com.landable.app.common

import com.landable.app.ui.home.dataModels.SavedSearchDataModelItem

interface SavedSearchListener {
    fun onClick(action: String, model: SavedSearchDataModelItem)
}
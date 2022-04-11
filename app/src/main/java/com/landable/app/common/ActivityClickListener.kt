package com.landable.app.common

import com.landable.app.ui.home.dataModels.ActivityDataModel

interface ActivityClickListener {
    fun onActivityClick(action: String, activity: ActivityDataModel?)
}
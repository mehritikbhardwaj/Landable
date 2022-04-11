package com.landable.app.common

import com.landable.app.ui.home.dataModels.Useractivity

interface AgencyActivityClickListener {
    fun onActivityClick(action: String, activity: Useractivity?)

}
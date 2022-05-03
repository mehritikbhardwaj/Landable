package com.landable.app.common

import com.landable.app.ui.home.dataModels.LeadsDataModel

interface LeadsClickListener {
    fun onLeadsClick(action: String, leadsDataModel: LeadsDataModel)

}
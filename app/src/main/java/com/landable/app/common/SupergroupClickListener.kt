package com.landable.app.common

import com.landable.app.ui.home.dataModels.SuperGroupsDataModelItem

interface SupergroupClickListener {
    fun onClickSuperGroup(action:String,superGroupsDataModelItem: SuperGroupsDataModelItem)
}
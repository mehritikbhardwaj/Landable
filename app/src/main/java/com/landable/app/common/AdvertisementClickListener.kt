package com.landable.app.common

import com.landable.app.ui.home.dataModels.Advertisment
import com.landable.app.ui.home.dataModels.ProjectsDataModel

interface AdvertisementClickListener {
    fun onAdvertisemntClick(action: String, advertisementDataModel: Advertisment?)

}
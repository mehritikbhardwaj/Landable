package com.landable.app.common

import com.landable.app.ui.home.dataModels.Bedroom
import com.landable.app.ui.home.dataModels.Floormaster

interface BedBathClickListener {
    fun onBedBathClick(action: String, bedroom: Bedroom)
}
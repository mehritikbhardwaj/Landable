package com.landable.app.common

import com.landable.app.ui.home.dataModels.Floormaster

interface FloorClickListener {
    fun onFloorClick(action: String, floormaster: Floormaster)
}
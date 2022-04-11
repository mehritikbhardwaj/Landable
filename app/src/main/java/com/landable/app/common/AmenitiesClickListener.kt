package com.landable.app.common

import com.landable.app.ui.home.dataModels.Amenitiesmaster

interface AmenitiesClickListener {
    fun onAmenitiesSelected(
        action: String?,
        selectedAmenitiesList: ArrayList<Amenitiesmaster>
    )
}
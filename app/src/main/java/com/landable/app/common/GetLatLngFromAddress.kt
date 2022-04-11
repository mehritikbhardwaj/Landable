package com.landable.app.common

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

class GetLatLngFromAddress {

    fun getLatLng(strAddress: String?, context: Context?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var latLng: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            latLng = if (address.isEmpty()) {
                LatLng(0.0, 0.0)
            } else {
                val location: Address = address[0]
                location.latitude
                location.longitude
                LatLng(location.latitude, location.longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return latLng
    }

}
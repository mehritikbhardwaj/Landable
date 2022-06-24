package com.landable.app.ui.home.homeUI

import android.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.landable.app.databinding.FragmentMapViewBinding
import com.landable.app.ui.HomeActivity
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapView : Fragment() {

    private lateinit var binding: FragmentMapViewBinding

    private val myOpenMapView: MapView? = null
    private val myMapController: MapController? = null
    var locationManager: LocationManager? = null

    companion object {
        fun newInstance() = MapView()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).showBottomNavigation()
        binding = DataBindingUtil.inflate(
            inflater,
            com.landable.app.R.layout.fragment_map_view,
            container,
            false
        )

        val myLocationoverlay = MyLocationNewOverlay(binding.mapView)
        myLocationoverlay.enableFollowLocation()
        val currentDraw = ResourcesCompat.getDrawable(resources, R.drawable.ic_dialog_map, null)
        var currentIcon: Bitmap? = null
        if (currentDraw != null) {
            currentIcon = (currentDraw as BitmapDrawable).bitmap
        }
        myLocationoverlay.setPersonIcon(currentIcon)
        myLocationoverlay.enableMyLocation()
        binding.mapView.getOverlays().add(myLocationoverlay)

        return binding.root
    }


}
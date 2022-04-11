package com.landable.app.ui.home.property.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.databinding.RowLocationDetailBinding
import com.landable.app.ui.home.dataModels.Distancefromlocation

class DistanceFromLocationAdapter(
    private val locationList: ArrayList<Distancefromlocation>
) : RecyclerView.Adapter<LocationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding: RowLocationDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_location_detail,
            parent,
            false
        )
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]


        holder.locationBinding.tvTitle.text = location.title
        holder.locationBinding.tvAddress.text = location.address
        holder.locationBinding.tvDistance.text = location.distance

    }

    override fun getItemCount(): Int {
        return locationList.size
    }
}


class LocationViewHolder(var locationBinding: RowLocationDetailBinding) :
    RecyclerView.ViewHolder(locationBinding.root)


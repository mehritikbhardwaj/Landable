package com.landable.app.ui.home.project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowAmenitiesBinding
import com.landable.app.ui.home.dataModels.Amenitiesmaster

class AmenitiesProjectAdapter(
    private val amenitiesList: ArrayList<Amenitiesmaster>
) :
    RecyclerView.Adapter<AmenitiesHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenitiesHolder {
        val binding: RowAmenitiesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_amenities,
            parent,
            false
        )
        return AmenitiesHolder(binding)
    }

    override fun onBindViewHolder(holder: AmenitiesHolder, position: Int) {
        val amenities = amenitiesList[position]

        holder.amenitiesBinding.tvAmenities.text = amenities.codevalue
        holder.amenitiesBinding.ivAmenity.load(LandableConstants.Image_URL + amenities.icon)
    }

    override fun getItemCount(): Int {
        return amenitiesList.size
    }
}


class AmenitiesHolder(var amenitiesBinding: RowAmenitiesBinding) :
    RecyclerView.ViewHolder(amenitiesBinding.root)
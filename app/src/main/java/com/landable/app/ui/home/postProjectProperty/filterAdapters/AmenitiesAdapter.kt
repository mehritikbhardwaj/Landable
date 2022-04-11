package com.landable.app.ui.home.postProjectProperty.filterAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AmenitiesClickListener
import com.landable.app.databinding.RowAmenitiesPostBinding
import com.landable.app.ui.home.dataModels.Amenitiesmaster

class AmenitiesAdapter(
    private val amenitiesList: ArrayList<Amenitiesmaster>,
    private var amenitiesClickListener: AmenitiesClickListener
) :
    RecyclerView.Adapter<AmenitiesHolder>() {

    private val selectedAmenitiesList: ArrayList<Amenitiesmaster> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenitiesHolder {
        val binding: RowAmenitiesPostBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_amenities_post,
            parent,
            false
        )
        return AmenitiesHolder(binding)
    }

    override fun onBindViewHolder(holder: AmenitiesHolder, position: Int) {
        val amenities = amenitiesList[position]

        holder.amenitiesBinding.tvAmenities.text = amenities.codevalue

        holder.amenitiesBinding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedAmenitiesList.add(amenities)
            } else {
                selectedAmenitiesList.remove(amenities)
            }
            amenitiesClickListener.onAmenitiesSelected("addAmenities", selectedAmenitiesList)
        }
    }

    override fun getItemCount(): Int {
        return amenitiesList.size
    }
}


class AmenitiesHolder(var amenitiesBinding: RowAmenitiesPostBinding) :
    RecyclerView.ViewHolder(amenitiesBinding.root)
package com.landable.app.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.PropertyDetailListener
import com.landable.app.databinding.RowSearchPropertiesBinding
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel

class SearchPropertyAdapter(
    private val propertiesList: ArrayList<FeaturePropertiesDataModel>,
    private var propertyDetailListener: PropertyDetailListener
) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowSearchPropertiesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_search_properties,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val property = propertiesList[position]

        holder.propertiesBinding.ivPropertyImage.load(LandableConstants.Image_URL + property.image1)
        holder.propertiesBinding.tvBathroomCount.text = property.bathroom
        holder.propertiesBinding.tvBedroomCount.text = property.bedroom
        holder.propertiesBinding.tvCategoryName.text = property.categoryname
        holder.propertiesBinding.tvPrice.text = "\u20B9 "+ property.costinword
        holder.propertiesBinding.tvPropertyArea.text = property.totalarea
        holder.propertiesBinding.tvPropertyLocation.text = property.cityname
        holder.propertiesBinding.tvPropertyName.text = property.title
        holder.propertiesBinding.tvSaleType.text = property.saletypename
        holder.propertiesBinding.tvPossessionName.text = property.possessionname
        holder.propertiesBinding.circleImageView.load(LandableConstants.Image_URL + property.profilepic)
        holder.propertiesBinding.tvOwnerName.text = property.name
        holder.propertiesBinding.tvDate.text = property.customertype

        holder.propertiesBinding.llProperyLayout.setOnClickListener {
            propertyDetailListener.onPropertyClick("selectedPropertyDetail", property)
        }

        holder.propertiesBinding.llContactOwner.setOnClickListener {
            propertyDetailListener.onPropertyClick("contactOwner", property)
        }
        holder.propertiesBinding.llProfile.setOnClickListener {
            propertyDetailListener.onPropertyClick("ViewAgencyProfile", property)
        }

    }


    override fun getItemCount(): Int {
        return propertiesList.size
    }
}


class MyViewHolder(var propertiesBinding: RowSearchPropertiesBinding) :
    RecyclerView.ViewHolder(propertiesBinding.root)


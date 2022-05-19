package com.landable.app.ui.home.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.PropertyDetailListener
import com.landable.app.databinding.RowPostedProjectPropertyBinding
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel

class PostedPropertyAdapter(
    private val propertiesList: ArrayList<FeaturePropertiesDataModel>,
    private var propertyDetailListener: PropertyDetailListener
) :
    RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowPostedProjectPropertyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_posted_project_property,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val property = propertiesList[position]

        holder.propertiesBinding.ivPropertyImage.load(LandableConstants.Image_URL + property.image1)
        holder.propertiesBinding.tvBathroomCount.text = property.bathroom.toString()
        holder.propertiesBinding.tvBedroomCount.text = property.bedroom
        holder.propertiesBinding.tvCategoryName.text = property.categoryname
        holder.propertiesBinding.tvPrice.text = "\u20B9 "+ property.costinword
        holder.propertiesBinding.tvPropertyArea.text = property.totalarea
        holder.propertiesBinding.tvPropertyLocation.text = property.cityname
        holder.propertiesBinding.tvPropertyName.text = property.title
        holder.propertiesBinding.tvSaleType.text = property.saletypename
        holder.propertiesBinding.tvPossessionName.text = property.possessionname

        holder.propertiesBinding.llAddProperty.visibility = View.GONE

        holder.propertiesBinding.propertyStrength.text = property.strengthmsg
        if (property.strength > 33 && property.strength < 66) {
            holder.propertiesBinding.firstProgress.progress = 33
            holder.propertiesBinding.secondProgress.progress = property.strength.toInt() - 33
            holder.propertiesBinding.firstProgressImage.visibility = View.VISIBLE
        } else if (property.strength > 66) {
            holder.propertiesBinding.firstProgress.progress = 33
            holder.propertiesBinding.secondProgress.progress = 33
            holder.propertiesBinding.thirdProgress.progress = property.strength.toInt() - 66
            holder.propertiesBinding.secondProgressImage.visibility = View.VISIBLE
            holder.propertiesBinding.firstProgressImage.visibility = View.VISIBLE
        } else {
            holder.propertiesBinding.firstProgress.progress = property.strength.toInt()
            holder.propertiesBinding.firstProgressImage.visibility = View.VISIBLE
        }

        holder.propertiesBinding.startDate.text = property.startdate
        holder.propertiesBinding.endDate.text = property.enddate
        holder.propertiesBinding.tvClick.text = " " + property.clicks.toString()
        holder.propertiesBinding.tvLeads.text = " " + property.leads.toString()

        holder.propertiesBinding.ivPropertyImage.setOnClickListener {
            propertyDetailListener.onPropertyClick("selectedPropertyDetail",property)
        }

        holder.propertiesBinding.ivDelete.setOnClickListener {
            propertyDetailListener.onPropertyClick("deleteSelectedPRoperty",property)
        }

        holder.propertiesBinding.ivEdit.setOnClickListener {
            propertyDetailListener.onPropertyClick("editSelectedProperty",property)
        }
    }

    override fun getItemCount(): Int {
        return propertiesList.size
    }
}

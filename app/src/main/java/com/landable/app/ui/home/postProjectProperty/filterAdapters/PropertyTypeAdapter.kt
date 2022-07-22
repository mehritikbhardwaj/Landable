package com.landable.app.ui.home.postProjectProperty.filterAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.PropertyTypeClickListener
import com.landable.app.databinding.RowCategoriesHomePageBinding
import com.landable.app.ui.home.dataModels.PropertyTypeDataModel

class PropertyTypeAdapter(
    private val propertyTypeList: ArrayList<PropertyTypeDataModel>,
    private var propertyTypeClickListener: PropertyTypeClickListener,
    private var selectedItem:Int,
    private var selectedFilter:String


) :
    RecyclerView.Adapter<MyViewHolder>() {

    private var selectedView: LinearLayout? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowCategoriesHomePageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_categories_home_page,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val propertyType = propertyTypeList[position]

        if (propertyType.id == selectedItem){
            selectedView = holder.categoryBinding.linearLayout3
            changeBackground(selectedView!!)
        }
        if (propertyType.codevalue == selectedFilter){
            selectedView = holder.categoryBinding.linearLayout3
            changeBackground(selectedView!!)
        }

        holder.categoryBinding.tvCategoryName.text = propertyType.codevalue
        holder.categoryBinding.ivCategoryImage.load(LandableConstants.Image_URL + propertyType.icon)

        holder.categoryBinding.linearLayout3.setOnClickListener {
            if (selectedView == null) {
                selectedView = holder.categoryBinding.linearLayout3
                changeBackground(selectedView!!)
                propertyTypeClickListener.onTypeClick("typeClick", propertyType)
            } else if(selectedView == holder.categoryBinding.linearLayout3){
                selectedView!!.setBackgroundResource(R.drawable.categories_bg_home_page)
                selectedView = null
                propertyTypeClickListener.onTypeClick("deleteTypeClick", propertyType)

            } else{
                selectedView!!.setBackgroundResource(R.drawable.categories_bg_home_page)
                changeBackground(
                    holder.categoryBinding.linearLayout3
                )
                selectedView = holder.categoryBinding.linearLayout3
                propertyTypeClickListener.onTypeClick("typeClick", propertyType)

            }
        }

    }

    override fun getItemCount(): Int {
        return propertyTypeList.size
    }
}


private fun changeBackground(layout: LinearLayout) {
    layout.setBackgroundResource(R.drawable.selected_category_drawable)
}


class MyViewHolder(var categoryBinding: RowCategoriesHomePageBinding) :
    RecyclerView.ViewHolder(categoryBinding.root)
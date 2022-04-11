package com.landable.app.ui.home.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.CategoryTypeClickListener
import com.landable.app.databinding.RowCategoriesFilterBinding
import com.landable.app.ui.home.dataModels.CategoriesDataModel

class CategoriesAdapter(
    private val categoriesList: ArrayList<CategoriesDataModel>,
    private var categoryTypeClickListener: CategoryTypeClickListener,
    private var selectedItem:Int
) :
    RecyclerView.Adapter<MyViewHolder>() {

    private var selectedView: LinearLayout? = null
    private var selectedTextView: TextView? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowCategoriesFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_categories_filter,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoriesList[position]

        if (category.id == selectedItem){
            selectedView = holder.categoryBinding.linearLayout3
            selectedTextView = holder.categoryBinding.tvCategoryName
            changeBackground(selectedView!!, selectedTextView!!)
            categoryTypeClickListener.onCategoryClick("categoryClick", category)
        }

        holder.categoryBinding.tvCategoryName.text = category.codevalue
        holder.categoryBinding.linearLayout3.setOnClickListener {
            if (selectedView == null) {
                selectedView = holder.categoryBinding.linearLayout3
                selectedTextView = holder.categoryBinding.tvCategoryName
                changeBackground(selectedView!!, selectedTextView!!)
                categoryTypeClickListener.onCategoryClick("categoryClick", category)
            } else if (selectedView == holder.categoryBinding.linearLayout3) {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                selectedView=null
                categoryTypeClickListener.onCategoryClick("removeCategoryClicked", category)
            }  else {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                changeBackground(
                    holder.categoryBinding.linearLayout3,
                    holder.categoryBinding.tvCategoryName
                )
                selectedView = holder.categoryBinding.linearLayout3
                selectedTextView = holder.categoryBinding.tvCategoryName

                categoryTypeClickListener.onCategoryClick("categoryClick", category)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }
}

private fun changeBackground(layout: LinearLayout, tvCategoryName: TextView) {
    layout.setBackgroundResource(R.drawable.free_home_page_bg)
    tvCategoryName.setTextColor(Color.WHITE)
}


class MyViewHolder(var categoryBinding: RowCategoriesFilterBinding) :
    RecyclerView.ViewHolder(categoryBinding.root)
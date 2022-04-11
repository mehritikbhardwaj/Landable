package com.landable.app.ui.home.postProjectProperty.filterAdapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.databinding.RowCategoriesFilterBinding
import com.landable.app.ui.home.dataModels.Arbitragemaster

class SaletypeAdapter(
    private val salesList: ArrayList<Arbitragemaster>,
    private var agentProfileListener: AgentProfileListener,
    private var isComingFromSale: String,
    private var selectedItem: Int

) :
    RecyclerView.Adapter<ViewHolder>() {

    private var selectedView: LinearLayout? = null
    private var selectedTextView: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowCategoriesFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_categories_filter,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val saleType = salesList[position]

        if (saleType.id == selectedItem) {
            selectedView = holder.categoryBinding.linearLayout3
            selectedTextView = holder.categoryBinding.tvCategoryName
            changeBackground(
                selectedView!!,
                selectedTextView!!
            )
        }

        holder.categoryBinding.tvCategoryName.text = saleType.codevalue
        holder.categoryBinding.linearLayout3.setOnClickListener {
            if (selectedView == null) {
                selectedView = holder.categoryBinding.linearLayout3
                selectedTextView = holder.categoryBinding.tvCategoryName
                changeBackground(
                    selectedView!!,
                    selectedTextView!!
                )
                if (isComingFromSale == "saleTypeClick") {
                    agentProfileListener.onAgentClick("saleTypeClick", saleType.id)
                } else if (isComingFromSale == "possessionCLick") {
                    agentProfileListener.onAgentClick("possessionCLick", saleType.id)
                } else if (isComingFromSale == "furnishedType") {
                    agentProfileListener.onAgentClick("furnishedType", saleType.id)
                } else if (isComingFromSale == "arbitrageClick") {
                    agentProfileListener.onAgentClick("arbitrageClick", saleType.id)
                }
            } else if (selectedView == holder.categoryBinding.linearLayout3) {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                selectedView = null
                if (isComingFromSale == "saleTypeClick") {
                    agentProfileListener.onAgentClick("deleteSaleTypeClick", saleType.id)
                } else if (isComingFromSale == "possessionCLick") {
                    agentProfileListener.onAgentClick("deletePossessionCLick", saleType.id)
                } else if (isComingFromSale == "furnishedType") {
                    agentProfileListener.onAgentClick("deleteFurnishedType", saleType.id)
                } else if (isComingFromSale == "arbitrageClick") {
                    agentProfileListener.onAgentClick("deleteArbitrageClick", saleType.id)
                }
            } else {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                changeBackground(
                    holder.categoryBinding.linearLayout3,
                    holder.categoryBinding.tvCategoryName
                )
                selectedView = holder.categoryBinding.linearLayout3
                selectedTextView = holder.categoryBinding.tvCategoryName
                if (isComingFromSale == "saleTypeClick") {
                    agentProfileListener.onAgentClick("saleTypeClick", saleType.id)
                } else if (isComingFromSale == "possessionCLick") {
                    agentProfileListener.onAgentClick("possessionCLick", saleType.id)
                } else if (isComingFromSale == "furnishedType") {
                    agentProfileListener.onAgentClick("furnishedType", saleType.id)
                } else if (isComingFromSale == "arbitrageClick") {
                    agentProfileListener.onAgentClick("arbitrageClick", saleType.id)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return salesList.size
    }
}


private fun changeBackground(layout: LinearLayout, tvCategoryName: TextView) {
    layout.setBackgroundResource(R.drawable.free_home_page_bg)
    tvCategoryName.setTextColor(Color.WHITE)
}


class ViewHolder(var categoryBinding: RowCategoriesFilterBinding) :
    RecyclerView.ViewHolder(categoryBinding.root)
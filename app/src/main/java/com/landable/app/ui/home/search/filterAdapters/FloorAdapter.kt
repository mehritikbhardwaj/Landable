package com.landable.app.ui.home.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.FloorClickListener
import com.landable.app.databinding.RowCategoriesFilterBinding
import com.landable.app.ui.home.dataModels.Floormaster

class FloorAdapter(
    private val arrayList: ArrayList<Floormaster>,
    private var floorClickListener: FloorClickListener,
    private var selectedFilter:String
) :
    RecyclerView.Adapter<FloorHolder>() {

    private var selectedView: LinearLayout? = null
    private var selectedTextView: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorHolder {
        val bedBAthBinding: RowCategoriesFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_categories_filter,
            parent,
            false
        )
        return FloorHolder(bedBAthBinding)
    }

    override fun onBindViewHolder(holder: FloorHolder, position: Int) {
        val list = arrayList[position]
        /*if (position == 0) {
            selectedView = holder.bedBAthBinding.linearLayout3
            selectedTextView = holder.bedBAthBinding.tvCategoryName
            changeBackground(selectedView!!, holder.bedBAthBinding.tvCategoryName)
        }
        holder.bedBAthBinding.tvCategoryName.text = list.floor
        holder.bedBAthBinding.linearLayout3.setOnClickListener {

            selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
            selectedTextView!!.setTextColor(Color.BLACK)
            changeBackground(
                holder.bedBAthBinding.linearLayout3,
                holder.bedBAthBinding.tvCategoryName
            )
            selectedView = holder.bedBAthBinding.linearLayout3

            floorClickListener.onFloorClick("floor", list)

        }*/

        if (list.floorvalue == selectedFilter){
            selectedView = holder.bedBAthBinding.linearLayout3
            selectedTextView = holder.bedBAthBinding.tvCategoryName
            changeBackground(
                selectedView!!,
                selectedTextView!!
            )
        }

        holder.bedBAthBinding.tvCategoryName.text = list.floor
        holder.bedBAthBinding.linearLayout3.setOnClickListener {
            if (selectedView == null) {
                selectedView = holder.bedBAthBinding.linearLayout3
                selectedTextView = holder.bedBAthBinding.tvCategoryName
                changeBackground(
                    selectedView!!,
                    selectedTextView!!
                )
                floorClickListener.onFloorClick("floor", list)
            } else if (selectedView == holder.bedBAthBinding.linearLayout3) {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                selectedView=null
                floorClickListener.onFloorClick("Nofloor", list)
            }  else {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                changeBackground(
                    holder.bedBAthBinding.linearLayout3,
                    holder.bedBAthBinding.tvCategoryName
                )
                selectedView = holder.bedBAthBinding.linearLayout3
                selectedTextView = holder.bedBAthBinding.tvCategoryName

                floorClickListener.onFloorClick("floor", list)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}

private fun changeBackground(layout: LinearLayout, tvCategoryName: TextView) {
    layout.setBackgroundResource(R.drawable.free_home_page_bg)
    tvCategoryName.setTextColor(Color.WHITE)
}


class FloorHolder(var bedBAthBinding: RowCategoriesFilterBinding) :
    RecyclerView.ViewHolder(bedBAthBinding.root)
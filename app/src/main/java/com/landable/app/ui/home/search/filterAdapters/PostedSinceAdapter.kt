package com.landable.app.ui.home.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.PostedDateClickListener
import com.landable.app.databinding.RowCategoriesFilterBinding
import com.landable.app.ui.home.dataModels.BedsDataModel

class PostedSinceAdapter(
    private val arrayList: ArrayList<BedsDataModel>,
    private var postedDateClickListener: PostedDateClickListener,
) :
    RecyclerView.Adapter<PostedHolder>() {

    private var selectedView: LinearLayout? = null
    private var selectedTextView: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostedHolder {
        val bedBAthBinding: RowCategoriesFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_categories_filter,
            parent,
            false
        )
        return PostedHolder(bedBAthBinding)
    }

    override fun onBindViewHolder(holder: PostedHolder, position: Int) {
        val list = arrayList[position]
        holder.bedBAthBinding.tvCategoryName.text = list.value
        holder.bedBAthBinding.linearLayout3.setOnClickListener {
            if (selectedView == null) {
                selectedView = holder.bedBAthBinding.linearLayout3
                selectedTextView = holder.bedBAthBinding.tvCategoryName
                changeBackground(
                    selectedView!!,
                    selectedTextView!!
                )
                postedDateClickListener.onPostedDateClick("postedDate", list.value)
            } else if (selectedView == holder.bedBAthBinding.linearLayout3) {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                selectedView=null
                postedDateClickListener.onPostedDateClick("removepostedDate", list.value)
            }  else {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                changeBackground(
                    holder.bedBAthBinding.linearLayout3,
                    holder.bedBAthBinding.tvCategoryName
                )
                selectedView = holder.bedBAthBinding.linearLayout3
                selectedTextView = holder.bedBAthBinding.tvCategoryName

                postedDateClickListener.onPostedDateClick("postedDate", list.value)
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


class PostedHolder(var bedBAthBinding: RowCategoriesFilterBinding) :
    RecyclerView.ViewHolder(bedBAthBinding.root)
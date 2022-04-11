package com.landable.app.ui.home.homeUI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.LandableConstants
import com.landable.app.common.WhyLandableClickListener
import com.landable.app.databinding.WhyLandableRowHomePageBinding
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.dataModels.WhyLandableDataModel

class WhyLandableAdapter(
    private val whyLandableList: ArrayList<WhyLandableDataModel>,
    private val whyLandableClickListener: WhyLandableClickListener
) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: WhyLandableRowHomePageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.why_landable_row_home_page,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val whyLandable = whyLandableList[position]

        holder.whyLandableBinding.tvText.text = whyLandable.pages
        holder.whyLandableBinding.ivIcon.load(LandableConstants.Image_URL + whyLandable.appicon)

        holder.whyLandableBinding.ivDetailsIcon.setOnClickListener {
            CustomAlertDialog(
                AppInfo.getContext(),
                whyLandable.pages,
                whyLandable.shortdescription
            ).show()
        }
        holder.whyLandableBinding.llWhyLandable.setOnClickListener {
            whyLandableClickListener.onWhyLandableClick("itemClicked", whyLandable)
        }

    }

    override fun getItemCount(): Int {
        return whyLandableList.size
    }
}

class MyViewHolder(var whyLandableBinding: WhyLandableRowHomePageBinding) :
    RecyclerView.ViewHolder(whyLandableBinding.root)
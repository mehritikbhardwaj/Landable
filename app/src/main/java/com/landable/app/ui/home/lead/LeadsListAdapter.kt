package com.landable.app.ui.home.lead

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.LeadsClickListener
import com.landable.app.databinding.RowLeadsBinding
import com.landable.app.ui.home.dataModels.LeadsDataModel

class LeadsListAdapter(private val leadsList: ArrayList<LeadsDataModel>,
                       private var context: Context,
                       private var leadsClickListener:LeadsClickListener
) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowLeadsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_leads,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leads = leadsList[position]

       /* Glide.with(context)
            .load(LandableConstants.Image_URL + leads.Image1)
            .into( holder.leadsRowBinding.ivImage)*/

        holder.leadsRowBinding.ivImage.load(LandableConstants.Image_URL + leads.Image1)
        holder.leadsRowBinding.tvName.text = leads.title
        holder.leadsRowBinding.tvAddedBy.text = "Name: " + leads.name
        holder.leadsRowBinding.tvEmail.text = "Email: " + leads.email
        holder.leadsRowBinding.tvMobile.text = "Mobile: " + leads.mobile
        holder.leadsRowBinding.tvOwnerName.text = leads.name
        holder.leadsRowBinding.tvDate.text = leads.dated
        holder.leadsRowBinding.circleImageView.load(LandableConstants.Image_URL + leads.logo)

        holder.leadsRowBinding.llChat.setOnClickListener {
            leadsClickListener.onLeadsClick("chat",leads)
        }

        holder.leadsRowBinding.llLeads.setOnClickListener {
            leadsClickListener.onLeadsClick("openLeadDetail",leads)
        }
        holder.leadsRowBinding.contact.setOnClickListener {
            leadsClickListener.onLeadsClick("call",leads)

        }
    }

    override fun getItemCount(): Int {
        return leadsList.size
    }
}


class MyViewHolder(var leadsRowBinding: RowLeadsBinding) :
    RecyclerView.ViewHolder(leadsRowBinding.root)
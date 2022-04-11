package com.landable.app.ui.home.property.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AdvertisementClickListener
import com.landable.app.databinding.RowAdvertisementsBinding
import com.landable.app.ui.home.dataModels.Advertisment

class AdvertisementAdapter(
    private val advertisementList: ArrayList<Advertisment>,
    private var advertisementClickListener: AdvertisementClickListener
) : RecyclerView.Adapter<AdvertisementViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertisementViewHolder {
        val binding: RowAdvertisementsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_advertisements,
            parent,
            false
        )
        return AdvertisementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertisementViewHolder, position: Int) {
        val advertisment = advertisementList[position]


        holder.advertisementsBinding.ivAdvertisement.load(advertisment.image)
        holder.advertisementsBinding.llAd.setOnClickListener {
            advertisementClickListener.onAdvertisemntClick("advertisementClick", advertisment)
        }
    }

    override fun getItemCount(): Int {
        return advertisementList.size
    }
}


class AdvertisementViewHolder(var advertisementsBinding: RowAdvertisementsBinding) :
    RecyclerView.ViewHolder(advertisementsBinding.root)


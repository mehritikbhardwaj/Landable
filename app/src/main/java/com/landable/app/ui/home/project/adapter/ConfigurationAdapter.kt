package com.landable.app.ui.home.project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.ConfigurationImageClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowConfigurationBinding
import com.landable.app.ui.home.dataModels.Projectconfiguration

class ConfigurationAdapter(
    private val configurationArray: ArrayList<Projectconfiguration>,
    private val configurationImageClickListener: ConfigurationImageClickListener
) :
    RecyclerView.Adapter<ConfigurationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigurationHolder {
        val binding: RowConfigurationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_configuration,
            parent,
            false
        )
        return ConfigurationHolder(binding)
    }

    override fun onBindViewHolder(holder: ConfigurationHolder, position: Int) {
        val configuration = configurationArray[position]

        holder.configurationBinding.ivImage.load(LandableConstants.Image_URL + configuration.uimage)
        holder.configurationBinding.tvUnitType.text = configuration.utype
        holder.configurationBinding.tvUnitSize.text = configuration.usize
        holder.configurationBinding.tvPrice.text = "\u20B9 "+ configuration.uprice2
        holder.configurationBinding.ivImage.setOnClickListener {
            configurationImageClickListener.onImageClick("imageClick",configuration)
        }
    }

    override fun getItemCount(): Int {
        return configurationArray.size
    }
}

class ConfigurationHolder(var configurationBinding: RowConfigurationBinding) :
    RecyclerView.ViewHolder(configurationBinding.root)
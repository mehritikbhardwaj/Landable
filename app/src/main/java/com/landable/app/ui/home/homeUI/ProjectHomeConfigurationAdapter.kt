package com.landable.app.ui.home.homeUI

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.databinding.RowSizeFlatLayoutBinding

class ProjectHomeConfigurationAdapter(
    private val configurationArray: Array<String>
) :
    RecyclerView.Adapter<ConfigurationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigurationViewHolder {
        val binding: RowSizeFlatLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_size_flat_layout,
            parent,
            false
        )
        return ConfigurationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConfigurationViewHolder, position: Int) {
        val configuration = configurationArray[position]

        holder.configurationBinding.tvConfiguration.text =Html.fromHtml(configuration)

    }

    override fun getItemCount(): Int {
        return configurationArray.size
    }
}


class ConfigurationViewHolder(var configurationBinding: RowSizeFlatLayoutBinding) :
    RecyclerView.ViewHolder(configurationBinding.root)
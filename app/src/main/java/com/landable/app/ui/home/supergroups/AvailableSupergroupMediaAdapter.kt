package com.landable.app.ui.home.supergroups

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowSelectedPropertyImageBinding
import com.landable.app.ui.home.dataModels.Propertyimage
import com.landable.app.ui.home.dataModels.SupergroupMediaModel

class AvailableSupergroupMediaAdapter (
    private val imagesList: ArrayList<SupergroupMediaModel>,
    private val deleteListener:AgentProfileListener
) :
    RecyclerView.Adapter<ImagesHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
        val binding: RowSelectedPropertyImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_selected_property_image,
            parent,
            false
        )
        return ImagesHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val images = imagesList[position]

        holder.imageBinding.ivSelectedImage.load(LandableConstants.Image_URL + images.path)
        holder.imageBinding.ivDelete.setOnClickListener {
            deleteListener.onAgentClick("delete",images.id)
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}


class ImagesHolder(var imageBinding: RowSelectedPropertyImageBinding) :
    RecyclerView.ViewHolder(imageBinding.root)
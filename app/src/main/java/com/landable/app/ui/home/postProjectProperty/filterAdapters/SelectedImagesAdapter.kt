package com.landable.app.ui.home.postProjectProperty.filterAdapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.databinding.RowSelectedPropertyImageBinding

class SelectedImagesAdapter(
    private val imagesList: ArrayList<String>
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

        holder.imageBinding.ivSelectedImage.load(Uri.parse(images))
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}


class ImagesHolder(var imageBinding: RowSelectedPropertyImageBinding) :
    RecyclerView.ViewHolder(imageBinding.root)
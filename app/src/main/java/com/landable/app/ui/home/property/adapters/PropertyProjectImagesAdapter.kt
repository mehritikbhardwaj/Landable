package com.landable.app.ui.home.property.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.DocumentClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowPropertyImagesBinding
import com.landable.app.ui.home.dataModels.Propertyimage

class PropertyProjectImagesAdapter(
    private val imagesList: ArrayList<Propertyimage>,
    private var documentClickListener: DocumentClickListener

) :
    RecyclerView.Adapter<ImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding: RowPropertyImagesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_property_images,
            parent,
            false
        )
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = imagesList[position]

        holder.imagesRowBinding.ivImage.load(LandableConstants.Image_URL + image.imagepath)
        holder.imagesRowBinding.tvImageType.text = image.imagetype

        holder.imagesRowBinding.llDocument.setOnClickListener {
            documentClickListener.onImageClick("imageClick", image)
        }
        /* holder.agentsRowBinding.ivAgentImage.load(LandableConstants.Image_URL + agent.profilepic)
         holder.agentsRowBinding.tvAgentName.text = agent.name
         holder.agentsRowBinding.tvAgentEmail.text = agent.email
         holder.agentsRowBinding.tvMobile.text = agent.mobile
         holder.agentsRowBinding.tvLocation.text = agent.cityname
         holder.agentsRowBinding.ivEditDetail.setOnClickListener {
             holder.agentsRowBinding.ivEditDetail.visibility = View.GONE
         }
         holder.agentsRowBinding.ivDelete.setOnClickListener {
             holder.agentsRowBinding.ivEditDetail.visibility = View.VISIBLE
         }

         holder.agentsRowBinding.llAgentProfile.setOnClickListener {
             agentClickListener.onAgentClick("selectedAgentProfile", agent.userid)
         }*/

    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}


class ImagesViewHolder(var imagesRowBinding: RowPropertyImagesBinding) :
    RecyclerView.ViewHolder(imagesRowBinding.root)
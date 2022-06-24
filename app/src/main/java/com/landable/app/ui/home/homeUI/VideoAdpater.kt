package com.landable.app.ui.home.homeUI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.VideoClickListener
import com.landable.app.databinding.RowBlogsBinding

class VideoAdpater(
    private val videosArray: ArrayList<DemoVideoDataModel>,
    private var videoClickListener: VideoClickListener

) :
    RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding: RowBlogsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_blogs,
            parent,
            false
        )
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val videos = videosArray[position]

        holder.blogsBinding.tvDate.text = videos.createddate
        holder.blogsBinding.tvHeading.text = videos.title
        holder.blogsBinding.ivImage.load(LandableConstants.Image_URL + videos.coverpage)


        holder.blogsBinding.llBlogs.setOnClickListener {
            videoClickListener.onvideoClick("click", videos.videopath)
        }
    }

    override fun getItemCount(): Int {
        return videosArray.size
    }
}

class NewsHolder(var blogsBinding: RowBlogsBinding) :
    RecyclerView.ViewHolder(blogsBinding.root)
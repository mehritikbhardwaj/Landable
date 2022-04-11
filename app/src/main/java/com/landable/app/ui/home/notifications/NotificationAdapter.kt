package com.landable.app.ui.home.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.databinding.RowBlogsBinding
import com.landable.app.ui.home.dataModels.NotificationsDataModelItem

class NotificationAdapter(
    private val notificationArray: ArrayList<NotificationsDataModelItem>
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
        val notification = notificationArray[position]

        holder.blogsBinding.tvDate.text = notification.CreatedDate
        holder.blogsBinding.tvHeading.text = notification.NotificatonTitle
        holder.blogsBinding.ivImage.load(R.drawable.icon)

    }

    override fun getItemCount(): Int {
        return notificationArray.size
    }
}

class NewsHolder(var blogsBinding: RowBlogsBinding) :
    RecyclerView.ViewHolder(blogsBinding.root)
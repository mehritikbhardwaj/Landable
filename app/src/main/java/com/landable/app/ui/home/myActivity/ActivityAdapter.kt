package com.landable.app.ui.home.myActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.ActivityClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowActivitiesBinding
import com.landable.app.ui.home.dataModels.ActivityDataModel

class ActivityAdapter(
    private val blogsArray: ArrayList<ActivityDataModel>,
    private val onActivityClick:ActivityClickListener
) :
    RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding: RowActivitiesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_activities,
            parent,
            false
        )
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val blogs = blogsArray[position]

        holder.blogsBinding.tvNarration.text = blogs.narration
        holder.blogsBinding.tvTitle.text = blogs.title
        holder.blogsBinding.date.text = blogs.dated + " | " + blogs.narration
        holder.blogsBinding.ivImage.load(LandableConstants.Image_URL + blogs.Image1)

        holder.blogsBinding.llActivity.setOnClickListener {
            onActivityClick.onActivityClick("activityClick",blogs)
        }

    }

    override fun getItemCount(): Int {
        return blogsArray.size
    }
}

class NewsHolder(var blogsBinding: RowActivitiesBinding) :
    RecyclerView.ViewHolder(blogsBinding.root)
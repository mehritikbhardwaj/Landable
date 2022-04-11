package com.landable.app.ui.home.blogs

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.BlogsClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.common.NewsClickListener
import com.landable.app.databinding.RowBlogsBinding
import com.landable.app.ui.home.dataModels.BlogDataModel
import com.landable.app.ui.home.dataModels.NewsDataModelItem

class BlogsAdapter(
    private val blogsArray: ArrayList<BlogDataModel>,
    private var blogsClickListener: BlogsClickListener

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
        val blogs = blogsArray[position]

        holder.blogsBinding.tvDate.text = blogs.bdate
        holder.blogsBinding.tvHeading.text = blogs.heading
        holder.blogsBinding.ivImage.load(LandableConstants.Image_URL + blogs.image)


        holder.blogsBinding.llBlogs.setOnClickListener {
            blogsClickListener.onBlogClick("blogClick", blogs)
        }
    }

    override fun getItemCount(): Int {
        return blogsArray.size
    }
}

class NewsHolder(var blogsBinding: RowBlogsBinding) :
    RecyclerView.ViewHolder(blogsBinding.root)
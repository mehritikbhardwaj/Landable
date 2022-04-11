package com.landable.app.ui.home.news

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.NewsClickListener
import com.landable.app.databinding.RowNewsBinding
import com.landable.app.ui.home.dataModels.NewsDataModelItem

class NewsListAdapter(
    private val newsArray: ArrayList<NewsDataModelItem>,
    private var newsClickListener: NewsClickListener

) :
    RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding: RowNewsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_news,
            parent,
            false
        )
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news = newsArray[position]

        holder.newsBinding.newsHeading.text = Html.fromHtml(news.title)
        holder.newsBinding.newsDescription.text = Html.fromHtml(news.content)


        holder.newsBinding.llNews.setOnClickListener {
            newsClickListener.onNewsClick("newsClick", news)
        }
    }

    override fun getItemCount(): Int {
        return newsArray.size
    }
}

class NewsHolder(var newsBinding: RowNewsBinding) :
    RecyclerView.ViewHolder(newsBinding.root)
package com.landable.app.ui.home.shortUrl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.ShortURLClickListener
import com.landable.app.databinding.RowShortUrlsBinding
import com.landable.app.ui.home.dataModels.ShortUrlDataModelItem

class ShortUrlListAdapter(
    private val shortUrlList: ArrayList<ShortUrlDataModelItem>,
    private val shortURLClickListener: ShortURLClickListener
) :
    RecyclerView.Adapter<ShortUrlViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortUrlViewHolder {
        val binding: RowShortUrlsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_short_urls,
            parent,
            false
        )
        return ShortUrlViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShortUrlViewHolder, position: Int) {
        val shortUrl = shortUrlList[position]

        holder.shortUrlRowBinding.tvBaseURl.text = shortUrl.shorturl
        holder.shortUrlRowBinding.rUrl.text = shortUrl.rurl

        holder.shortUrlRowBinding.ivDelete.setOnClickListener {
            shortURLClickListener.onShortUrlClick("deleteButton",shortUrl)
        }

    }

    override fun getItemCount(): Int {
        return shortUrlList.size
    }
}


class ShortUrlViewHolder(var shortUrlRowBinding: RowShortUrlsBinding) :
    RecyclerView.ViewHolder(shortUrlRowBinding.root)
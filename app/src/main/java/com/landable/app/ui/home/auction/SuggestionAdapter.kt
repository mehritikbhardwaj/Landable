package com.landable.app.ui.home.auction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.SavedSearchListener
import com.landable.app.common.VideoClickListener
import com.landable.app.databinding.LayoutSuggestionsBinding
import com.landable.app.databinding.RowBlogsBinding
import com.landable.app.ui.home.homeUI.DemoVideoDataModel

class SuggestionAdapter (
    private val suggestionArray: ArrayList<AuctionSuggestionModel>,
    private val searchListener: VideoClickListener
) :
    RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding: LayoutSuggestionsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_suggestions,
            parent,
            false
        )
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val suggestion = suggestionArray[position]

        holder.suggesitonBinding.tvLocation.text = suggestion.value

        holder.suggesitonBinding.llLocation.setOnClickListener {
            searchListener.onvideoClick("clicked",suggestion.value)
        }
    }

    override fun getItemCount(): Int {
        return suggestionArray.size
    }
}

class NewsHolder(var suggesitonBinding: LayoutSuggestionsBinding) :
    RecyclerView.ViewHolder(suggesitonBinding.root)
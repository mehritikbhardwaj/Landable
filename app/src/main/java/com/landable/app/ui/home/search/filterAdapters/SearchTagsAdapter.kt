package com.landable.app.ui.home.search.filterAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.FilterDismissListener
import com.landable.app.common.SavedSearchListener
import com.landable.app.databinding.SearchTagsLayoutBinding
import com.landable.app.ui.home.search.SearchTagModel

class SearchTagsAdapter(
    private val searchTagsArray: ArrayList<SearchTagModel>,
    private val listener: FilterDismissListener
) :
    RecyclerView.Adapter<SearchTagsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTagsHolder {
        val binding: SearchTagsLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.search_tags_layout,
            parent,
            false
        )
        return SearchTagsHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchTagsHolder, position: Int) {
        val searchTag = searchTagsArray[position]

        holder.savedSearchBinding.filter.text = searchTag.text

        holder.savedSearchBinding.dismiss.setOnClickListener {
            listener.onDismissCLick(searchTag.type)
        }
    }

    override fun getItemCount(): Int {
        return searchTagsArray.size
    }
}

class SearchTagsHolder(var savedSearchBinding: SearchTagsLayoutBinding) :
    RecyclerView.ViewHolder(savedSearchBinding.root)
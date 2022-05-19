package com.landable.app.ui.home.savedSearches

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.SavedSearchListener
import com.landable.app.databinding.RowSavedSearchBinding
import com.landable.app.ui.home.dataModels.SavedSearchDataModelItem

class SavedSearchAdapter(
    private val savedSearchesArray: ArrayList<SavedSearchDataModelItem>,
    private var clickListener: SavedSearchListener
) :
    RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding: RowSavedSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_saved_search,
            parent,
            false
        )
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val savedSearches = savedSearchesArray[position]

        holder.savedSearchBinding.saveType.text = savedSearches.searchtype
        holder.savedSearchBinding.saveName.text = savedSearches.searchname

        holder.savedSearchBinding.ivDelete.setOnClickListener {
            clickListener.onClick("delete",savedSearches)
        }
    }

    override fun getItemCount(): Int {
        return savedSearchesArray.size
    }
}

class NewsHolder(var savedSearchBinding: RowSavedSearchBinding) :
    RecyclerView.ViewHolder(savedSearchBinding.root)
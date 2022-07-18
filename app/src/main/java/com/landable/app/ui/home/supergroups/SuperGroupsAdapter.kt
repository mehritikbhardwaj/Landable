package com.landable.app.ui.home.supergroups

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.SupergroupClickListener
import com.landable.app.databinding.RowSuperGroupsBinding
import com.landable.app.ui.home.dataModels.SuperGroupsDataModelItem

class SuperGroupsAdapter(
    private val superGroupsArray: ArrayList<SuperGroupsDataModelItem>,
    private val supergroupListener: SupergroupClickListener
) :
    RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding: RowSuperGroupsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_super_groups,
            parent,
            false
        )
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val superGroups = superGroupsArray[position]

        holder.supergroupsBinding.tvTitle.text = superGroups.title
        holder.supergroupsBinding.tvDescription.text = superGroups.description
        holder.supergroupsBinding.tvCategoryName.text = superGroups.categoryname
        holder.supergroupsBinding.tvSalesTypeName.text = superGroups.saletypename
        holder.supergroupsBinding.tvPossessionName.text = superGroups.possessionname
        holder.supergroupsBinding.tvAddress.text = superGroups.locality
        holder.supergroupsBinding.tvSubCategoryName.text = superGroups.subcategoryname
        holder.supergroupsBinding.tvArbitrage.text = Html.fromHtml(superGroups.arbitragebadge)
        holder.supergroupsBinding.tvComment.text = superGroups.comment.toString() + "Comment"
        holder.supergroupsBinding.tvViews.text = superGroups.Viewed.toString() + "Views"
        holder.supergroupsBinding.tvTime.text = superGroups.postedsince

        holder.supergroupsBinding.deleteSupergroup.setOnClickListener {
            supergroupListener.onClickSuperGroup("deleteSupergroup",superGroups)
        }
        holder.supergroupsBinding.chatThread.setOnClickListener {
            supergroupListener.onClickSuperGroup("chatSupergroup",superGroups)
        }
        holder.supergroupsBinding.edit.setOnClickListener {
            supergroupListener.onClickSuperGroup("edit",superGroups)

        }

        holder.supergroupsBinding.llChatBox.setOnClickListener {
            supergroupListener.onClickSuperGroup("detail",superGroups)

        }


    }

    override fun getItemCount(): Int {
        return superGroupsArray.size
    }
}

class NewsHolder(var supergroupsBinding: RowSuperGroupsBinding) :
    RecyclerView.ViewHolder(supergroupsBinding.root)
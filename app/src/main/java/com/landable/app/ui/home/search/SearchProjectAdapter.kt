package com.landable.app.ui.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.ProjectDetailListener
import com.landable.app.databinding.RowSearchProjectsBinding
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.homeUI.ProjectHomeConfigurationAdapter

class SearchProjectAdapter(
    private var projetsList: ArrayList<ProjectsDataModel>,
    private var projectDetailListener: ProjectDetailListener,
    private val context: Context
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RowSearchProjectsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_search_projects,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val projects = projetsList[position]

        holder.projectsBinding.ivPropertyImage.load(LandableConstants.Image_URL + projects.image1)
        holder.projectsBinding.tvCategoryName.text = projects.categoryname
        holder.projectsBinding.tvPrice.text =
            "\u20B9 "+  projetsList[position].mincost + " -  " + projects.maxcost
        holder.projectsBinding.tvPropertyArea.text = projects.Fulladdress
        holder.projectsBinding.tvPropertyName.text = projects.projectname
        holder.projectsBinding.tvSaleType.text = projects.subcategoryname
        holder.projectsBinding.tvPossessionName.text = projects.possessionname


        val myArray: Array<String> = projects.configuration.split(",").toTypedArray()

        holder.projectsBinding.rvConfiguration.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.projectsBinding.rvConfiguration.adapter = ProjectHomeConfigurationAdapter(myArray)


        holder.projectsBinding.llProjectLayout.setOnClickListener {
            projectDetailListener.onProjectClick("selectedProjectDetail",projects)
        }
    }

    override fun getItemCount(): Int {
        return projetsList.size
    }
}

class ViewHolder(var projectsBinding: RowSearchProjectsBinding) :
    RecyclerView.ViewHolder(projectsBinding.root)

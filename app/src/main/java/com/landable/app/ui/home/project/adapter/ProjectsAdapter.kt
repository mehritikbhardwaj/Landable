package com.landable.app.ui.home.project.adapter

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
import com.landable.app.databinding.RowFeaturedProjectsHomeBinding
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.homeUI.ProjectHomeConfigurationAdapter
import java.util.*

class ProjectsAdapter(
    private val projectsList: ArrayList<ProjectsDataModel>,
    private var projectDetailListener: ProjectDetailListener,
    private val context:Context
) :
    RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowFeaturedProjectsHomeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_featured_projects_home,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val project = projectsList[position]

        holder.projectBinding.ivPropertyImage.load(LandableConstants.Image_URL + project.image1)
        holder.projectBinding.tvCategoryName.text = project.categoryname
        holder.projectBinding.tvPrice.text = "\u20B9 " + project.mincost + " -  " + project.maxcost
        holder.projectBinding.tvPropertyArea.text = project.Fulladdress
        holder.projectBinding.tvPropertyName.text = project.projectname
        holder.projectBinding.tvSaleType.text = project.subcategoryname
        holder.projectBinding.tvPossessionName.text = project.possessionname

        val myArray: Array<String> = project.configuration.split(",").toTypedArray()

        holder.projectBinding.rvConfiguration.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.projectBinding.rvConfiguration.adapter = ProjectHomeConfigurationAdapter(myArray)


        holder.projectBinding.llProjectLayout.setOnClickListener {
            projectDetailListener.onProjectClick("selectedProjectDetail", project)
        }
    }

    override fun getItemCount(): Int {
        return projectsList.size
    }
}


class MyViewHolder(var projectBinding: RowFeaturedProjectsHomeBinding) :
    RecyclerView.ViewHolder(projectBinding.root)
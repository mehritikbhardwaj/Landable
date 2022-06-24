package com.landable.app.ui.home.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.common.ProjectDetailListener
import com.landable.app.databinding.RowPostedProjectPropertyBinding
import com.landable.app.ui.home.dataModels.ProjectsDataModel

class PostedProjectAdapter(
    private val projectsList: ArrayList<ProjectsDataModel>,
    private var projectDetailListener: ProjectDetailListener
) :
    RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowPostedProjectPropertyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_posted_project_property,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val project = projectsList[position]

        holder.propertiesBinding.Status.text = "Status: "+project.Status

        holder.propertiesBinding.ivPropertyImage.load(LandableConstants.Image_URL + project.image1)
        holder.propertiesBinding.tvPossessionName.text = project.possessionname
        holder.propertiesBinding.llBathroomCOunt.visibility = View.GONE
        holder.propertiesBinding.line.visibility = View.GONE
        holder.propertiesBinding.tvPropertyName.text = project.projectname
        holder.propertiesBinding.linearLayout2.visibility = View.GONE
        holder.propertiesBinding.linearLayout3.visibility = View.GONE
        holder.propertiesBinding.tvCategoryName.text = project.categoryname
        holder.propertiesBinding.tvPropertyLocation.text = project.Fulladdress
        holder.propertiesBinding.propertyStrength.text = project.strengthmsg
        if (project.strength > 33 && project.strength < 66) {
            holder.propertiesBinding.firstProgress.progress = 33
            holder.propertiesBinding.secondProgress.progress = project.strength.toInt() - 33
            holder.propertiesBinding.firstProgressImage.visibility = View.VISIBLE
        } else if (project.strength > 66) {
            holder.propertiesBinding.firstProgress.progress = 33
            holder.propertiesBinding.secondProgress.progress = 33
            holder.propertiesBinding.thirdProgress.progress = project.strength.toInt() - 66
            holder.propertiesBinding.secondProgressImage.visibility = View.VISIBLE
            holder.propertiesBinding.firstProgressImage.visibility = View.VISIBLE
        } else {
            holder.propertiesBinding.firstProgress.progress = project.strength.toInt()
            holder.propertiesBinding.firstProgressImage.visibility = View.VISIBLE
        }
        holder.propertiesBinding.startDate.text = project.startdate
        holder.propertiesBinding.endDate.text = project.enddate
        holder.propertiesBinding.tvClick.text = " " + project.clicks.toString()
        holder.propertiesBinding.tvLeads.text = " " + project.leads.toString()

        holder.propertiesBinding.tvPrice.text =
            "\u20B9 " + project.mincost + " - " + project.maxcost


        holder.propertiesBinding.llProperyLayout.setOnClickListener {
            projectDetailListener.onProjectClick("selectedProjectDetail", project)
        }

        holder.propertiesBinding.ivDelete.setOnClickListener {
            projectDetailListener.onProjectClick("deleteSelectedPRoject", project)
        }
        holder.propertiesBinding.ivEdit.setOnClickListener {
            projectDetailListener.onProjectClick("editSelectedPRoject", project)
        }
        holder.propertiesBinding.llAddProperty.setOnClickListener {
            projectDetailListener.onProjectClick("addProperty", project)
        }
    }

    override fun getItemCount(): Int {
        return projectsList.size
    }
}


class MyViewHolder(var propertiesBinding: RowPostedProjectPropertyBinding) :
    RecyclerView.ViewHolder(propertiesBinding.root)


package com.landable.app.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowSearchAgentsBinding
import com.landable.app.ui.home.dataModels.Profile

class SearchAgencyAdapter(
    private val agencyList: ArrayList<Profile>,
    private val agentProfileListener: AgentProfileListener
) :
    RecyclerView.Adapter<AgencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgencyViewHolder {
        val binding: RowSearchAgentsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_search_agents,
            parent,
            false
        )
        return AgencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgencyViewHolder, position: Int) {
        val agency = agencyList[position]

        holder.agencyBinding.tvSaleCount.text = "For Sale : " + agency.salecount
        holder.agencyBinding.tvOperatingSince.text =
            "Operating since " + agency.Operatingsince.toString()
        holder.agencyBinding.tvSaleCost.text =
            "Price Range :" + "\u20B9 " + agency.salemincost + " - " + agency.salemaxcost

        holder.agencyBinding.tvRentCount.text = "For Rent : " + agency.rentcount
        holder.agencyBinding.tvRentCost.text =
            "Price Range :" + "\u20B9 " + agency.rentmincost + " - " + agency.rentmaxcost

        holder.agencyBinding.ivProfilePicture.load(LandableConstants.Image_URL + agency.profilepic)

        holder.agencyBinding.tvAgencyName.text = agency.agencyname
        holder.agencyBinding.tvName.text = agency.name
        holder.agencyBinding.tvLocation.text = agency.cityname
           holder.agencyBinding.llAgencyProfile.setOnClickListener {
               agentProfileListener.onAgentClick("agencyID", agency.userid)
           }

        holder.agencyBinding.ivChat.setOnClickListener {
            agentProfileListener.onAgentClick("chatClicked",agency.userid)
        }
    }


    override fun getItemCount(): Int {
        return agencyList.size
    }
}


class AgencyViewHolder(var agencyBinding: RowSearchAgentsBinding) :
    RecyclerView.ViewHolder(agencyBinding.root)


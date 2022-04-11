package com.landable.app.ui.home.agent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowAgentsProfileBinding
import com.landable.app.ui.home.dataModels.UserProfileDataModel

class AgentsListAdapter(
    private val agentsList: ArrayList<UserProfileDataModel>,
    private var agentClickListener: AgentProfileListener

) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowAgentsProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_agents_profile,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val agent = agentsList[position]

        holder.agentsRowBinding.ivAgentImage.load(LandableConstants.Image_URL + agent.profilepic)
        holder.agentsRowBinding.tvAgentName.text = agent.name
        holder.agentsRowBinding.tvAgentEmail.text = agent.email
        holder.agentsRowBinding.tvMobile.text = agent.mobile
        holder.agentsRowBinding.tvLocation.text = agent.cityname


        holder.agentsRowBinding.ivDelete.setOnClickListener {
            agentClickListener.onAgentClick("deleteAgent", agent.userid)
        }
        holder.agentsRowBinding.llAgentProfile.setOnClickListener {
            agentClickListener.onAgentClick("selectedAgentProfile", agent.userid)
        }
        holder.agentsRowBinding.ivEditDetail.setOnClickListener{
            agentClickListener.onAgentClick("editSelectedAgent", agent.userid)
        }

    }

    override fun getItemCount(): Int {
        return agentsList.size
    }
}


class MyViewHolder(var agentsRowBinding: RowAgentsProfileBinding) :
    RecyclerView.ViewHolder(agentsRowBinding.root)
package com.landable.app.ui.home.agent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AgencyActivityClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowAgentActivityBinding
import com.landable.app.ui.home.dataModels.Useractivity

class AgentActivityAdapter(
    private val useractivityArray: ArrayList<Useractivity>,
    private var activityClickListener: AgencyActivityClickListener

) :
    RecyclerView.Adapter<AgentActivityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentActivityHolder {
        val binding: RowAgentActivityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_agent_activity,
            parent,
            false
        )
        return AgentActivityHolder(binding)
    }

    override fun onBindViewHolder(holder: AgentActivityHolder, position: Int) {
        val activity = useractivityArray[position]

        holder.agentsActivityBinding.ivPropertyImage.load(LandableConstants.Image_URL + activity.Image1)
        holder.agentsActivityBinding.tvTitle.text = activity.title
        holder.agentsActivityBinding.tvNarration.text = activity.narration
        holder.agentsActivityBinding.tvDate.text = activity.dated

        holder.agentsActivityBinding.llActivity.setOnClickListener {
            activityClickListener.onActivityClick("activityClick", activity)
        }
    }

    override fun getItemCount(): Int {
        return useractivityArray.size
    }
}

class AgentActivityHolder(var agentsActivityBinding: RowAgentActivityBinding) :
    RecyclerView.ViewHolder(agentsActivityBinding.root)
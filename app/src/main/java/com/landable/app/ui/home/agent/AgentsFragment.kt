package com.landable.app.ui.home.agent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentAgentsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.UserProfileDataModel

class AgentsFragment : Fragment(), AgentProfileListener {

    private lateinit var binding: FragmentAgentsBinding
    private var agentsList = ArrayList<UserProfileDataModel>()
    private var progressDialog: CustomProgressDialog? = null


    companion object {
        fun newInstance() = AgentsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Agents")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_agents, container, false)

        getAgentsListData()

        binding.ivAddAgent.setOnClickListener {
            loadAddAgentFragment(0)
        }
        return binding.root
    }

    fun getAgentsListData() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val agentsListResponse = RegisterRepository().getAgentsListData()
        agentsListResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()

            if (it.toString() != "null") {
                try {
                    agentsList = ParseResponse.parseUserProfileResponse(it.toString())
                    updateAgentsInfo()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateAgentsInfo() {
        if (agentsList.size == 0) {
            binding.tvNoResult.visibility = View.GONE
        } else {
            binding.rvAgentsList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvAgentsList.adapter = AgentsListAdapter(agentsList, this)

        }
    }

    private fun deleteAgent(id: Int) {
        val deleteResponse = RegisterRepository().getDeleteAgent(id)
        deleteResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    if (it.toString() != "null") {
                        if (it == "success") {
                            getAgentsListData()
                        } else {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onAgentClick(action: String, id: Int) {
        when (action) {
            "selectedAgentProfile" -> loadAgentProfileFragment(id)
            "deleteAgent" -> deleteAgent(id)
            "editSelectedAgent" -> {
                loadAddAgentFragment(id)
            }
        }
    }

    private fun loadAddAgentFragment(
        id: Int,
    ) {
        val bundle = Bundle()
        bundle.putInt("agentID", id)
        val agentEditFragment = AddAgentFragment.newInstance()
        agentEditFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            agentEditFragment,
            AddAgentFragment::class.java.name
        )

    }

    private fun loadAgentProfileFragment(
        id: Int,
    ) {
        val bundle = Bundle()
        bundle.putInt("agentID", id)
        val agencyProfileFragment = AgencyProfileFragment.newInstance()
        agencyProfileFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            agencyProfileFragment,
            AgencyProfileFragment::class.java.name
        )
    }
}
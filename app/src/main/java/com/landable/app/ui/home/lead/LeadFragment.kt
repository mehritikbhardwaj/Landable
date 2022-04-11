package com.landable.app.ui.home.lead

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.landable.app.R
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentLeadBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.LeadsDataModel

class LeadFragment : Fragment() {

    private lateinit var binding: FragmentLeadBinding
    private var progressDialog: CustomProgressDialog? = null
    private var leadsList = ArrayList<LeadsDataModel>()


    companion object {
        fun newInstance() = LeadFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Leads")
        (activity as HomeActivity).hideBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lead, container, false)

        getMyLeadsData()

        return binding.root
    }

    private fun getMyLeadsData() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val agentsListResponse = RegisterRepository().getMyLeads()
        agentsListResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()

            if (it.toString() != "null") {
                try {
                    leadsList = ParseResponse.parseLeadsResponse(it.toString())
                    updateLeadsInfo()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateLeadsInfo() {
        if (leadsList.size == 0) {
            binding.tvNoResult.visibility = View.GONE
        } else {
            binding.rvLeads.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvLeads.adapter = LeadsListAdapter(leadsList)

        }
    }
}
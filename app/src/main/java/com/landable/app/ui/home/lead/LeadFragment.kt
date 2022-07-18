package com.landable.app.ui.home.lead

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.LeadsClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentLeadBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.chats.ChatsFragment
import com.landable.app.ui.home.dataModels.LeadsDataModel

class LeadFragment : Fragment(), LeadsClickListener {

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

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Leads Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Leads Fragment", null)

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
            binding.rvLeads.adapter = LeadsListAdapter(leadsList, requireContext(), this)

        }
    }

    override fun onLeadsClick(action: String, leadsDataModel: LeadsDataModel) {
        when (action) {
            "chat" -> {
                 loadChatsFragment(leadsDataModel)
            }
            "call" -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel: ${leadsDataModel.mobile}")
                startActivity(intent)
            }
            else -> {
                (activity as HomeActivity).callBrowserActivity(
                    LandableConstants.Image_URL + leadsDataModel.link,
                    "LeadsClick"
                )
            }
        }
    }

    private fun loadChatsFragment(
        leadsDataModel: LeadsDataModel,
    ) {
        val bundle = Bundle()
        bundle.putString("type", leadsDataModel.type)
         bundle.putInt("id", leadsDataModel.chatid)
        bundle.putInt("toUserID", leadsDataModel.userid.toInt())
        bundle.putBoolean("comingfromchat", false)

        val chatsFragment = ChatsFragment.newInstance()
        chatsFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            chatsFragment,
            ChatsFragment::class.java.name
        )
    }
}
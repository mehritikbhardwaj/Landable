package com.landable.app.ui.home.supergroups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.SupergroupClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostedProjectBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.chats.ChatsFragment
import com.landable.app.ui.home.dataModels.SuperGroupsDataModelItem

class SuperGroupsFragment : Fragment(), SupergroupClickListener {

    private lateinit var binding: FragmentPostedProjectBinding
    private var progressDialog: CustomProgressDialog? = null
    private var superGroupsList = ArrayList<SuperGroupsDataModelItem>()


    companion object {
        fun newInstance() = SuperGroupsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("My Posts")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_project, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "My Supergroups Fragment", null)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "My Threads Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))

        getMySuperGroups()

        return binding.root
    }

    private fun getMySuperGroups() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getMySuperGroups()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    superGroupsList = ParseResponse.parseSuperGroupsList(it.toString())
                    updateSuperGroupsUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateSuperGroupsUI() {
        binding.rvPostedProject.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPostedProject.adapter = SuperGroupsAdapter(superGroupsList, this)
    }


    private fun loadAddPostFragment(threadID:Int) {
        val bundle = Bundle()
        bundle.putSerializable("isComingForEdit", false)
        bundle.putInt("threadId", threadID)
        val addSuperGroupFragment = AddSuperGroupFragment.newInstance()
        addSuperGroupFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            addSuperGroupFragment,
            AddSuperGroupFragment::class.java.name)
    }

    private fun deleteSupergroup(id: Int) {
        val deleteResponse = RegisterRepository().getDeleteSupergroup(id)
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
                        superGroupsList.clear()
                        getMySuperGroups()
                        Toast.makeText((activity as HomeActivity), it, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onClickSuperGroup(
        action: String,
        superGroupsDataModelItem: SuperGroupsDataModelItem
    ) {
        when (action) {
            "deleteSupergroup" -> {
                deleteSupergroup(superGroupsDataModelItem.id)
            }
            "chatSupergroup" -> {
                loadChatsFragment(superGroupsDataModelItem)
            }
            "detail" -> {
                (activity as HomeActivity).callBrowserActivity(
                    superGroupsDataModelItem.link, "Supergroup Detail " +
                            "Page"
                )
            }
            "edit"->{
                loadAddPostFragment(superGroupsDataModelItem.id)
            }
        }
    }

    private fun loadChatsFragment(
        supergroupDataModel: SuperGroupsDataModelItem,
    ) {
        val bundle = Bundle()
        bundle.putString("type", "Thread")
        bundle.putInt("id", supergroupDataModel.id)
        bundle.putInt("toUserID", supergroupDataModel.addedby)
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
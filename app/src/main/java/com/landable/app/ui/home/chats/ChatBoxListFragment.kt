package com.landable.app.ui.home.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.ChatBoxClickListener
import com.landable.app.common.FragmentHelper
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentSuperGroupsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.ChatBoxDataModel

class ChatBoxListFragment : Fragment(), ChatBoxClickListener {

    private lateinit var binding: FragmentSuperGroupsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var chatBoxList = ArrayList<ChatBoxDataModel>()

    companion object {
        fun newInstance() = ChatBoxListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Chats List")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_super_groups, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "ChatBox List Fragment", null);

        getChatBoxList()
        return binding.root
    }

    private fun getChatBoxList() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getChatbox()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    chatBoxList = ParseResponse.parseChatBoxList(it.toString())
                    updateChatBoxListUI()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateChatBoxListUI() {
        binding.rvChatBoxListing.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvChatBoxListing.adapter = ChatBoxListAdapter(chatBoxList, this)
    }

    override fun onChatBoxClick(action: String, chatBoxDataModel: ChatBoxDataModel) {
        when (action) {
            "chatBoxSelected" -> {
                loadChatsListFragment(chatBoxDataModel)
            }
        }
    }


    private fun loadChatsListFragment(
        chatBoxDataModel: ChatBoxDataModel,
    ) {
        val bundle = Bundle()
        bundle.putSerializable("chatBoxDataModel", chatBoxDataModel)
        val chatsListFragment = ChatsListFragment.newInstance()
        chatsListFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            chatsListFragment,
            ChatsListFragment::class.java.name
        )
    }

}
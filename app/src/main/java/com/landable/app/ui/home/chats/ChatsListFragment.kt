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
import com.landable.app.common.ChatsUserClickListener
import com.landable.app.common.FragmentHelper
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentSuperGroupsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.ChatBoxDataModel
import com.landable.app.ui.home.dataModels.ChatUsersDataModel

class ChatsListFragment : Fragment(), ChatsUserClickListener {

    private lateinit var binding: FragmentSuperGroupsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var chatUsersList = ArrayList<ChatUsersDataModel>()
    private var chatBoxDataModel: ChatBoxDataModel? = null

    companion object {
        fun newInstance() = ChatsListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting album model from other fragments
        chatBoxDataModel =
            requireArguments().getSerializable("chatBoxDataModel") as ChatBoxDataModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Chats")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_super_groups, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Chats List Fragment", null);

        getChatUsersList(chatBoxDataModel!!.id, chatBoxDataModel!!.type)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Chat UserList Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))
        return binding.root
    }

    private fun getChatUsersList(id: Int, type: String) {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getchatuserlist(id, type)
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    chatUsersList = ParseResponse.parseChatUsersList(it.toString())
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
        binding.rvChatBoxListing.adapter = AllChatsListAdapter(chatUsersList, this)
    }


    private fun loadChatsFragment(
        chatUsersDataModel: ChatUsersDataModel,
    ) {
        val bundle = Bundle()
        bundle.putSerializable("chatUsersDataModel", chatUsersDataModel)
        bundle.putString("type",chatBoxDataModel!!.type)
        bundle.putInt("id",chatBoxDataModel!!.id)
        bundle.putBoolean("comingfromchat",true)

        val chatsFragment = ChatsFragment.newInstance()
        chatsFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            chatsFragment,
            ChatsFragment::class.java.name
        )
    }

    override fun onUserClick(action: String, chatUsersDataModel: ChatUsersDataModel) {
        when (action) {
            "userClicked" -> {
                loadChatsFragment(chatUsersDataModel)
            }
        }
    }


}
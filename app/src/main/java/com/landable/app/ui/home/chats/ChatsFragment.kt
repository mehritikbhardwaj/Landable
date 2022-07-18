package com.landable.app.ui.home.chats

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.MyCountDownTimer
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentContentChatsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.Chat
import com.landable.app.ui.home.dataModels.ChatUsersDataModel
import com.landable.app.ui.home.dataModels.ChatsDataModel

class ChatsFragment : Fragment(), MyCountDownTimer.ICompleteTimerListener {

    private lateinit var binding: FragmentContentChatsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var chatsList = ArrayList<Chat>()
    private var chats: ChatsDataModel? = null
    private var chatUsersDataModel: ChatUsersDataModel? = null
    private var type: String = ""
    private var _Id: Int = 0
    private var touSerID: Int = 0
    private var isComingfromChat: Boolean = false
    private var chatsAdapter:ChatsAdapter? = null

    private var countDownTimer: MyCountDownTimer? = null

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 20000

    // private var file: File? = null

    companion object {
        fun newInstance() = ChatsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isComingfromChat = requireArguments().getBoolean("comingfromchat")
        type = requireArguments().getString("type")!!
        _Id = requireArguments().getInt("id")
        touSerID = requireArguments().getInt("toUserID")
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer!!.cancel()
    }

    override fun onResume() {
        super.onResume()
        startTimerAndUpdateUi()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_content_chats, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Chats Fragment", null)

        if (isComingfromChat) {
            chatUsersDataModel =
                requireArguments().getSerializable("chatUsersDataModel") as ChatUsersDataModel?
            chatUsersDataModel =
                requireArguments().getSerializable("chatUsersDataModel") as ChatUsersDataModel
            binding.ivProfileImage.load(LandableConstants.Image_URL + chatUsersDataModel!!.logo)
            binding.tvName.text = chatUsersDataModel!!.name
            touSerID = chatUsersDataModel!!.id
        }

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Chats Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))
        binding.ivBack.setOnClickListener {
            FragmentHelper().popBackStackImmediate((activity as HomeActivity))
        }

        if (chatUsersDataModel == null) {
            getChatsList(_Id, touSerID, type)
        } else getChatsList(_Id, touSerID, type)

        binding.ivSendChat.setOnClickListener {
            RegisterRepository().post_Addchat(
                _Id,
                binding.etChat.text.toString(), touSerID, type
            )
             chatsList.clear()
            getChatsList(_Id, touSerID, type)
            binding.etChat.text.clear()
        }
        return binding.root
    }


    private fun getChatsList(id: Int, toUserId: Int, type: String) {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getchatcommentlist(id, toUserId, type)
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    chats = ParseResponse.parseChatsResponse(it.toString())
                    chatsList = chats!!.chat
                    updateChatsUI()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun refreshChats(id: Int, toUserId: Int, type: String) {
        // Show progress bar
        val dashboardResponse = RegisterRepository().getchatcommentlist(id, toUserId, type)
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    chats = ParseResponse.parseChatsResponse(it.toString())
                    chatsList = chats!!.chat
                    updateChatsUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun updateChatsUI() {

        binding.rvChats.smoothScrollToPosition(chatsList.size)
        binding.rvChats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvChats.itemAnimator = DefaultItemAnimator()
        chatsAdapter = ChatsAdapter(chatsList, requireContext())
        binding.rvChats.adapter = chatsAdapter
        binding.rvChats.smoothScrollToPosition(chatsList.size)
        binding.ivProfileImage.load(LandableConstants.Image_URL + chats!!.logo)
        binding.tvName.text = chats!!.name
    }

    override fun onCompleteTimer(action: String) {
        chatsList.clear()
        refreshChats(_Id, touSerID, type)
        startTimerAndUpdateUi()
    }

    private fun startTimerAndUpdateUi() {
        countDownTimer = MyCountDownTimer(200000, 1000, binding.tvDummy, this)
        countDownTimer!!.start()
    }


}


package com.landable.app.ui.home.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentContentChatsBinding
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.chats.ChatsAdapter
import com.landable.app.ui.home.dataModels.Chat
import com.landable.app.ui.home.dataModels.ChatsDataModel

class ChatsWebFragment : Fragment() {

    private lateinit var binding: FragmentContentChatsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var chatsList = ArrayList<Chat>()
    private var chats: ChatsDataModel? = null
    private var type: String = ""
    private var _Id: Int = 0
    private var touSerID: Int = 0

    companion object {
        fun newInstance() = ChatsWebFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = requireArguments().getString("type")!!
        _Id = requireArguments().getInt("id")
        touSerID = requireArguments().getInt("toUserID")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_content_chats, container, false)

        //  binding.ivBack.visibility = View.GONE

        binding.ivBack.setOnClickListener {
            (activity as ChatActivity).finish()
        }
        getChatsList(_Id, touSerID, type)

        binding.ivSendChat.setOnClickListener {
            RegisterRepository().post_Addchat(
                _Id,
                binding.etChat.text.toString(), touSerID, type
            )
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
        binding.rvChats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvChats.adapter = ChatsAdapter(chatsList, requireContext())
        binding.ivProfileImage.load(LandableConstants.Image_URL + chats!!.logo)
        binding.tvName.text = chats!!.name
    }


}

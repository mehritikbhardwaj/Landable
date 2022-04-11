package com.landable.app.ui.home.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.ChatsUserClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowChatsListBinding
import com.landable.app.ui.home.dataModels.ChatUsersDataModel

class AllChatsListAdapter(
    private val chatUsersList: ArrayList<ChatUsersDataModel>,
    private val chatsUserClickListener: ChatsUserClickListener
) :
    RecyclerView.Adapter<ChatUsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUsersViewHolder {
        val binding: RowChatsListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_chats_list,
            parent,
            false
        )
        return ChatUsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatUsersViewHolder, position: Int) {
        val chatBox = chatUsersList[position]

        holder.chatBoxRowBinding.ivImage.load(LandableConstants.Image_URL + chatBox.logo)
        holder.chatBoxRowBinding.tvName.text = chatBox.name
        holder.chatBoxRowBinding.tvRating.text = chatBox.rating.toString()

        if (chatBox.recentchat.isEmpty()) {
            holder.chatBoxRowBinding.tvMessage.text = "Start a Conversation"
        } else holder.chatBoxRowBinding.tvMessage.text = chatBox.recentchat

        holder.chatBoxRowBinding.llChatBox.setOnClickListener {
            chatsUserClickListener.onUserClick("userClicked", chatBox)
        }
    }

    override fun getItemCount(): Int {
        return chatUsersList.size
    }
}


class ChatUsersViewHolder(var chatBoxRowBinding: RowChatsListBinding) :
    RecyclerView.ViewHolder(chatBoxRowBinding.root)
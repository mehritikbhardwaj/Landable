package com.landable.app.ui.home.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.ChatBoxClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowChatsBoxBinding
import com.landable.app.databinding.RowSuperGroupsBinding
import com.landable.app.ui.home.dataModels.ChatBoxDataModel

class ChatBoxListAdapter(
    private val chatBoxList: ArrayList<ChatBoxDataModel>,
    private val chatBoxClickListener: ChatBoxClickListener
) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowChatsBoxBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_chats_box,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chatBox = chatBoxList[position]

        holder.chatBoxRowBinding.ivImage.load(LandableConstants.Image_URL + chatBox.iconimage)
        holder.chatBoxRowBinding.tvTitle.text = chatBox.title
        holder.chatBoxRowBinding.tvAddress.text = chatBox.address
        holder.chatBoxRowBinding.tvCategoryName.text = chatBox.categoryname

        if (chatBox.arbitrage.isNullOrEmpty()) {
            holder.chatBoxRowBinding.tvArbitrage.visibility = View.GONE
        } else holder.chatBoxRowBinding.tvArbitrage.text = chatBox.arbitrage

        holder.chatBoxRowBinding.llChatBox.setOnClickListener {
            chatBoxClickListener.onChatBoxClick("chatBoxSelected",chatBox)
        }
    }

    override fun getItemCount(): Int {
        return chatBoxList.size
    }
}


class MyViewHolder(var chatBoxRowBinding: RowChatsBoxBinding) :
    RecyclerView.ViewHolder(chatBoxRowBinding.root)
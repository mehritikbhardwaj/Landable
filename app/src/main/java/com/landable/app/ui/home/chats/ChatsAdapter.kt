package com.landable.app.ui.home.chats

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.ui.home.dataModels.Chat

class ChatsAdapter(private val chatsList: ArrayList<Chat>, private var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val RECIEVE = 1
    val SENT = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.list_row_chat_left, parent, false)
            return RecieveViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.list_row_chat_right, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (AppInfo.getUserId() == chatsList[position].touserid.toString()) {
            RECIEVE
        } else SENT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = chatsList[position]

        if (holder.javaClass == SentViewHolder::class.java) {

            holder as SentViewHolder
            holder.sentMessage.text = Html.fromHtml(currentMessage.comment)
            holder.tv_date.text = Html.fromHtml(currentMessage.postedsince)

        } else {
            holder as RecieveViewHolder
            holder.recieveMessage.text = Html.fromHtml(currentMessage.comment)
            holder.tv_date.text = Html.fromHtml(currentMessage.postedsince)

        }
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.tv_Sent_msg)
        val tv_date = itemView.findViewById<TextView>(R.id.tv_date)

    }

    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recieveMessage = itemView.findViewById<TextView>(R.id.tv_msg)
        val tv_date = itemView.findViewById<TextView>(R.id.tv_date)

    }
}
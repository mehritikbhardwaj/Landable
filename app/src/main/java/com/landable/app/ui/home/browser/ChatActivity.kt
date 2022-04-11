package com.landable.app.ui.home.browser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.landable.app.R
import com.landable.app.common.FragmentHelper

class ChatActivity : AppCompatActivity() {

    private var urlForChat:String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        urlForChat = intent.getStringExtra("url")

        if(urlForChat ==""){
            loadAddPostFragment()
        }else loadChatFragment()



    }

    private fun loadChatFragment() {
        if (!urlForChat.isNullOrEmpty()) {
            val type = urlForChat!!.substringAfter("&type=")
            val thString = urlForChat!!.substringBefore("&uid=")
            val th = thString.substringAfter("th=")
            val toUserIdString = urlForChat!!.substringBefore("&type=")
            val toUserId = toUserIdString.substringAfter("&touserid=")

            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putInt("id", th.toInt())
            bundle.putInt("toUserID", toUserId.toInt())

            val chatsFragment = ChatsWebFragment.newInstance()
            chatsFragment.arguments = bundle
            FragmentHelper().addFragment(
                supportFragmentManager,
                getChatViewActivityContainerId(),
                chatsFragment,
                ChatsWebFragment::class.java.name
            )
        }
    }

    fun loadAddPostFragment() {
        val bundle = Bundle()
        val addPostFragment = AddSuperGroupWebFragment.newInstance()
        addPostFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getChatViewActivityContainerId(),
            addPostFragment,
            AddSuperGroupWebFragment::class.java.name
        )
    }

    private fun getChatViewActivityContainerId(): Int {
        return R.id.fragment_container
    }
}
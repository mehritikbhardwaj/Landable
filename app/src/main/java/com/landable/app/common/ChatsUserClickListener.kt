package com.landable.app.common

import com.landable.app.ui.home.dataModels.ChatUsersDataModel

interface ChatsUserClickListener {
    fun onUserClick(action: String, chatUsersDataModel: ChatUsersDataModel)

}
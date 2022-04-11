package com.landable.app.common

import com.landable.app.ui.home.dataModels.ActivityDataModel
import com.landable.app.ui.home.dataModels.ChatBoxDataModel

interface ChatBoxClickListener {
    fun onChatBoxClick(action: String, chatBoxDataModel: ChatBoxDataModel)
}
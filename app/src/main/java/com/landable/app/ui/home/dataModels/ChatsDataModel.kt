package com.landable.app.ui.home.dataModels

import java.io.Serializable

class ChatsDataModel:Serializable {
    var chat= ArrayList<Chat>()
    var userassets= ArrayList<Userasset>()

    var name:String = ""
    var agencyname:String = ""
    var logo:String = ""

}
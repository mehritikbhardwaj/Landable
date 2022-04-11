package com.landable.app.ui.home.dataModels

import java.io.Serializable

class SearchAgencyDataModel:Serializable {
    var advertisment= ArrayList<Advertisment>()
    var searchid: Int=0
    var searchdescription:String = ""
    var userProfiles= ArrayList<Profile>()
}
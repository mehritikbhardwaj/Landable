package com.landable.app.ui.home.dataModels

import java.io.Serializable

class SearchProjectDataModel:Serializable {
    var projects = ArrayList<ProjectsDataModel>()
    var advertisment = ArrayList<Advertisment>()
    var searchid :Int = 0
    var searchdescription :String = ""

}
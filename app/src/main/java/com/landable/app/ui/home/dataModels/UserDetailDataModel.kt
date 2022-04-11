package com.landable.app.ui.home.dataModels

import java.io.Serializable

class UserDetailDataModel : Serializable {

    var Projects = ArrayList<ProjectsDataModel>()
    var Properties = ArrayList<FeaturePropertiesDataModel>()
    var Useractivities = ArrayList<Useractivity>()
    var profile = Profile()
}
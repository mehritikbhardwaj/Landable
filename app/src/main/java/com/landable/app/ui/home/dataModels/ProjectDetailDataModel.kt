package com.landable.app.ui.home.dataModels

import java.io.Serializable

class ProjectDetailDataModel : Serializable {

    var Amenitiesmasters = ArrayList<Amenitiesmaster>()
    var additionaldetails = AdditionaldetailsProject()
    var advertisment = ArrayList<Advertisment>()
    var details = ProjectsDataModel()
    var distancefromlocation = ArrayList<Distancefromlocation>()
    var featuredprojects = ArrayList<ProjectsDataModel>()
    var projectconfiguration = ArrayList<Projectconfiguration>()
    var projectimages = ArrayList<Propertyimage>()
    var projectsproperty = ArrayList<Any>()
    var review = ArrayList<Review>()
    var similarprojects = ArrayList<ProjectsDataModel>()
}

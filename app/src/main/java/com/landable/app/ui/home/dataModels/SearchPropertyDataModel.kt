package com.landable.app.ui.home.dataModels

import java.io.Serializable

class SearchPropertyDataModel : Serializable {

    var Properties = ArrayList<FeaturePropertiesDataModel>()
    var advertisment = ArrayList<Advertisment>()
    var searchid :Int = 0
    var searchdescription:String = ""
}
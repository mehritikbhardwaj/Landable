package com.landable.app.ui.home.dataModels

import java.io.Serializable

class ListingDataModel : Serializable {

    var featuredproperties = ArrayList<FeaturePropertiesDataModel>()
    var projects = ArrayList<ProjectsDataModel>()
}

package com.landable.app.ui.home.dataModels

import java.io.Serializable

class DashBoardDataModel  : Serializable {

    var categorymaster = ArrayList<CategoriesDataModel>()
    var propertytypemaster = ArrayList<PropertyTypeDataModel>()
    var whylandable = ArrayList<WhyLandableDataModel>()
    var featuredproperties =  ArrayList<FeaturePropertiesDataModel>()
    var recentproperties = ArrayList<FeaturePropertiesDataModel>()
    var projects = ArrayList<ProjectsDataModel>()


    var residentialTypeLinkedHashMap = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
    var commercialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
    var agriculturalTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
    var otherTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()

}
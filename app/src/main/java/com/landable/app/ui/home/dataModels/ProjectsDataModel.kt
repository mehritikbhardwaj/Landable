package com.landable.app.ui.home.dataModels

import java.io.Serializable

class ProjectsDataModel : Serializable {
    var id : Int = 0
    var projectid : String = ""
    var projectname : String = ""
    var Fulladdress : String = ""
    var description : String = ""
    var categoryname : String = ""
    var subcategoryname : String = ""
    var possessionname : String = ""
    var addedbyid : Int = 0
    var mincost : String = ""
    var maxcost : String = ""
    var customertype : String = ""
    var name : String = ""
    var image1 : String = ""
    var configuration : String = ""
    var configuration2 : String = ""
    var strength : Double = 0.00
    var strengthmsg : String = ""
    var rating: Double = 0.00
    var lat : String = ""
    var lon :String = ""
    var isfavourite:String = ""
    var startdate: String = ""
    var enddate: String = ""
    var clicks: Int = 0
    var leads: Int = 0
    var isverfiedicon:String = ""
    var linkurl:String = ""
    var badges:String = ""
    var profilepic:String = ""
}
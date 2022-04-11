package com.landable.app.ui.home.dataModels

import java.io.Serializable

class UserProfileDataModel : Serializable {

    var userid: Int = 0
    var scode: String = ""
    var customertype: String = ""
    var name: String = ""
    var mobile: String = ""
    var email: String = ""
    var password: String = ""
    var country:Int = 0
    var state:Int = 0
    var city:Int = 0
    var address: String = ""
    var statename: String = ""
    var cityname: String = ""
    var agencyname: String = ""
    var description: String = ""
    var customer_link: String = ""
    var profilepic: String = ""
    var lat: String = ""
    var lon: String = ""
    var badges: String = ""
    var rating: Double = 0.0
    var userstatus: String = ""
    var strength: Double = 0.0

}
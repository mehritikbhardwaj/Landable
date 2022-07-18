package com.landable.app.ui.home.dataModels

import java.io.Serializable

class SupergroupMasterMediaModel:Serializable {

    var image = ArrayList<SupergroupMediaModel>()
    var video = ArrayList<SupergroupMediaModel>()
    var document = ArrayList<SupergroupMediaModel>()
    var url :String = ""

}
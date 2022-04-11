package com.landable.app.ui.home.dataModels

import java.io.Serializable

class SearchAuctionDataModel:Serializable {
    var Auction= ArrayList<AuctionSearchInfoModel>()
    var advertisment= ArrayList<Advertisment>()
    var searchid:Int = 0
    var searchdescription:String = ""
}
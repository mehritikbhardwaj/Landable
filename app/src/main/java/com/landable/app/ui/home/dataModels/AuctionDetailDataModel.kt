package com.landable.app.ui.home.dataModels

import java.io.Serializable

class AuctionDetailDataModel : Serializable {
    var Auction = ArrayList<Auction>()
    var advertisment = ArrayList<Advertisment>()
    var auctiondocuments = ArrayList<Auctiondocument>()
}
package com.landable.app.ui.home.dataModels

import java.io.Serializable

class FilterMasterDataModel : Serializable {
    var Amenitiesmaster = ArrayList<Amenitiesmaster>()
    var Arbitragemaster = ArrayList<Arbitragemaster>()
    var AuctionPossessiontypeMaster = ArrayList<AuctionPossessiontypeMaster>()
    var Unitmaster = ArrayList<Unitmaster>()
    var balconey = ArrayList<Bedroom>()
    var banknamemaster = ArrayList<Banknamemaster>()
    var bathroom = ArrayList<Bedroom>()
    var bedroom = ArrayList<Bedroom>()
    var borrowernamemaster = ArrayList<Borrowernamemaster>()
    var categorymaster = ArrayList<CategoriesDataModel>()
    var cityHashMap = LinkedHashMap<Int, ArrayList<Citymaster>>()
    var floormaster = ArrayList<Floormaster>()
    var furnishedmaster = ArrayList<Arbitragemaster>()
    var localitymaster = ArrayList<Localitymaster>()
    var mimaxauctionPriceRange = ArrayList<MimaxauctionPriceRange>()
    var mimaxpropertyPriceRange = ArrayList<MimaxpropertyPriceRange>()
    var parking = ArrayList<Bedroom>()
    var possessionmaster = ArrayList<Arbitragemaster>()
    var propertytypemaster = ArrayList<PropertyTypeDataModel>()
    var saletypemaster = ArrayList<Arbitragemaster>()
    var statemaster = ArrayList<Statemaster>()


    var residentialTypeLinkedHashMap = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
    var commercialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
    var agriculturalTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
    var otherTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()

}

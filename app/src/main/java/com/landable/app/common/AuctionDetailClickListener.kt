package com.landable.app.common

import com.landable.app.ui.home.dataModels.AuctionSearchInfoModel

interface AuctionDetailClickListener {
    fun onAuctionClick(action: String, auctionSearchInfoModelDataModel: AuctionSearchInfoModel)

}
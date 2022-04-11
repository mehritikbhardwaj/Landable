package com.landable.app.common

import com.landable.app.ui.home.dataModels.Auctiondocument

interface AuctionDocumentClickListener {
    fun onDocumentClick(action: String, auctiondocument: Auctiondocument?)
}
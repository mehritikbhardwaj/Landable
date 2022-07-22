package com.landable.app.common

import com.landable.app.ui.home.dataModels.Arbitragemaster

interface ArbitrageTypeClick {
    fun onArbitrageClick(action: String, activity: Arbitragemaster)
}
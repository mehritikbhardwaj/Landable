package com.landable.app.common

import com.landable.app.ui.home.dataModels.Projectconfiguration

interface ConfigurationImageClickListener {
    fun onImageClick(action: String, configuration: Projectconfiguration?)

}
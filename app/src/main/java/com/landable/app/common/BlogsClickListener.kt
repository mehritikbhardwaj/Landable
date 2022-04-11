package com.landable.app.common

import com.landable.app.ui.home.dataModels.BlogDataModel

interface BlogsClickListener {
    fun onBlogClick(action: String, blogsDataModel: BlogDataModel)

}
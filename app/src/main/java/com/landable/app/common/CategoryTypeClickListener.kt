package com.landable.app.common

import com.landable.app.ui.home.dataModels.CategoriesDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel

interface CategoryTypeClickListener {
    fun onCategoryClick(action: String, categoryDataModel: CategoriesDataModel?)

}
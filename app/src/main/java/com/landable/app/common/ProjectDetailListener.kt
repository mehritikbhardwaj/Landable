package com.landable.app.common

import com.landable.app.ui.home.dataModels.ProjectsDataModel

interface ProjectDetailListener {
    fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?)
}
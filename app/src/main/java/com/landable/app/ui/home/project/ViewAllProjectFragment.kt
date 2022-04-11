package com.landable.app.ui.home.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.ProjectDetailListener
import com.landable.app.databinding.FragmentViewAllBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.search.SearchProjectAdapter

class ViewAllProjectFragment : Fragment(), ProjectDetailListener {

    private lateinit var binding: FragmentViewAllBinding
    private var projectsList = ArrayList<ProjectsDataModel>()


    companion object {
        fun newInstance() = ViewAllProjectFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectsList =
            requireArguments().getSerializable("allProjects") as ArrayList<ProjectsDataModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Projects")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_all, container, false)

        updateProjects()

        return binding.root
    }


    private fun updateProjects() {
        binding.rvProperties.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProperties.adapter = SearchProjectAdapter(projectsList, this,requireContext())
    }

    private fun loadProjectDetailFragment(
        projectsDataModel: ProjectsDataModel?,
    ) {
        val bundle = Bundle()
        bundle.putSerializable("projectDetailModel", projectsDataModel)
        val propertyDetailFragment = ProjectDetailFragment.newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            ProjectDetailFragment::class.java.name
        )
    }

    override fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?) {
        when (action) {
            "selectedProjectDetail" -> {
                loadProjectDetailFragment(projectDataModel)
            }
        }
    }
}
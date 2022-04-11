package com.landable.app.ui.home.favorites

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
import com.landable.app.databinding.FragmentPostedProjectBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.project.ProjectDetailFragment
import com.landable.app.ui.home.search.SearchProjectAdapter

class FavoriteProjectFragment : Fragment(), ProjectDetailListener {

    private lateinit var binding: FragmentPostedProjectBinding
    private var projectList = ArrayList<ProjectsDataModel>()

    companion object {
        fun newInstance() = FavoriteProjectFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting album model from other fragments
        projectList =
            requireArguments().getSerializable("projectList") as ArrayList<ProjectsDataModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Favourites")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_project, container, false)

        if(projectList.size ==0){
            binding.tvNoResult.visibility = View.VISIBLE
        }else{
            binding.rvPostedProject.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvPostedProject.adapter = SearchProjectAdapter(projectList, this,requireContext())

        }

        return binding.root
    }

    override fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?) {
        when (action) {
            "selectedProjectDetail" -> {
                loadProjectDetailFragment(projectDataModel)
            }
        }
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
}
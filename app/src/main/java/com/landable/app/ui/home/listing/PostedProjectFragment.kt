package com.landable.app.ui.home.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.ProjectDetailListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.FragmentPostedProjectBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.postProjectProperty.PostProjectPropertyFragment
import com.landable.app.ui.home.project.ProjectDetailFragment

class PostedProjectFragment : Fragment(), ProjectDetailListener {

    private lateinit var binding: FragmentPostedProjectBinding
    private var projectList = ArrayList<ProjectsDataModel>()

    companion object {
        fun newInstance() = PostedProjectFragment()
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
        (activity as HomeActivity).enableBackButton("My listing")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_project, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Posted Project Fragment", null);

        if (projectList.size == 0) {
            binding.tvNoResult.visibility = View.VISIBLE
        } else {
            binding.rvPostedProject.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvPostedProject.adapter = PostedProjectAdapter(projectList, this)
        }
        return binding.root

    }

    override fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?) {
        when (action) {
            "selectedProjectDetail" -> {
                loadProjectDetailFragment(projectDataModel)
            }
            "deleteSelectedPRoject" -> {
                deleteProject(projectDataModel!!.id)
            }
            "editSelectedPRoject" -> {
                loadProjectEditFragment(projectDataModel)
            }
            "addProperty" -> {
                loadPostPropertyFragment()
            }
        }
    }

    private fun loadPostPropertyFragment() {
        val bundle = Bundle()
        bundle.putString("isComingForWhichEditType", "None")
        val propertyeditFragment = PostProjectPropertyFragment.newInstance()
        propertyeditFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyeditFragment,
            PostProjectPropertyFragment::class.java.name
        )
    }


    private fun loadProjectEditFragment(projectdataModel: ProjectsDataModel?) {
        val bundle = Bundle()
        bundle.putString("isComingForWhichEditType", "ProjectEdit")
        bundle.putSerializable("projectDataModel", projectdataModel)
        val propertyeditFragment = PostProjectPropertyFragment.newInstance()
        propertyeditFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyeditFragment,
            PostProjectPropertyFragment::class.java.name
        )
    }

    private fun deleteProject(id: Int) {
        val deleteResponse = RegisterRepository().getDeleteProject(id)
        deleteResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    if (it.toString() != "null") {
                        if (it == "success") {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

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
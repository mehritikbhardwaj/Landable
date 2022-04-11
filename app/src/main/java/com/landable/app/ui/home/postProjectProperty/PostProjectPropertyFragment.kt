package com.landable.app.ui.home.postProjectProperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.databinding.FragmentPostPropertyMainBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.postProjectProperty.project.PostProjectBasicInfoFragment
import com.landable.app.ui.home.postProjectProperty.property.PostPropertyBasicInfoFragment
import com.landable.app.ui.home.search.ViewPagerAdapter

class PostProjectPropertyFragment : Fragment() {

    private lateinit var binding: FragmentPostPropertyMainBinding

    private var propertyInfo: FeaturePropertiesDataModel? = null
    private var projectInfo: ProjectsDataModel? = null

    private val postPropertyFragment = PostPropertyBasicInfoFragment.newInstance()
    private val postProjectFragment = PostProjectBasicInfoFragment.newInstance()

    private var isComingForWhichEditType: String? = null

    companion object {
        fun newInstance() = PostProjectPropertyFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isComingForWhichEditType = requireArguments().getString("isComingForWhichEditType")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_post_property_main, container,
                false
            )

        (activity as HomeActivity).hideBottomNavigation()
        (activity as HomeActivity).showTopBar()


        when (isComingForWhichEditType) {
            "PropertyEdit" -> {
                (activity as HomeActivity).enableBackButton("Edit Property")
            }
            "ProjectEdit" -> {
                (activity as HomeActivity).enableBackButton("Edit Project")
            }
            else -> {
                (activity as HomeActivity).enableBackButton("Post Property / Project")
            }

        }


        if (isComingForWhichEditType == "PropertyEdit") {
            propertyInfo =
                requireArguments().getSerializable("propertyDataModel") as FeaturePropertiesDataModel
        }

        if (isComingForWhichEditType == "ProjectEdit") {
            projectInfo =
                requireArguments().getSerializable("projectDataModel") as ProjectsDataModel
        }


        setUpTabs()

        return binding.root

    }

    private fun setUpTabs() {
        val bundle = Bundle()
        bundle.putSerializable("propertiesDataModel", propertyInfo)
        bundle.putString("isComingForWhichEditType", isComingForWhichEditType)
        postPropertyFragment.arguments = bundle

        val otherFacesBundle = Bundle()
        otherFacesBundle.putSerializable("projectsDataModel", projectInfo)
        otherFacesBundle.putString("isComingForWhichEditType", isComingForWhichEditType)
        postProjectFragment.arguments = otherFacesBundle

        val adapter = ViewPagerAdapter(childFragmentManager)
        when (isComingForWhichEditType) {
            "PropertyEdit" -> {
                adapter.addFragment(postPropertyFragment, "Edit Property")
            }
            "None" -> {
                adapter.addFragment(postPropertyFragment, "Post Property")
                if (AppInfo.getCustomerType() != "Individual") {
                    adapter.addFragment(postProjectFragment, "Post Project")
                }
            }
        }

        if (AppInfo.getCustomerType() != "Individual" && isComingForWhichEditType == "ProjectEdit") {
            adapter.addFragment(postProjectFragment, "Edit Project")
        }
        binding.pager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.pager)
    }
}


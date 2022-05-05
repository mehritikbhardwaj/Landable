package com.landable.app.ui.home.homeUI

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentHomeBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.auction.FragmentAuction
import com.landable.app.ui.home.blogs.BlogFragment
import com.landable.app.ui.home.chats.ChatBoxListFragment
import com.landable.app.ui.home.dataModels.DashBoardDataModel
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.dataModels.WhyLandableDataModel
import com.landable.app.ui.home.news.NewsFragment
import com.landable.app.ui.home.notifications.NotificationsFragment
import com.landable.app.ui.home.postProjectProperty.PostProjectPropertyFragment
import com.landable.app.ui.home.project.ProjectDetailFragment
import com.landable.app.ui.home.project.ViewAllProjectFragment
import com.landable.app.ui.home.project.adapter.ProjectsAdapter
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.property.ViewAllPropertyFragment
import com.landable.app.ui.home.property.adapters.FeaturePropertiesAdapter
import com.landable.app.ui.home.search.SearchFragment


class HomeFragment : Fragment(), PropertyDetailListener, ProjectDetailListener,
    WhyLandableClickListener {

    private lateinit var binding: FragmentHomeBinding
    private var whyLandableList = ArrayList<WhyLandableDataModel>()
    private var featurePropertyList = ArrayList<FeaturePropertiesDataModel>()
    private var recentPropertyList = ArrayList<FeaturePropertiesDataModel>()
    private var projectsList = ArrayList<ProjectsDataModel>()
    private var dashBoardData: DashBoardDataModel? = null
    private var progressDialog: CustomProgressDialog? = null
    private var whyLandableVisiblePosition: Int = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectsList = (activity as HomeActivity).getProjectsList()
        recentPropertyList = (activity as HomeActivity).getRecentPropertyList()
        projectsList = (activity as HomeActivity).getProjectsList()
        featurePropertyList = (activity as HomeActivity).getfeaturePropertyList()
        whyLandableList = (activity as HomeActivity).getWhyLandableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).showBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Dashboard", null);

        binding.ivSideNavigation.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }

        binding.ivNotifications.setOnClickListener {
            loadNotificationsFragment()
        }

        //hideLeftScrollButtons()

        binding.rightScrollLandable.setOnClickListener {
            binding.leftScrollLandable.visibility = View.VISIBLE
            whyLandableVisiblePosition = getLastVisiblePosition(binding.rvWhyLandable) - 1
            scrollRightRecyclerView(binding.rvWhyLandable)
            if (whyLandableVisiblePosition >= whyLandableList.size - 2) {
                binding.rightScrollLandable.visibility = View.GONE
            }
        }
        binding.leftScrollLandable.setOnClickListener {
            binding.rightScrollLandable.visibility = View.VISIBLE
            whyLandableVisiblePosition = getLastVisiblePosition(binding.rvWhyLandable) - 1
            if (whyLandableVisiblePosition == 1) {
                binding.leftScrollLandable.visibility = View.GONE
            } else {
                scrollLeftRecyclerView(binding.rvWhyLandable)
               /* if (whyLandableVisiblePosition == 1) {
                    binding.leftScrollLandable.visibility = View.GONE
                }*/
            }
        }


        binding.editText.setOnClickListener {
            loadSearchFragment()
        }

        binding.viewAllFeaturedProperties.setOnClickListener {
            if (featurePropertyList.size != 0) {
                loadViewAllFeaturedProperties(featurePropertyList)
            }
        }

        binding.viewAllProjects.setOnClickListener {
            if (projectsList.size != 0) {
                loadViewAllProjects()
            }
        }

        binding.viewAllRecentProperties.setOnClickListener {
            if (recentPropertyList.size != 0) {
                loadViewAllFeaturedProperties(recentPropertyList)
            }
        }

        binding.llPostProperty.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                (activity as HomeActivity).askForLogin()
            } else loadPostPropertyFragment()
        }

        if (featurePropertyList.size == 0 || recentPropertyList.size == 0 || projectsList.size == 0) {
            getDashboardInfo()
        } else updateDashboardInfo()

        if ((activity as HomeActivity).propertyID != "" && AppInfo.getSCode() != "") {
            postContactOwner(
                (activity as HomeActivity).propertyID,
                (activity as HomeActivity).contactType
            )
            (activity as HomeActivity).propertyID = ""
        }

        return binding.root
    }

    private fun scrollRightRecyclerView(recyclerView: RecyclerView) {
        recyclerView.smoothScrollToPosition(whyLandableVisiblePosition + 2)
        whyLandableVisiblePosition += 1
    }

    private fun scrollLeftRecyclerView(recyclerView: RecyclerView) {
        recyclerView.smoothScrollToPosition(whyLandableVisiblePosition - 2)
        whyLandableVisiblePosition -= 1

    }

    private fun hideLeftScrollButtons() {
        binding.leftScrollLandable.visibility = View.GONE
        binding.leftScrollFeature.visibility = View.GONE
        binding.leftScrollRecent.visibility = View.GONE
        binding.leftScrollProject.visibility = View.GONE
    }

    fun getLastVisiblePosition(rv: RecyclerView?): Int {
        if (rv != null) {
            val layoutManager = rv
                .layoutManager
            if (layoutManager is LinearLayoutManager) {
                return layoutManager
                    .findLastVisibleItemPosition()
            }
        }
        return 0
    }


    private fun postContactOwner(propertyid: String, type: String) {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val newsResponse = RegisterRepository().postContactOwner(propertyid, type)
        newsResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    loadAgentProfileFragment((activity as HomeActivity).agentID)
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadAgentProfileFragment(id: Int) {
        val bundle = Bundle()
        bundle.putInt("agentID", id)
        val agencyProfileFragment = AgencyProfileFragment.newInstance()
        agencyProfileFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            agencyProfileFragment,
            AgencyProfileFragment::class.java.name
        )
    }

    private fun loadViewAllFeaturedProperties(propertyList: ArrayList<FeaturePropertiesDataModel>) {
        val bundle = Bundle()
        bundle.putSerializable("allProperties", propertyList)
        val allPropertiesFragment = ViewAllPropertyFragment.newInstance()
        allPropertiesFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            allPropertiesFragment,
            ViewAllPropertyFragment::class.java.name

        )
    }

    private fun loadViewAllProjects() {
        val bundle = Bundle()
        bundle.putSerializable("allProjects", projectsList)
        val allProjectFragment = ViewAllProjectFragment.newInstance()
        allProjectFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            allProjectFragment,
            ViewAllProjectFragment::class.java.name

        )
    }

    private fun whyLandableList() {
        binding.rvWhyLandable.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvWhyLandable.adapter = WhyLandableAdapter(whyLandableList, this)
    }

    private fun updateFeaturePropertiesList() {
        binding.rvFeaturedProperties.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedProperties.adapter = FeaturePropertiesAdapter(featurePropertyList, this)
    }

    private fun updateRecentPropertiesList() {
        binding.rvRecentProperties.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecentProperties.adapter = FeaturePropertiesAdapter(recentPropertyList, this)
    }

    private fun updateProjectsList() {
        binding.rvFeaturesProjects.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturesProjects.adapter = ProjectsAdapter(projectsList, this, requireContext())
    }

    private fun updateDashboardInfo() {
        whyLandableList()
        updateFeaturePropertiesList()
        updateRecentPropertiesList()
        updateProjectsList()
    }

    private fun getDashboardInfo() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getDashboardInfo()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    dashBoardData = ParseResponse.parseDashboardResponse(it.toString())
                    whyLandableList = dashBoardData!!.whylandable
                    featurePropertyList = dashBoardData!!.featuredproperties
                    recentPropertyList = dashBoardData!!.recentproperties
                    projectsList = dashBoardData!!.projects
                    saveAllData()
                    updateDashboardInfo()
                } catch (
                    e: Exception
                ) {
                    /*CustomAlertDialog(
                        requireContext(),
                        "Alert",
                        LitConstants.InvalidResponseMessage
                    ).show()*/
                    e.printStackTrace()
                }
            }
        }
    }

    private fun saveAllData() {
        (activity as HomeActivity).updateWhyLandableList(dashBoardData!!.whylandable)
        (activity as HomeActivity).updateProjectList(dashBoardData!!.projects)
        (activity as HomeActivity).updateRecentPropertyList(dashBoardData!!.recentproperties)
        (activity as HomeActivity).updatefeaturePropertyList(dashBoardData!!.featuredproperties)
    }


    private fun loadSearchFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            SearchFragment.newInstance(),
            SearchFragment::class.java.name
        )
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


    private fun loadPropertyDetailFragment(propertyDataModel: FeaturePropertiesDataModel?) {
        val bundle = Bundle()
        bundle.putSerializable("propertiesDetailModel", propertyDataModel)
        val propertyDetailFragment = PropertyDetailFragment.newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            PropertyDetailFragment::class.java.name
        )
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

    override fun onPropertyClick(
        action: String,
        featurePropertiesDataModel: FeaturePropertiesDataModel?
    ) {
        when (action) {
            "selectedPropertyDetail" -> {
                loadPropertyDetailFragment(featurePropertiesDataModel)
            }
        }

    }

    override fun onProjectClick(action: String, projectDataModel: ProjectsDataModel?) {
        when (action) {
            "selectedProjectDetail" -> {
                loadProjectDetailFragment(projectDataModel)
            }
        }
    }

    override fun onWhyLandableClick(action: String, whyLandableDataModel: WhyLandableDataModel?) {
        when (action) {
            "itemClicked" -> {
                if (whyLandableDataModel!!.pages == "Registration Lookup") {

                    FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Chats Fragment", null);

                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        (activity as HomeActivity).askForLogin()
                    } else {
                        val url =
                            LandableConstants.Image_URL + "app/powerbi.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                        (activity as HomeActivity).callBrowserActivity(url,"Registration Lookup")
                    }
                } else if (whyLandableDataModel.pages == "Supergroups") {
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        (activity as HomeActivity).askForLogin()
                    } else {
                        val url =
                            LandableConstants.Image_URL + "app/threadsearch.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                        (activity as HomeActivity).callBrowserActivity(url, "Supergroups Page")
                    }
                } else if (whyLandableDataModel.pages == "Auction") {
                    loadAuctionFragment()
                } else if (whyLandableDataModel.pages == "Data Analytics") {
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        (activity as HomeActivity).askForLogin()
                    } else {
                        val url =
                            LandableConstants.Image_URL + "app/analyse-trends.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                        (activity as HomeActivity).callBrowserActivity(url, "Data Analytics Page")
                    }
                } else if (whyLandableDataModel.pages == "Free Listing") {
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        (activity as HomeActivity).askForLogin()
                    } else loadPostPropertyFragment()
                } else if (whyLandableDataModel.pages == "Chat") {
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        (activity as HomeActivity).askForLogin()
                    } else {
                        loadSuperGroupFragment()
                    }
                } else if (whyLandableDataModel.pages == "News") {
                    loadNewsFragment()
                } else if (whyLandableDataModel.pages == "Blogs") {
                    loadBlogFragment()
                }
            }
        }
    }

    private fun loadNewsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            NewsFragment.newInstance(),
            NewsFragment::class.java.name
        )
    }

    private fun loadBlogFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            BlogFragment.newInstance(),
            BlogFragment::class.java.name
        )
    }

    private fun loadSuperGroupFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            ChatBoxListFragment.newInstance(),
            ChatBoxListFragment::class.java.name
        )
    }

    private fun loadAuctionFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            FragmentAuction.newInstance(),
            FragmentAuction::class.java.name
        )
    }

    private fun loadNotificationsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            NotificationsFragment.newInstance(),
            NotificationsFragment::class.java.name
        )
    }


}
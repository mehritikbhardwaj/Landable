package com.landable.app.ui.home.homeUI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentHomeBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.*
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.auction.FragmentAuction
import com.landable.app.ui.home.blogs.BlogFragment
import com.landable.app.ui.home.blogs.BlogNewsFragment
import com.landable.app.ui.home.chats.ChatBoxListFragment
import com.landable.app.ui.home.dataModels.DashBoardDataModel
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.dataModels.WhyLandableDataModel
import com.landable.app.ui.home.deeplink.AuctionDetailPageDeepLinkFragment
import com.landable.app.ui.home.deeplink.ProjectDetailDeepLinkFragment
import com.landable.app.ui.home.deeplink.PropertyDetailDeepLinkFragment
import com.landable.app.ui.home.lead.LeadFragment
import com.landable.app.ui.home.listing.MyListingFragment
import com.landable.app.ui.home.news.NewsFragment
import com.landable.app.ui.home.notifications.NotificationsFragment
import com.landable.app.ui.home.postProjectProperty.PostProjectPropertyFragment
import com.landable.app.ui.home.profile.ProfileFragment
import com.landable.app.ui.home.project.ProjectDetailFragment
import com.landable.app.ui.home.project.ViewAllProjectFragment
import com.landable.app.ui.home.project.adapter.ProjectsAdapter
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.property.ViewAllPropertyFragment
import com.landable.app.ui.home.property.adapters.FeaturePropertiesAdapter
import com.landable.app.ui.home.search.SearchFragment
import com.landable.app.ui.home.supergroups.AddSuperGroupFragment


class HomeFragment : Fragment(), PropertyDetailListener, ProjectDetailListener,
    WhyLandableClickListener, PostTypeDialog.UploadTypeListener,
    HelpTutorialDialog.SelectedListener {

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

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Dashboard", null)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setAnalyticsCollectionEnabled(true)

        updateFCM()

        if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
        } else {
            if (AppInfo.getName().isEmpty() || AppInfo.getUserEmail()
                    .isEmpty() || AppInfo.getUserMobile().isEmpty()
            ) {
                UpdateProfileDialog(activity as HomeActivity).show()
            }
        }


        binding.layoutHome.ivProfileDetailsIcon.setOnClickListener {
            showInfoPopup("Check your profile details.")
        }
        binding.layoutHome.ivBuyDetailsIcon.setOnClickListener {
            showInfoPopup("Search properties for buying/renting.")
        }

        binding.layoutHome.SellDetail.setOnClickListener {
            showInfoPopup("List your property for sale /rent.")
        }

        binding.layoutHome.ivAuctionsDetailsIcon.setOnClickListener {
            showInfoPopup("Get auction related updates for all type of properties in selected area.")
        }
        binding.layoutHome.ivRegistrationDetailsIcon.setOnClickListener {
            showInfoPopup("Check owner & registration details of any property as per govt. records.")
        }
        binding.layoutHome.ivDataDetailsIcon.setOnClickListener {
            showInfoPopup("View real-estate trends across different localities.")
        }
        binding.layoutHome.ivChatsDetailsIcon.setOnClickListener {
            showInfoPopup("Message buyer/seller without sharing your number.")
        }
        binding.layoutHome.ivBlogsDetailsIcon.setOnClickListener {
            showInfoPopup(
                "News:- Get area-specific news updates on development, regulations etc." +
                        "\n\nBlogs:- Get interesting insights on diverse aspects of real estate sector."
            )
        }


        binding.layoutHome.llProfile.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                if (AppInfo.getName().isEmpty() || AppInfo.getUserEmail()
                        .isEmpty() || AppInfo.getUserMobile().isEmpty()
                ) {
                    UpdateProfileDialog(activity as HomeActivity).show()
                } else {
                    (activity as HomeActivity).updateBottomNavigationSelection(R.id.home)
                    loadProfileFragment()
                }
            }
        }

        binding.layoutHome.llSearch.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                val url =
                    LandableConstants.Image_URL + "app/threadsearch.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                (activity as HomeActivity).callBrowserActivity(url, "Supergroups Page")
            }
        }

        binding.layoutHome.Leads.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                loadLeadFragment()
            }
        }
        binding.layoutHome.Listing.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                loadMyListingFragment()
            }
        }

        binding.layoutHome.ivListingIcon.setOnClickListener {
            showInfoPopup("Check out your posted properties")
        }

        binding.layoutHome.ivLeadsDetail.setOnClickListener {
            showInfoPopup("Check leads on your properties")
        }

        binding.layoutHome.llBlogsNews.setOnClickListener {
            loadBlogsNewsFragment()
        }
        binding.layoutHome.llAuction.setOnClickListener {
            loadAuctionFragment()
        }
        binding.layoutHome.AboutUs.setOnClickListener {
            //(activity as HomeActivity).callBrowserActivity("https://www.landable.in/about.aspx", "About us")
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.landable.in/about.aspx")
            )
            startActivity(browserIntent)
        }

        binding.layoutHome.RegistrationLookup.setOnClickListener {
            FirebaseAnalytics.getInstance((activity as HomeActivity))
                .setCurrentScreen((activity as HomeActivity), "Registration Lookup", null)
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                val url =
                    LandableConstants.Image_URL + "app/powerbi.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                (activity as HomeActivity).callBrowserActivity(url, "Registration Lookup")

            }
        }

        binding.layoutHome.DataAnalytics.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                val url =
                    LandableConstants.Image_URL + "app/analyse-trends.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                (activity as HomeActivity).callBrowserActivity(url, "Data Analytics Page")
            }
        }

        binding.layoutHome.Chat.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                loadSuperGroupFragment()
            }
        }

        binding.layoutHome.llSellRent.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                PostTypeDialog(requireActivity(), this).show()
            }
        }

        binding.layoutHome.help.setOnClickListener {
            HelpTutorialDialog(requireActivity(), this).show()
        }

        if (LandableConstants.isClickedDeepLinking && LandableConstants.deepLinkURL!!.isNotEmpty()) {
            //Handle deep link functionality
            var url = ""
            if (LandableConstants.deepLinkURL!!.contains("-pr-prop")) {
                loadDeepLinkOpenPropertyFragment()
            } else if (LandableConstants.deepLinkURL!!.contains("-pj-proj")) {
                loadDeepLinkOpenProjectFragment()
            } else if (LandableConstants.deepLinkURL!!.contains("https://www.landable.in/powerbi.aspx")) {
                FirebaseAnalytics.getInstance((activity as HomeActivity))
                    .setCurrentScreen((activity as HomeActivity), "Registration Lookup", null)

                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    url =
                        LandableConstants.Image_URL + "app/powerbi.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                    (activity as HomeActivity).callBrowserActivity(url, "Registration Lookup")

                }
            } else if (LandableConstants.deepLinkURL!!.contains("propertysearch")) {
                loadSearchFragment()
            } else if (LandableConstants.deepLinkURL!!.contains("-th-")) {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    val threadId = LandableConstants.deepLinkURL!!.substringAfter("-th-")

                    url =
                        LandableConstants.Image_URL + "app/appthreaddetails.aspx?th=$threadId" + "&uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                    (activity as HomeActivity).callBrowserActivity(url, "Supergroups Page")
                }
            } else if (LandableConstants.deepLinkURL!!.contains("-ac-")) {
                loadDeepLinkOpenAuctionFragment()
            } else if (LandableConstants.deepLinkURL!!.contains("analyse-trends")) {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    url =
                        LandableConstants.Image_URL + "app/analyse-trends.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                    (activity as HomeActivity).callBrowserActivity(url, "Data Analytics Page")
                }
            } else (activity as HomeActivity).callBrowserActivity(
                LandableConstants.deepLinkURL!!,
                ""
            )
            LandableConstants.isClickedDeepLinking = false
            //   CustomAlertDialog(this, "Deep link", LitConstants.deepLinkURL!!).show()
        } /*else {
            if (featurePropertyList.size == 0 || recentPropertyList.size == 0 || projectsList.size == 0) {
                getDashboardInfo()
            } else updateDashboardInfo()*/


        binding.ivSideNavigation.setOnClickListener {
            (activity as HomeActivity).openDrawer()
        }

        binding.ivNotifications.setOnClickListener {
            loadNotificationsFragment()
        }

        //hideLeftScrollButtons()

        /* binding.rightScrollLandable.setOnClickListener {
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
                 *//* if (whyLandableVisiblePosition == 1) {
                     binding.leftScrollLandable.visibility = View.GONE
                 }*//*
            }
        }
*/

        /* binding.editText.setOnClickListener {
             loadSearchFragment()
         }

         binding.viewAllFeaturedProperties.setOnClickListener {
             (activity as HomeActivity).postUserTrackingModel(
                 HomeActivity.PostUserTrackingModel(
                     "View all featured property page",
                     "Visit",
                     "Visit",
                     "Visit",
                     "",
                     ""
                 )
             )
             if (featurePropertyList.size != 0) {
                 loadViewAllFeaturedProperties(featurePropertyList)
             }
         }

         binding.viewAllProjects.setOnClickListener {
             (activity as HomeActivity).postUserTrackingModel(
                 HomeActivity.PostUserTrackingModel(
                     "View all project page",
                     "Visit",
                     "Visit",
                     "Visit",
                     "",
                     ""
                 )
             )
             if (projectsList.size != 0) {
                 loadViewAllProjects()
             }
         }

         binding.viewAllRecentProperties.setOnClickListener {
             (activity as HomeActivity).postUserTrackingModel(
                 HomeActivity.PostUserTrackingModel(
                     "View all recent page",
                     "Visit",
                     "Visit",
                     "Visit",
                     "",
                     ""
                 )
             )
             if (recentPropertyList.size != 0) {
                 loadViewAllFeaturedProperties(recentPropertyList)
             }
         }*/

        binding.llPostProperty.setOnClickListener {
            if (AppInfo.getSCode() == "" || AppInfo.getSCode() == "0") {
                (activity as HomeActivity).askForLogin()
            } else {
                PostTypeDialog(requireActivity(), this).show()
            }
        }



        if ((activity as HomeActivity).propertyID != "" && AppInfo.getSCode() != "") {
            postContactOwner(
                (activity as HomeActivity).propertyID,
                (activity as HomeActivity).contactType
            )
            (activity as HomeActivity).propertyID = ""
        }

        return binding.root
    }

    private fun loadLeadFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            LeadFragment.newInstance(),
            LeadFragment::class.java.name
        )
    }

    private fun loadDeepLinkOpenPropertyFragment() {
        val bundle = Bundle()
        val albumJoinDeepLink = PropertyDetailDeepLinkFragment.newInstance()
        albumJoinDeepLink.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            (activity as HomeActivity).supportFragmentManager,
            HomeActivity().getHomePageContainerId(),
            PropertyDetailDeepLinkFragment.newInstance(),
            PropertyDetailDeepLinkFragment::class.java.name
        )
    }

    private fun loadBlogsNewsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            BlogNewsFragment.newInstance(),
            BlogNewsFragment::class.java.name
        )
    }


    private fun loadDemovideoFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            DemoVideosFragment.newInstance(),
            DemoVideosFragment::class.java.name
        )
    }


    private fun loadMyListingFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            MyListingFragment.newInstance(),
            MyListingFragment::class.java.name
        )
    }

    private fun loadDeepLinkOpenProjectFragment() {
        val bundle = Bundle()
        val projectDeepLink = ProjectDetailDeepLinkFragment.newInstance()
        projectDeepLink.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            (activity as HomeActivity).supportFragmentManager,
            HomeActivity().getHomePageContainerId(),
            ProjectDetailDeepLinkFragment.newInstance(),
            ProjectDetailDeepLinkFragment::class.java.name
        )
    }

    private fun loadDeepLinkOpenAuctionFragment() {
        val bundle = Bundle()
        val auctionDeepLink = AuctionDetailPageDeepLinkFragment.newInstance()
        auctionDeepLink.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            (activity as HomeActivity).supportFragmentManager,
            HomeActivity().getHomePageContainerId(),
            AuctionDetailPageDeepLinkFragment.newInstance(),
            AuctionDetailPageDeepLinkFragment::class.java.name
        )
    }


    private fun scrollRightRecyclerView(recyclerView: RecyclerView) {
        recyclerView.smoothScrollToPosition(whyLandableVisiblePosition + 2)
        whyLandableVisiblePosition += 1
    }

    private fun scrollLeftRecyclerView(recyclerView: RecyclerView) {
        recyclerView.smoothScrollToPosition(whyLandableVisiblePosition - 2)
        whyLandableVisiblePosition -= 1

    }

    private fun loadProfileFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            ProfileFragment.newInstance(),
            ProfileFragment::class.java.name
        )
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


    private fun loadAuctionFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            FragmentAuction.newInstance(),
            FragmentAuction::class.java.name
        )
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

        binding.rvWhyLandable.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvWhyLandable.adapter =
            WhyLandableAdapter(whyLandableList, this)
        /*  binding.rvWhyLandable.layoutManager =
              LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
          binding.rvWhyLandable.adapter = WhyLandableAdapter(whyLandableList, this)
     */
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
        /* updateFeaturePropertiesList()
         updateRecentPropertiesList()
         updateProjectsList()*/
    }

    private fun getDashboardInfo() {
        // Show progress bar
        /*  progressDialog = CustomProgressDialog(requireContext())
          progressDialog!!.show()*/
        val dashboardResponse = RegisterRepository().getDashboardInfo()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            //  progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    dashBoardData = ParseResponse.parseDashboardResponse(it.toString())
                    whyLandableList = dashBoardData!!.whylandable
                    /*    featurePropertyList = dashBoardData!!.featuredproperties
                        recentPropertyList = dashBoardData!!.recentproperties
                        projectsList = dashBoardData!!.projects*/
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

                    FirebaseAnalytics.getInstance((activity as HomeActivity))
                        .setCurrentScreen((activity as HomeActivity), "Registration Lookup", null)

                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        (activity as HomeActivity).askForLogin()
                    } else {
                        val url =
                            LandableConstants.Image_URL + "app/powerbi.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                        (activity as HomeActivity).callBrowserActivity(url, "Registration Lookup")
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
                    } else /*loadPostPropertyFragment()*/ PostTypeDialog(
                        requireActivity(),
                        this
                    ).show()

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

    private fun loadNotificationsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            NotificationsFragment.newInstance(),
            NotificationsFragment::class.java.name
        )
    }

    override fun onClickButtonForUploadType(typeOfUpload: String) {
        when (typeOfUpload) {
            "llPostProperty" -> {
                loadPostPropertyFragment()
            }
            "llPostSupergroup" -> {
                loadAddPostFragment()
            }
        }
    }

    private fun loadAddPostFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            AddSuperGroupFragment.newInstance(),
            AddSuperGroupFragment::class.java.name
        )
    }


    private fun updateFCM() {
        val response = RegisterRepository().updateFCM()
        response.observe(viewLifecycleOwner) {
            // hide progress bar
            // parse dashboard info
            if (it != "null") {
            }
        }
    }


    override fun onClickButton(clicked: String) {
        when (clicked) {
            "llFAQ" -> {
                (activity as HomeActivity).callBrowserActivity(
                    "https://www.landable.in/app/faq.aspx",
                    "Faq page"
                )
                /* val browserIntent = Intent(
                     Intent.ACTION_VIEW,
                     Uri.parse("https://www.landable.in/app/faq.aspx")
                 )
                 startActivity(browserIntent)*/
            }
            "llDemo" -> {
                loadDemovideoFragment()
                //showInfoPopup("Coming Soon")
            }
            "llWhatsapp" -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=918448856325&text=Hello,%20I%20have%20a%20question%20about%20")
                )
                startActivity(browserIntent)
            }
            "llCall" -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel: 8448856325")
                startActivity(intent)
            }
        }
    }

    private fun showInfoPopup(msg: String) {
        CustomAlertDialog(requireContext(), "Info", msg).show()
    }

}
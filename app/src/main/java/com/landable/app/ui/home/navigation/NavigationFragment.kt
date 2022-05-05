package com.landable.app.ui.home.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.bumptech.glide.Glide
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.FragmentHelper
import com.landable.app.common.IListener
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentNavigationDrawerLayoutBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.auction.FragmentAuction
import com.landable.app.ui.home.blogs.BlogFragment
import com.landable.app.ui.home.chats.ChatBoxListFragment
import com.landable.app.ui.home.dataModels.UserProfileDataModel
import com.landable.app.ui.home.news.NewsFragment
import com.landable.app.ui.home.notifications.NotificationsFragment
import com.landable.app.ui.home.profile.ProfileFragment
import com.landable.app.ui.home.search.SearchFragment
import com.landable.app.ui.home.supergroups.AddSuperGroupFragment
import com.landable.app.ui.home.supergroups.SuperGroupsFragment
import org.json.JSONObject

class NavigationFragment : Fragment(), IListener {

    private lateinit var binding: FragmentNavigationDrawerLayoutBinding
    private var userData = ArrayList<UserProfileDataModel>()

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_navigation_drawer_layout,
            container,
            false
        )
        AppInfo.setContext(requireContext())
        binding.tvName.text = AppInfo.getName()
        binding.tvCustomerType.text = AppInfo.getCustomerType()
        Glide.with(binding.profilePicture.context)
            .load(AppInfo.getUserImage())
            .placeholder(R.drawable.user_circle)
            .into(binding.profilePicture)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUnreadMessage()
        userData = (activity as HomeActivity).getUserData()
        if (userData.isNotEmpty()) {
            updatedUserInfo()
        } else {
            if (AppInfo.getUserId().isBlank() && AppInfo.getUserId() != "0") {
                getUserProfileData()
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(NavigationViewModel::class.java)
        binding.navigationViewModel = viewModel

        viewModel.listener = this

    }

    override fun onStarted(action: String) {
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        (activity as HomeActivity).hideDrawer()
        when (action) {
            "onDashboardClick" -> {

            }
            "onSearchPropertyClick" -> {
                loadSearchFragment()
            }
            "onAuctionsClick" -> {
                loadAuctionFragment()
            }
            "onSearchPostClick" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    val url =
                        LandableConstants.Image_URL + "app/threadsearch.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                    (activity as HomeActivity).callBrowserActivity(url, "Supergroups Fragment")
                }
            }
            "onAddPosts" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    (activity as HomeActivity).updateBottomNavigationSelection(R.id.home)
                    loadAddPostFragment()
                }
            }
            "onMyPostsClick" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    (activity as HomeActivity).updateBottomNavigationSelection(R.id.home)
                    loadMyPostsFragment()
                }
            }
            "onChatClick" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    loadSuperGroupFragment()
                }
            }
            "onPropertyRegistrationLookup" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    val url =
                        LandableConstants.Image_URL + "app/powerbi.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                    (activity as HomeActivity).callBrowserActivity(
                        url,
                        "PropertyRegistrationLookup Page"
                    )
                }
            }
            "onAnalyzeTrendsClick" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    val url =
                        LandableConstants.Image_URL + "app/analyse-trends.aspx?uid=" + AppInfo.getUserId() + "&ucode=" + AppInfo.getSCode()
                    (activity as HomeActivity).callBrowserActivity(url, "AnalyzeTrends Page")
                }
            }
            "onNewsClick" -> {
                loadNewsFragment()
            }
            "onBlogsClick" -> {
                loadBlogFragment()
            }
            "onNotificationsClick" -> {
                loadNotificationsFragment()
            }
            "onProfileCLick" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).askForLogin()
                } else {
                    (activity as HomeActivity).updateBottomNavigationSelection(R.id.home)
                    loadProfileFragment()
                }
            }
            "onSearchMapClick" -> {
                val url = "https://www.landable.in/auctionmap.aspx?key=&ct=0&st=0&" +
                        "city=,status=Active,locality=,locality=,beforedate=,bankname=," +
                        "borrower=,costfrom=0,costto=0,areafrom=0,areato=10000000,emddate="
                (activity as HomeActivity).callBrowserActivity(url, "Search Map Page")
            }

        }
    }

    override fun onFailure(message: String, action: String) {
    }

    fun getUserProfileData() {
        val userDataResponse = RegisterRepository().getUserProfileData()
        userDataResponse.observe(viewLifecycleOwner) {
            // parse faces list
            if (it.toString() != "null") {
                try {
                    userData = ParseResponse.parseUserProfileResponse(it.toString())
                    (activity as HomeActivity).updateUserData(userData)
                    updatedUserInfo()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updatedUserInfo() {
        binding.profilePicture.load(LandableConstants.Image_URL + userData[0].profilepic)
        binding.tvName.text = userData[0].name
        binding.tvCustomerType.text = userData[0].customertype
        updateSharedPreference(
            userData[0].name,
            LandableConstants.BASE_URL + userData[0].profilepic,
            userData[0].customertype
        )
    }

    private fun updateSharedPreference(
        name: String,
        profilePicture: String,
        customerType: String
    ) {
        AppInfo.setName(name)
        AppInfo.setUserEmail(userData[0].email)
        AppInfo.setUserImage(profilePicture)
        AppInfo.setCustomerType(customerType)
    }

    private fun loadProfileFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            ProfileFragment.newInstance(),
            ProfileFragment::class.java.name
        )
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

    private fun loadNotificationsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            NotificationsFragment.newInstance(),
            NotificationsFragment::class.java.name
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

    private fun loadSearchFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            SearchFragment.newInstance(),
            SearchFragment::class.java.name
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

    private fun loadAddPostFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            AddSuperGroupFragment.newInstance(),
            AddSuperGroupFragment::class.java.name
        )
    }

    private fun loadMyPostsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            SuperGroupsFragment.newInstance(),
            SuperGroupsFragment::class.java.name
        )
    }


    private fun getUnreadMessage() {
        val response = RegisterRepository().get_Unreadmsg()
        response.observe(viewLifecycleOwner) {

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
                        val jsonObj = JSONObject(it)
                        val unread = jsonObj.getInt("unread")
                        if (unread > 0) {
                            binding.newMessagesCounter.visibility = View.VISIBLE
                            binding.newMessagesCounter.text = "$unread new messages"
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

}
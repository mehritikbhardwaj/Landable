package com.landable.app.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.FragmentHelper
import com.landable.app.common.IListener
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentProfileBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.agent.AgentsFragment
import com.landable.app.ui.home.changePassword.ChangePasswordFragment
import com.landable.app.ui.home.dataModels.UserProfileDataModel
import com.landable.app.ui.home.favorites.FavoritesFragment
import com.landable.app.ui.home.lead.LeadFragment
import com.landable.app.ui.home.listing.MyListingFragment
import com.landable.app.ui.home.login.OTPLoginFragment
import com.landable.app.ui.home.myActivity.MyActivityFragment
import com.landable.app.ui.home.postProjectProperty.PostProjectPropertyFragment
import com.landable.app.ui.home.savedSearches.SavedSearchesFragment
import com.landable.app.ui.home.shortUrl.ShortUrlFragment

class ProfileFragment : Fragment(), IListener {

    private lateinit var binding: FragmentProfileBinding
    private var userData = ArrayList<UserProfileDataModel>()
    private var progressDialog: CustomProgressDialog? = null

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideTopbar()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Profile")
        (activity as HomeActivity).hideBottomNavigation()

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Profile Fragment", null);

        if (AppInfo.getCustomerType() == "Individual") {
            binding.llAgents.visibility = View.GONE
            binding.tvPostProperty.text = "Post Property"
        }

        Glide.with(binding.circleImageView.context)
            .load(AppInfo.getUserImage())
            .placeholder(R.drawable.user_circle)
            .into(binding.circleImageView)

        binding.tvName.text = AppInfo.getName()
        binding.tvCustomerType.text = AppInfo.getCustomerType()

        getUserProfileData()


        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.profileOtpViewModel = viewModel

        viewModel.listener = this

    }

    private fun loadLeadFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            LeadFragment.newInstance(),
            LeadFragment::class.java.name
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

    private fun loadAgentsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            AgentsFragment.newInstance(),
            AgentsFragment::class.java.name
        )
    }

    private fun loadShortUrlFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            ShortUrlFragment.newInstance(),
            ShortUrlFragment::class.java.name
        )
    }

    private fun loadSavedSearchesFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            SavedSearchesFragment.newInstance(),
            SavedSearchesFragment::class.java.name
        )
    }

    private fun loadChangePasswordFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            ChangePasswordFragment.newInstance(),
            ChangePasswordFragment::class.java.name
        )
    }

    private fun loadLoginFragment() {
        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            OTPLoginFragment.newInstance(),
            OTPLoginFragment::class.java.name
        )
    }

    private fun loadActivityFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            MyActivityFragment.newInstance(),
            MyActivityFragment::class.java.name
        )
    }

    private fun loadEditProfileFragment() {
        val bundle = Bundle()
        bundle.putSerializable("profileData", userData)
        val profileFragment = EditProfileFragment.newInstance()
        profileFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            profileFragment,
            EditProfileFragment::class.java.name

        )
    }

    override fun onStarted(action: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        when (action) {
            "onEditProfileClick" -> {
                loadEditProfileFragment()
            }
            "onAgentClick" -> {
                loadAgentsFragment()
            }
            "onShortURLClick" -> {
                loadShortUrlFragment()
            }
            "onPostPropertyProjectClick" -> {
                loadPostPropertyFragment()
            }
            "onMyListingClick" -> {
                loadMyListingFragment()
            }
            "onLeadsClick" -> {
                loadLeadFragment()
            }
            "onFavoritesClick" -> {
                loadShortlistedFragment()
            }
            "onActivityClick" ->
                loadActivityFragment()

            "onSavedSearchesClick" -> {
                loadSavedSearchesFragment()
            }
            "onChangePasswordClick" -> {
                loadChangePasswordFragment()
            }
            "onLogoutClick" -> {
                AppInfo.clearUserData()
                loadLoginFragment()
            }

        }
    }

    override fun onFailure(message: String, action: String) {
        TODO("Not yet implemented")
    }

    private fun loadShortlistedFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            FavoritesFragment.newInstance(),
            FavoritesFragment::class.java.name
        )
    }

    private fun getUserProfileData() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val userDataResponse = RegisterRepository().getUserProfileData()
        userDataResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancel()
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
        Glide.with(binding.circleImageView.context)
            .load(LandableConstants.Image_URL + userData[0].profilepic)
            .placeholder(R.drawable.user_circle)
            .into(binding.circleImageView)

        binding.tvName.text = userData[0].name
        binding.tvCustomerType.text = userData[0].customertype
        updateSharedPreference(
            userData[0].name,
            LandableConstants.Image_URL + userData[0].profilepic,
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


}
package com.landable.app.ui.home.agent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentAgencyProfileBinding
import com.landable.app.ui.BrowserActivity
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.browser.WebViewActivity
import com.landable.app.ui.home.chats.ChatsFragment
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.dataModels.UserDetailDataModel
import com.landable.app.ui.home.dataModels.Useractivity
import com.landable.app.ui.home.project.ProjectDetailFragment
import com.landable.app.ui.home.project.adapter.ProjectsAdapter
import com.landable.app.ui.home.project.ViewAllProjectFragment
import com.landable.app.ui.home.property.adapters.FeaturePropertiesAdapter
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.property.ViewAllPropertyFragment

class AgencyProfileFragment : Fragment(), PropertyDetailListener, ProjectDetailListener,
    AgencyActivityClickListener {

    private lateinit var binding: FragmentAgencyProfileBinding
    private var profileData = UserDetailDataModel()
    private var progressDialog: CustomProgressDialog? = null
    private var Projects = ArrayList<ProjectsDataModel>()
    private var Properties = ArrayList<FeaturePropertiesDataModel>()
    private var Useractivities = ArrayList<Useractivity>()
    private var agentID: Int = 0

    companion object {
        fun newInstance() = AgencyProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting album model from other fragments
        agentID = requireArguments().getInt("agentID")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Agency Profile")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_agency_profile, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Agency Profile Fragment", null);

        getAgencyProfileDetails(agentID)

        binding.viewALlPRoperties.setOnClickListener {
            if (Properties.size != 0) {
                loadViewAllFeaturedProperties()
            }
        }

        binding.ivChat.setOnClickListener {
            loadChatsFragment("Agent",AppInfo.getUserId().toInt(),agentID)
        }

        binding.viewAllProjects.setOnClickListener {
            if (Projects.size != 0) {
                loadViewAllProjects()
            }
        }

        binding.ivContact.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = ContactOwnerDialogFragment(
                profileData.profile.mobile,profileData.profile.name,profileData.profile.email,profileData.profile.userid)
            dialogFragment.show(fm, "")
        }
        return binding.root
    }

    private fun loadViewAllProjects() {
        val bundle = Bundle()
        bundle.putSerializable("allProjects", Projects)
        val allProjectFragment = ViewAllProjectFragment.newInstance()
        allProjectFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            allProjectFragment,
            ViewAllProjectFragment::class.java.name

        )
    }
    private fun loadChatsFragment(type: String, id: Int, toUserID: Int) {
        val bundle = Bundle()
        bundle.putString("type", type)
        bundle.putInt("id", id)
        bundle.putInt("toUserID", toUserID)
        bundle.putBoolean("comingfromchat",false)

        val chatsFragment = ChatsFragment.newInstance()
        chatsFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            chatsFragment,
            ChatsFragment::class.java.name
        )
    }

    private fun loadViewAllFeaturedProperties() {
        val bundle = Bundle()
        bundle.putSerializable("allProperties", Properties)
        val allPropertiesFragment = ViewAllPropertyFragment.newInstance()
        allPropertiesFragment.arguments = bundle

        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            allPropertiesFragment,
            ViewAllPropertyFragment::class.java.name

        )
    }

    private fun getAgencyProfileDetails(id: Int) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val propertyResponse = RegisterRepository().getUserDetails(id)
        propertyResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    profileData = ParseResponse.parseUserDetailsResponse(it.toString())
                    Projects = profileData.Projects
                    Properties = profileData.Properties
                    Useractivities = profileData.Useractivities

                    updateUIData()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUIData() {

        binding.ivProfilePicture.load(LandableConstants.Image_URL + profileData.profile.profilepic)
        binding.tvAgencyName.text = profileData.profile.agencyname
        binding.tvName.text = profileData.profile.name
        binding.tvLocation.text = profileData.profile.cityname
        binding.tvOperatingSince.text =
            "Operating since " + profileData.profile.Operatingsince.toString()
        binding.tvSaleCount.text = "For Sale : " + profileData.profile.salecount
        binding.tvSaleCost.text =
            "Price Range : Rs " + profileData.profile.salemincost + " - " + profileData.profile.salemaxcost
        binding.tvRentCount.text = "For Rent : " + profileData.profile.rentcount
        binding.tvRentCost.text =
            "Price Range : Rs " + profileData.profile.rentmincost + " - " + profileData.profile.rentmaxcost


        binding.rvActivity.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvActivity.adapter = AgentActivityAdapter(Useractivities, this)

        binding.rvFeaturedProperties.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedProperties.adapter = FeaturePropertiesAdapter(Properties, this)

        binding.rvFeaturedProjects.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedProjects.adapter = ProjectsAdapter(Projects, this,requireContext())

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

    private fun loadPropertyDetailFragment(propertyDataModel: FeaturePropertiesDataModel?) {
        val bundle = Bundle()
        bundle.putSerializable("propertiesDetailModel", propertyDataModel)
        val propertyDetailFragment = PropertyDetailFragment.newInstance()
        propertyDetailFragment.arguments = bundle

        FragmentHelper().replaceFragment(
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

        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            propertyDetailFragment,
            ProjectDetailFragment::class.java.name
        )
    }

    override fun onActivityClick(action: String, activity: Useractivity?) {
        when (action) {
            "activityClick" -> {
                if (activity != null) {
                    callBrowserActivity(LandableConstants.Image_URL + activity.link)
                }
            }
        }
    }

    private fun callBrowserActivity(url: String) {
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", "Landable")
        startActivity(intent) //Intent in = new Intent(this,)
    }


}
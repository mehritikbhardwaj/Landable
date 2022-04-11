package com.landable.app.ui.home.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.FragmentHelper
import com.landable.app.common.PropertyDetailListener
import com.landable.app.databinding.FragmentViewAllBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.agent.ContactOwnerDialogFragment
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.search.SearchPropertyAdapter

class ViewAllPropertyFragment : Fragment(), PropertyDetailListener {

    private lateinit var binding: FragmentViewAllBinding
    private var propertyList = ArrayList<FeaturePropertiesDataModel>()


    companion object {
        fun newInstance() = ViewAllPropertyFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        propertyList =
            requireArguments().getSerializable("allProperties") as ArrayList<FeaturePropertiesDataModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Properties")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_all, container, false)

        updateFeaturePropertiesList()

        return binding.root
    }

    private fun updateFeaturePropertiesList() {
        binding.rvProperties.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvProperties.adapter = SearchPropertyAdapter(propertyList, this)
    }


    override fun onPropertyClick(
        action: String,
        featurePropertiesDataModel: FeaturePropertiesDataModel?
    ) {
        when (action) {
            "selectedPropertyDetail" -> {
                loadPropertyDetailFragment(featurePropertiesDataModel)
            }
            "ViewAgencyProfile" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).propertyID = featurePropertiesDataModel!!.propertyid
                    (activity as HomeActivity).contactType = "Property"
                    (activity as HomeActivity).agentID = featurePropertiesDataModel.addedbyid
                    (activity as HomeActivity).askForLogin()
                } else {
                    loadAgentProfileFragment(featurePropertiesDataModel!!.addedbyid)
                }
            }
            "contactOwner" -> {
                if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                    (activity as HomeActivity).propertyID = featurePropertiesDataModel!!.propertyid
                    (activity as HomeActivity).contactType = "Property"
                    (activity as HomeActivity).agentID = featurePropertiesDataModel.addedbyid
                    (activity as HomeActivity).askForLogin()
                } else {
                    val fm = requireActivity().supportFragmentManager
                    val dialogFragment = ContactOwnerDialogFragment(
                        featurePropertiesDataModel!!.addedbyid,
                        featurePropertiesDataModel.name
                    )
                    dialogFragment.show(fm, "")
                }
            }
        }
    }


    private fun loadAgentProfileFragment(
        id: Int,
    ) {
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

}
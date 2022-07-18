package com.landable.app.ui.home.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.PropertyDetailListener
import com.landable.app.databinding.FragmentPostedPropertyBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.home.agent.AgencyProfileFragment
import com.landable.app.ui.home.agent.ContactOwnerDialogFragment
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.search.SearchPropertyAdapter

class FavoritePropertyFragment : Fragment(), PropertyDetailListener {

    private lateinit var binding: FragmentPostedPropertyBinding
    private var propertyList = ArrayList<FeaturePropertiesDataModel>()


    companion object {
        fun newInstance() = FavoritePropertyFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting album model from other fragments
        propertyList =
            requireArguments().getSerializable("propertyList") as ArrayList<FeaturePropertiesDataModel>
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_property, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Favourite Property Fragment", null);

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Favourite Property Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))
        if(propertyList.size == 0){
            binding.tvNoResult.visibility = View.VISIBLE
        }else {
            binding.rvPostedProperty.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvPostedProperty.adapter = SearchPropertyAdapter(propertyList, this)
        }
        return binding.root
    }

    override fun onPropertyClick(
        action: String,
        featurePropertiesDataModel: FeaturePropertiesDataModel?
    ) {
        when (action) {
            "selectedPropertyDetail" -> {
                loadPropertyDetailFragment(featurePropertiesDataModel)
            }
            "contactOwner" -> {
                val fm = requireActivity().supportFragmentManager
                val dialogFragment = ContactOwnerDialogFragment(
                    "",
                    featurePropertiesDataModel!!.name,
                    "", featurePropertiesDataModel.addedbyid
                )
                dialogFragment.show(fm, "")

            }
            "ViewAgencyProfile" -> {
                loadAgentProfileFragment(featurePropertiesDataModel!!.addedbyid)

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
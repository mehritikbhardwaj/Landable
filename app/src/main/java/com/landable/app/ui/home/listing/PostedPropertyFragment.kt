package com.landable.app.ui.home.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.PropertyDetailListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.FragmentPostedPropertyBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.postProjectProperty.PostProjectPropertyFragment
import com.landable.app.ui.home.property.PropertyDetailFragment

class PostedPropertyFragment : Fragment(), PropertyDetailListener {

    private lateinit var binding: FragmentPostedPropertyBinding
    private var propertyList = ArrayList<FeaturePropertiesDataModel>()


    companion object {
        fun newInstance() = PostedPropertyFragment()
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
        (activity as HomeActivity).enableBackButton("My listing")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_property, container, false)


        if (propertyList.size == 0) {
            binding.tvNoResult.visibility = View.VISIBLE
        } else {
            binding.rvPostedProperty.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvPostedProperty.adapter = PostedPropertyAdapter(propertyList, this)

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
            "deleteSelectedPRoperty" -> {
                deleteProperty(featurePropertiesDataModel!!.id)
            }
            "editSelectedProperty" -> {
                loadPropertyEditFragment(featurePropertiesDataModel)
            }
        }
    }

    private fun deleteProperty(id: Int) {
        val deleteResponse = RegisterRepository().getDeleteProperty(id)
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

    private fun loadPropertyEditFragment(propertyDataModel: FeaturePropertiesDataModel?) {
        val bundle = Bundle()
        bundle.putString("isComingForWhichEditType", "PropertyEdit")
        bundle.putSerializable("propertyDataModel", propertyDataModel)
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
}
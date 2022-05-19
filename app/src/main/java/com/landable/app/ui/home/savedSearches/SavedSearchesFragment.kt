package com.landable.app.ui.home.savedSearches

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
import com.landable.app.common.LandableConstants
import com.landable.app.common.SavedSearchListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentSavedSearchBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.SavedSearchDataModelItem

class SavedSearchesFragment : Fragment(), SavedSearchListener {

    private lateinit var binding: FragmentSavedSearchBinding
    private var progressDialog: CustomProgressDialog? = null
    private var savedSearchesList = ArrayList<SavedSearchDataModelItem>()

    companion object {
        fun newInstance() = SavedSearchesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Saved Search")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_saved_search, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Saved Searches List Fragment", null);

        getSavedSearhcesList()
        return binding.root
    }


    private fun getSavedSearhcesList() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getSavedsearchlist()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    savedSearchesList = ParseResponse.parseSavedSearchesList(it.toString())
                    updateChatBoxListUI()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateChatBoxListUI() {
        binding.rvSavedSearches.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSavedSearches.adapter = SavedSearchAdapter(savedSearchesList,this)
    }


    private fun deleteSavedSearches(id: Int) {
        val deleteResponse = RegisterRepository().getDeletesavedsearch(id)
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
                        savedSearchesList.clear()
                        getSavedSearhcesList()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onClick(action: String, model: SavedSearchDataModelItem) {
        when(action){
            "delete"->{
                deleteSavedSearches(model.id)
            }
        }
    }

}
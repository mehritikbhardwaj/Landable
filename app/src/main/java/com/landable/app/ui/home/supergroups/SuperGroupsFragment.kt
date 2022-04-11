package com.landable.app.ui.home.supergroups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.landable.app.R
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostedProjectBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.SuperGroupsDataModelItem

class SuperGroupsFragment : Fragment() {

    private lateinit var binding: FragmentPostedProjectBinding
    private var progressDialog: CustomProgressDialog? = null
    private var superGroupsList = ArrayList<SuperGroupsDataModelItem>()


    companion object {
        fun newInstance() = SuperGroupsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("My Posts")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_project, container, false)

        getMySuperGroups()

        return binding.root
    }

    private fun getMySuperGroups() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getMySuperGroups()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    superGroupsList = ParseResponse.parseSuperGroupsList(it.toString())
                    updateSuperGroupsUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateSuperGroupsUI() {
        binding.rvPostedProject.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPostedProject.adapter = SuperGroupsAdapter(superGroupsList)
    }
}
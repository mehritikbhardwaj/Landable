package com.landable.app.ui.home.myActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.ActivityClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostedPropertyBinding
import com.landable.app.ui.BrowserActivity
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.browser.WebViewActivity
import com.landable.app.ui.home.dataModels.ActivityDataModel

class MyActivityFragment : Fragment(), ActivityClickListener {

    private lateinit var binding: FragmentPostedPropertyBinding
    private var progressDialog: CustomProgressDialog? = null
    private var activityListing: ArrayList<ActivityDataModel>? = null


    companion object {
        fun newInstance() = MyActivityFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton(" My Activity")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_posted_property, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Activity Fragment", null);

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Activity Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))
        getActivityList()
        return binding.root
    }

    private fun getActivityList() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val newsResponse = RegisterRepository().getMyActivity()
        newsResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    activityListing = ParseResponse.parseMyActivityList(it.toString())
                    updateNewsUI()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun updateNewsUI() {
        if(activityListing!!.size ==0){
            binding.tvNoResult.visibility = View.VISIBLE
        }else{
            binding.rvPostedProperty.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvPostedProperty.adapter = ActivityAdapter(activityListing!!, this)

        }
       }

    override fun onActivityClick(action: String, activity: ActivityDataModel?) {
        when (action) {
            "activityClick" -> {
                callBrowserActivity(LandableConstants.Image_URL + activity!!.link)
            }
        }
    }

    private fun callBrowserActivity(url: String) {
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", "Landable")
        startActivity(intent)
    }
}
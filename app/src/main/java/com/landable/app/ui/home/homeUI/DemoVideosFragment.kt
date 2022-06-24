package com.landable.app.ui.home.homeUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.VideoClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentBlogsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog

class DemoVideosFragment : Fragment(), VideoClickListener {

    private lateinit var binding: FragmentBlogsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var videosListing: ArrayList<DemoVideoDataModel>? = null

    companion object {
        fun newInstance() = DemoVideosFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Demo Videos")
        (activity as HomeActivity).hideBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blogs, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Demo Videos", null)

        getVideosList()

        return binding.root
    }

    private fun getVideosList() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val blogsResponse = RegisterRepository().getVideos()
        blogsResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    videosListing = ParseResponse.parseVideoList(it.toString())
                    updateBlogsUI()

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateBlogsUI() {
        binding.rvBlogs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvBlogs.adapter = VideoAdpater(videosListing!!, this)
    }

    override fun onvideoClick(action: String, path: String) {
        when (action) {
            "click" -> {
                (activity as HomeActivity).callBrowserActivity(path, "Demo Video")

            }
        }
    }


}
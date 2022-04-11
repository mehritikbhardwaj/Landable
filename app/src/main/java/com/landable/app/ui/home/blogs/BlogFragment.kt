package com.landable.app.ui.home.blogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.landable.app.R
import com.landable.app.common.BlogsClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentBlogsBinding
import com.landable.app.ui.BrowserActivity
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.BlogDataModel

class BlogFragment : Fragment(), BlogsClickListener {

    private lateinit var binding: FragmentBlogsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var blogsListing: ArrayList<BlogDataModel>? = null

    companion object {
        fun newInstance() = BlogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Blogs")
        (activity as HomeActivity).hideBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blogs, container, false)

        getBlogsList()
        
        return binding.root
    }

    private fun getBlogsList() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val blogsResponse = RegisterRepository().getBlogsList()
        blogsResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    blogsListing = ParseResponse.parseBlogsList(it.toString())
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
        binding.rvBlogs.adapter = BlogsAdapter(blogsListing!!, this)
    }

    override fun onBlogClick(action: String, blogsDataModel: BlogDataModel) {
        when (action) {
            "blogClick" -> {
                (activity as HomeActivity).callBrowserActivity(blogsDataModel.link)

            }
        }
    }


}
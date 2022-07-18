package com.landable.app.ui.home.news

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.NewsClickListener
import com.landable.app.common.Utility
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentNewsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.NewsDataModelItem

class NewsFragment : Fragment(), NewsClickListener {

    private lateinit var binding: FragmentNewsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var newsListing: ArrayList<NewsDataModelItem>? = null

    companion object {
        fun newInstance() = NewsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Blogs & News")
        (activity as HomeActivity).hideBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "News Fragment", null)

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "News Page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            ))
        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)

        binding.editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (binding.editText.text.trim().isNotBlank()) {
                    getNewsList(1, binding.editText.text.toString())
                } else Toast.makeText(
                    requireContext(),
                    "Please type something to search!!",
                    Toast.LENGTH_LONG
                ).show()
            } else Toast.makeText(
                requireContext(),
                "Please type something to search!!",
                Toast.LENGTH_LONG
            ).show()
            return@OnEditorActionListener true
        })

        binding.ivSearch.setOnClickListener {
            if (!binding.editText.text.isNullOrEmpty()) {
                getNewsList(1, binding.editText.text.toString())

            } else Toast.makeText(
                requireContext(),
                "Please type something to search!!",
                Toast.LENGTH_LONG
            ).show()

        }

        getNewsList(1, "na")

        return binding.root
    }

    private fun getNewsList(pageIndex: Int, keyword: String) {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val newsResponse = RegisterRepository().getNewsList(pageIndex, keyword)
        newsResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    newsListing = ParseResponse.parseNewsList(it.toString())
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
        binding.rvNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNews.adapter = NewsListAdapter(newsListing!!, this)
    }

    override fun onNewsClick(action: String, newsDataModel: NewsDataModelItem) {
        when (action) {
            "newsClick" -> {
                (activity as HomeActivity).callBrowserActivity(newsDataModel.link, "News Page")
            }
        }
    }


}
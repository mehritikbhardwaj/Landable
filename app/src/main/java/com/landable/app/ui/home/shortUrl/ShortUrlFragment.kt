package com.landable.app.ui.home.shortUrl

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
import com.landable.app.common.ShortURLClickListener
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentShortUrlBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.ShortUrlDataModelItem

class ShortUrlFragment : Fragment(), ShortURLClickListener {

    private lateinit var binding: FragmentShortUrlBinding
    private var progressDialog: CustomProgressDialog? = null
    private var shortUrlList = ArrayList<ShortUrlDataModelItem>()

    companion object {
        fun newInstance() = ShortUrlFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Short Url")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_short_url, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Short URL Fragment", null)

        getShortUrlList()

        binding.buttonSaveUrl.setOnClickListener {
            postShortURL(
                PostShortURL(
                    0,
                    binding.edRedirectTo.text.toString(),
                    binding.edUrlName.text.toString()
                )
            )
        }

        return binding.root
    }

    class PostShortURL(
        id: Int,
        referenceurl: String,
        shorturl: String
    ) {
        private val id: Int = id
        private val referenceurl: String = referenceurl
        private val shorturl: String = shorturl
    }

    private fun getShortUrlList() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getShortlink()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    shortUrlList = ParseResponse.parseShortUrlList(it.toString())
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
        binding.rvShortUrls.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvShortUrls.adapter = ShortUrlListAdapter(shortUrlList, this)
    }

    override fun onShortUrlClick(action: String, shortUrlDataModelItem: ShortUrlDataModelItem) {
        when (action) {
            "deleteButton" -> {
                deleteShortLink(shortUrlDataModelItem.id)
            }
        }
    }

    private fun postShortURL(dataModel: PostShortURL) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val postPropertyStep1Response =
            RegisterRepository().post_Addshortlink(dataModel)
        postPropertyStep1Response.observe(viewLifecycleOwner) {
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
                    if (it.toString() != "null") {
                        binding.edUrlName.text.clear()
                        binding.edRedirectTo.text.clear()
                        shortUrlList.clear()
                        getShortUrlList()
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun deleteShortLink(id: Int) {
        val deleteResponse = RegisterRepository().getDeleteShortlink(id)
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
                        /* if (it == "success") {
                             getShortUrlList()
                         } else {
                             Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                         }*/
                        getShortUrlList()
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

}

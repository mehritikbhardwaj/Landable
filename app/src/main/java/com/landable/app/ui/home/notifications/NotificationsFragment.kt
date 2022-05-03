package com.landable.app.ui.home.notifications

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
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentNotificationsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.LeadsDataModel
import com.landable.app.ui.home.dataModels.NotificationsDataModelItem
import com.landable.app.ui.home.lead.LeadsListAdapter

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var notificationsList = ArrayList<NotificationsDataModelItem>()



    companion object {
        fun newInstance() = NotificationsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Notifications")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity)).setCurrentScreen((activity as HomeActivity), "Notifications Fragment", null);

        getNotificationsData()

        return binding.root
    }

    fun getNotificationsData() {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val agentsListResponse = RegisterRepository().getNotification()
        agentsListResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()

            if (it.toString() != "null") {
                try {
                    notificationsList = ParseResponse.parseNotificationsResponse(it.toString())
                    updateNotificationUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }
    private fun updateNotificationUI(){
        binding.rvNotifications.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNotifications.adapter = NotificationAdapter(notificationsList)
    }

}
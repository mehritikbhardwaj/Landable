package com.landable.app.ui.home.changePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.IListener
import com.landable.app.common.LandableConstants
import com.landable.app.common.Utility
import com.landable.app.databinding.FragmentChangePasswordBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.login.OTPLoginFragment
import org.json.JSONObject

class ChangePasswordFragment : Fragment(), IListener {

    private lateinit var binding: FragmentChangePasswordBinding

    private var progressDialog: CustomProgressDialog? = null

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).hideBottomNavigation()

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        binding.changePasswordViewModel = viewModel
        viewModel.listener = this
    }

    override fun onStarted(action: String) {
        if (action == "submitButtonClick") {
            // Show progress bar
            progressDialog = CustomProgressDialog(requireContext())
            progressDialog!!.show()
        }
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        if (action == "submitButtonClick") {
            response.observe(this) {
                // hide progress bar
                progressDialog!!.cancelProgress()

                if (it == LandableConstants.noInternetErrorMessage) {
                    //print NoInternet Error Message
                    CustomAlertDialog(
                        requireContext(),
                        LandableConstants.noInternetErrorTitle,
                        it
                    ).show()
                } else {
                    val jsonObj = JSONObject(it)
                    val status = jsonObj.getString("status")

                    if (status == "success") {
                        val bundle = Bundle()
                        bundle.putString(
                            "password",
                            binding.changePasswordViewModel!!.confirmPassword
                        )

                        CustomAlertDialog(
                            requireContext(),
                            "Alert",
                            "Password has been changed."
                        ).show()

                        val loginFragment = OTPLoginFragment.newInstance()
                        loginFragment.arguments = bundle

                        activity?.let { it ->
                            FragmentHelper().replaceFragmentAddToBackstack(
                                it.supportFragmentManager,
                                (activity as HomeActivity).getHomePageContainerId(),
                                loginFragment,
                                OTPLoginFragment::class.java.name
                            )
                        }
                    } else
                        CustomAlertDialog(requireContext(), "Alert", "There is some error.").show()
                }

            }
        } else if (action == "backButtonClick")
            FragmentHelper().popBackStackImmediate(requireActivity())
    }

    override fun onFailure(message: String, action: String) {
        if (action == "submitButtonClick")
            CustomAlertDialog(requireContext(), "Alert", message).show()
    }
}
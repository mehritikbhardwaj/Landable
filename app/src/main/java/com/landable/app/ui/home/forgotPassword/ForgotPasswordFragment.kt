package com.landable.app.ui.home.forgotPassword

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
import com.landable.app.databinding.FragmentForgotPasswordBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import org.json.JSONObject

class ForgotPasswordFragment : Fragment(), IListener {

    private lateinit var binding: FragmentForgotPasswordBinding

    private var progressDialog: CustomProgressDialog? = null

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =

            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).hideBottomNavigation()

        Utility.hideKeyboardOutsideClick(requireActivity(), binding.outerLayout)
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val forgotPasswordViewModel =
            ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        binding.forgotPasswordViewModel = forgotPasswordViewModel
        forgotPasswordViewModel.listener = this
    }

    override fun onStarted(action: String) {
        if (action == "sendPasswordClick") {
            // Show progress bar
            progressDialog = CustomProgressDialog(requireContext())
            progressDialog!!.show()
        }
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        if (action == "sendPasswordClick") {
            response.observe(this) {
                // hide progress bar
                progressDialog!!.cancelProgress()

                if (it == LandableConstants.noInternetErrorMessage) {
                    //print NOInternet Error Message
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
                        bundle.putString("email", binding.forgotPasswordViewModel!!.email)

                        CustomAlertDialog(
                            requireContext(),
                            "Alert",
                            "Password has been sent."
                        ).show()
                        FragmentHelper().popBackStackImmediate(requireActivity())
                        /* val resetPassword = ResetPasswordFragment.newInstance()
                         resetPassword.arguments = bundle

                         activity?.let {
                             FragmentHelper().replaceFragmentAddToBackstack(
                                 it.supportFragmentManager,
                                 (activity as HomeActivity).getHomePageContainerId(),
                                 resetPassword,
                                 ResetPasswordFragment::class.java.name)
                         }*/
                    } else
                        CustomAlertDialog(requireContext(), "Alert", "Email not registered.").show()

                }
            }
        } else if (action == "backButtonClick")
            FragmentHelper().popBackStackImmediate(requireActivity())
    }


    override fun onFailure(message: String, action: String) {
        if (action == "sendPasswordClick")
            CustomAlertDialog(requireContext(), "Alert", message).show()
        //requireActivity().toast(message)
    }
}

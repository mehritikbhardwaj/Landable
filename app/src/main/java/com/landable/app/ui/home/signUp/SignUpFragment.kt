package com.landable.app.ui.home.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.common.IListener
import com.landable.app.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(), IListener {

    private lateinit var binding: FragmentSignUpBinding
    private var number: String? = null



    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        number = arguments?.getString("number")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        openIndividualSignupFragment()

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.signUpViewModel = viewModel
        viewModel.listener = this
    }

    private fun getSignupPageContainerId(): Int {
        return R.id.signup_fragment_container
    }


    override fun onStarted(action: String) {
        TODO("Not yet implemented")
    }

    override fun onSuccess(response: LiveData<String>, action: String) {
        if (action == "loginButtonClick" || (action == "backButtonClick")) {
            FragmentHelper().popBackStackImmediate(requireActivity())
        } else if (action == "agencyButtonClick") {
            openAgencySignupFragment()
        } else if (action == "individualButtonClick") {
            openIndividualSignupFragment()
        }
    }

    override fun onFailure(message: String, action: String) {
    }


    private fun openIndividualSignupFragment() {
        val bundle = Bundle()
        bundle.putString("number", number)
        val signupFragment = IndividualSignUpFragment.newInstance()
        signupFragment.arguments = bundle

        activity?.let {
            FragmentHelper().replaceFragment(
                it.supportFragmentManager,
                getSignupPageContainerId(),
                signupFragment,
                IndividualSignUpFragment::class.java.name
            )
        }
    }

    private fun openAgencySignupFragment() {
        val bundle = Bundle()
        bundle.putString("number", number)
        val agencysignupFragment = AgencySignUpFragment.newInstance()
        agencysignupFragment.arguments = bundle

        activity?.let {
            FragmentHelper().replaceFragment(
                it.supportFragmentManager,
                getSignupPageContainerId(),
                agencysignupFragment,
                AgencySignUpFragment::class.java.name
            )
        }
    }


}
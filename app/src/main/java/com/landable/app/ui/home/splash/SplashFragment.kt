package com.landable.app.ui.home.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.databinding.FragmentSplashBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.home.homeUI.HomeFragment

class SplashFragment : Fragment() {

    private lateinit var splashFragmentInstance: FragmentSplashBinding

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        splashFragmentInstance =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        // hide Top Navigation & bottom navigation
        (activity as HomeActivity).hideTopbar()
        (activity as HomeActivity).hideBottomNavigation()
      /*  val versionInfo = "Version ${BuildConfig.VERSION_NAME}"
        splashFragmentInstance.tvVersion.text = versionInfo*/

        Handler(Looper.getMainLooper()).postDelayed({
           /* if (AppInfo.isOtpVerified())
                callHomeActivity()
            else */loadHomeFragment()

        }, 2000)


        return splashFragmentInstance.root
    }

    private fun loadHomeFragment() {
        activity?.let {
            FragmentHelper().replaceFragment(
                it.supportFragmentManager,
                (activity as HomeActivity).getHomePageContainerId(),
                HomeFragment.newInstance(),
                HomeFragment::class.java.name
            )
        }
    }
}
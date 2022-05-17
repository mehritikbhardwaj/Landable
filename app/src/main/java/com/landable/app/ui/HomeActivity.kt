package com.landable.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.landable.app.R
import com.landable.app.common.AppInfo
import com.landable.app.common.FragmentHelper
import com.landable.app.common.LandableConstants
import com.landable.app.common.ScreenTagManager
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.ActivityHomeBinding
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomConfirmationDialog
import com.landable.app.ui.home.chats.ChatsFragment
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel
import com.landable.app.ui.home.dataModels.UserProfileDataModel
import com.landable.app.ui.home.dataModels.WhyLandableDataModel
import com.landable.app.ui.home.favorites.FavoritesFragment
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.login.OTPLoginFragment
import com.landable.app.ui.home.myActivity.MyActivityFragment
import com.landable.app.ui.home.profile.ProfileFragment
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.splash.SplashFragment
import com.landable.app.ui.home.supergroups.AddSuperGroupFragment

class HomeActivity : AppCompatActivity(),
    CustomConfirmationDialog.ICustomConfirmationDialogListener {

    private lateinit var binding: ActivityHomeBinding
    private var backButtonCounter: Int = 0
    private var userData = ArrayList<UserProfileDataModel>()
    private var whyLandableList = ArrayList<WhyLandableDataModel>()
    private var featurePropertyList = ArrayList<FeaturePropertiesDataModel>()
    private var recentPropertyList = ArrayList<FeaturePropertiesDataModel>()
    private var projectsList = ArrayList<ProjectsDataModel>()
    var propertyID: String = ""
    var contactType: String = ""
    var agentID: Int = 0
    private var appUpdateManager: AppUpdateManager? = null
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.activity_home)
        AppInfo.setContext(this@HomeActivity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //load Splash fragment

        appUpdateManager = AppUpdateManagerFactory.create(this)

        if(intent.hasExtra("isComingFromNotification")){
            loadActivityFragment()
        }
        if (intent.hasExtra("url")) {
            loadAddSupergroupFragment()
        } else {
            loadSplashFragment()
        }

        checkUpdate()


        // Creates instance of the manager.

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> loadHomeFragment()
                R.id.activity -> {
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        askForLogin()
                    } else loadActivityFragment()
                }
                R.id.shortlisted -> {
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        askForLogin()
                    } else loadShortlistedFragment()
                }
                R.id.profile -> {
                    updateBottomNavigationSelection(R.id.home)
                    if (AppInfo.getSCode() == "" || AppInfo.getUserId() == "0") {
                        askForLogin()
                    } else loadProfileFragment()
                }
            }
            true
        }

        if (AppInfo.getFCMToken().isBlank())
            initializeFCM()
        else LandableConstants.fcmToken = AppInfo.getFCMToken()

        // Toast.makeText(this,LandableConstants.fcmToken,Toast.LENGTH_LONG).show()
        binding.ivBack.setOnClickListener {
            if (intent.hasExtra("url")){
                intent.removeExtra("url");
                loadHomeFragment()
                }else FragmentHelper().popBackStackImmediate(this@HomeActivity)
        }

        binding.ivSideNavigation.setOnClickListener {
            if (binding.drawer.isDrawerOpen(Gravity.LEFT))
                binding.drawer.closeDrawer(Gravity.LEFT)
            else binding.drawer.openDrawer(Gravity.LEFT)
            //NavigationMenuDialog(this@HomeActivity, this@HomeActivity).show()
        }
        binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun inProgressUpdate() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager?.startUpdateFlowForResult(
                    updateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE
                )
            }
        }
    }

    private fun checkUpdate() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager?.startUpdateFlowForResult(
                    updateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE
                )
            }
        }
    }

    private fun initializeFCM() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("HomeActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            LandableConstants.fcmToken = task.result
            AppInfo.setFCMToken(LandableConstants.fcmToken)

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, LandableConstants.fcmToken)
            Log.d("HomeActivity", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }


    fun hideDrawer() {
        binding.drawer.closeDrawer(Gravity.LEFT)
    }

    fun openDrawer() {
        binding.drawer.openDrawer(Gravity.LEFT)

    }

    fun getHomePageContainerId(): Int {
        return R.id.fragment_container
    }


    fun enableBackButton(title: String) {
        binding.apply {
            ivBack.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            ivSideNavigation.visibility = View.GONE

            tvTitle.text = title
        }
    }

    fun hideTopbar() {
        binding.constraintLayout.visibility = View.GONE
    }

    fun showTopBar() {
        binding.constraintLayout.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }


    fun updateBottomNavigationSelection(navigationId: Int) {
        binding.bottomNavigationView.selectedItemId = navigationId
    }


    fun getWhyLandableList(): ArrayList<WhyLandableDataModel> {
        return whyLandableList
    }

    fun updateWhyLandableList(whyLandableList: ArrayList<WhyLandableDataModel>) {
        this.whyLandableList = whyLandableList
    }

    fun getfeaturePropertyList(): ArrayList<FeaturePropertiesDataModel> {
        return featurePropertyList
    }

    fun updatefeaturePropertyList(featurePropertyList: ArrayList<FeaturePropertiesDataModel>) {
        this.featurePropertyList = featurePropertyList
    }

    fun getProjectsList(): ArrayList<ProjectsDataModel> {
        return projectsList
    }

    fun updateProjectList(projectsList: ArrayList<ProjectsDataModel>) {
        this.projectsList = projectsList
    }

    fun getRecentPropertyList(): ArrayList<FeaturePropertiesDataModel> {
        return recentPropertyList
    }

    fun updateRecentPropertyList(recentPropertyList: ArrayList<FeaturePropertiesDataModel>) {
        this.recentPropertyList = recentPropertyList
    }


    override fun onBackPressed() {
        try {
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount > 0) {
                backButtonCounter = 0
                FragmentHelper().popBackStackImmediate(this@HomeActivity)
            } else {
                when (backButtonCounter) {
                    0 -> {
                        val homeFragment: HomeFragment? =
                            supportFragmentManager.findFragmentByTag(HomeFragment::class.java.name) as HomeFragment?
                        if (homeFragment != null && homeFragment.isVisible) {
                            updateBottomNavigationSelection(R.id.home)
                            Toast.makeText(
                                applicationContext,
                                "Press again to close Landable",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // load home fragment
                            updateBottomNavigationSelection(R.id.home)
                        }
                    }
                    1 -> {
                        try {
                            ScreenTagManager.fragmentStack.clear()
                            backButtonCounter = 0
                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_HOME)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.message
                        }
                    }
                }
                backButtonCounter++
            }
        } catch (e: Exception) {
            e.message
        }
    }

    private fun loadHomeFragment() {
        FragmentHelper().replaceFragment(
            supportFragmentManager,
            getHomePageContainerId(),
            HomeFragment.newInstance(),
            HomeFragment::class.java.name
        )
    }

    private fun loadActivityFragment() {
        updateBottomNavigationSelection(R.id.home)
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getHomePageContainerId(),
            MyActivityFragment.newInstance(),
            MyActivityFragment::class.java.name
        )
    }

    private fun loadShortlistedFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getHomePageContainerId(),
            FavoritesFragment.newInstance(),
            FavoritesFragment::class.java.name
        )
    }

    private fun loadAddSupergroupFragment() {
        FragmentHelper().replaceFragment(
            supportFragmentManager,
            getHomePageContainerId(),
            AddSuperGroupFragment.newInstance(),
            AddSuperGroupFragment::class.java.name
        )
    }

    private fun loadProfileFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getHomePageContainerId(),
            ProfileFragment.newInstance(),
            ProfileFragment::class.java.name
        )
    }

    private fun loadChatsFragment() {
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getHomePageContainerId(),
            ChatsFragment.newInstance(),
            ChatsFragment::class.java.name
        )
    }

    private fun loadSplashFragment() {
        FragmentHelper().addFragment(
            supportFragmentManager,
            getHomePageContainerId(),
            SplashFragment.newInstance(),
            SplashFragment::class.java.name
        )
    }

    fun askForLogin() {
        CustomConfirmationDialog(
            this,
            "Alert",
            "Please login first to access this feature.",
            "ConfirmationForLogin",
            true,
            this
        ).show()
    }

    private fun loadLoginFragment() {
        FragmentHelper().replaceFragment(
            this.supportFragmentManager,
            getHomePageContainerId(),
            OTPLoginFragment.newInstance(),
            OTPLoginFragment::class.java.name
        )
    }

    fun updateUserData(userData: ArrayList<UserProfileDataModel>) {
        this.userData = userData
    }

    fun getUserData(): ArrayList<UserProfileDataModel> {
        return userData
    }

    fun callBrowserActivity(url: String, title: String) {
        val intent = Intent(this, BrowserActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    override fun onPressedCustomDialogButton(pressedButtonName: String?, action: String?) {
        if (pressedButtonName == "ok") {
            userData.clear()
            loadLoginFragment()
        }
    }

     fun postUserTrackingModel(dataModel: PostUserTrackingModel) {
        val postaddLocationResponse = RegisterRepository().post_usertracking(dataModel)
        postaddLocationResponse.observe(this) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(this,
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    if (it.toString() != "null") {
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    class PostUserTrackingModel(
        page: String,
        action: String,
        actionnon: String,
        message: String,
        referenceid1: String,
        referenceid2: String,

        ) {
        private var page: String = page
        private var action: String = action
        private var message: String = message
        private var actionnon: String = actionnon
        private var referenceid1: String = referenceid1
        private var referenceid2: String = referenceid2

    }
}
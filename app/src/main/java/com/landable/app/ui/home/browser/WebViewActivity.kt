package com.landable.app.ui.home.browser

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.landable.app.R
import com.landable.app.common.FragmentHelper
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.chats.ChatsFragment
import com.landable.app.ui.home.supergroups.AddSuperGroupFragment


class WebViewActivity : AppCompatActivity() {

    private var url: String = ""
    private var localtitle: String? = null
    var title: String? = null
   // private var webView: WebView? = null

    var urlForChat: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_webview)
    //    webView = findViewById<View>(R.id.webView) as WebView

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        url = intent.getStringExtra("url").toString()
        title = intent.getStringExtra("title")
        localtitle = intent.getStringExtra("localtitle")
        supportActionBar!!.title = title

        loadWebViewFragment()

        toolbar.setNavigationOnClickListener {
            // handle back pressed
            onBackPressed()
        }
    }

    private fun loadWebViewFragment() {
        FragmentHelper().addFragment(
            supportFragmentManager,
            getWebViewActivityContainerId(),
            WebViewFragment.newInstance(),
            WebViewFragment::class.java.name
        )
    }

    private fun getWebViewActivityContainerId(): Int {
        return R.id.fragment_container
    }

    fun updateTitle(title: String) {
        supportActionBar!!.title = title
    }

    fun getWebViewUrl(): String {
        return url
    }

    fun invalidateOptionMenu() {
        invalidateOptionsMenu()
    }

    fun urlChat(url:String){
        urlForChat =  url
    }

    fun loadChatFragment() {
        if (!urlForChat.isNullOrEmpty()) {
            val type = urlForChat!!.substringAfter("&type=")
            val thString = urlForChat!!.substringBefore("&uid=")
            val th = thString.substringAfter("th=")
            val toUserIdString = urlForChat!!.substringBefore("&type=")
            val toUserId = toUserIdString.substringAfter("&touserid=")

            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putInt("id", th.toInt())
            bundle.putInt("toUserID", toUserId.toInt())

            val chatsFragment = ChatsWebFragment.newInstance()
            chatsFragment.arguments = bundle
            FragmentHelper().replaceFragmentAddToBackstack(
                supportFragmentManager,
                getWebViewActivityContainerId(),
                chatsFragment,
                ChatsWebFragment::class.java.name
            )

        } else {
            CustomAlertDialog(this@WebViewActivity, "Alert!", "Chat url is blank.").show()
        }
    }

    fun loadAddPostFragment() {
        val bundle = Bundle()
        val addPostFragment = AddSuperGroupWebFragment.newInstance()
        addPostFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getWebViewActivityContainerId(),
            addPostFragment,
            AddSuperGroupWebFragment::class.java.name
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> finish()
        }
        return true
    }


    override fun onBackPressed() {
        try {
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount > 0) {
                FragmentHelper().popBackStackImmediate(this@WebViewActivity)
            } else {
                val webViewFragment: WebViewFragment? =
                    supportFragmentManager.findFragmentByTag(WebViewFragment::class.java.name) as WebViewFragment?
                if (webViewFragment != null && webViewFragment.isVisible) {
                    // finish activity
                    finish()
                } else {
                    // load webView fragment
                    loadWebViewFragment()
                }
            }
        } catch (e: Exception) {
            e.message
        }
    }

}
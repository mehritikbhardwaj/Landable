package com.landable.app.ui.home.browser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.landable.app.R
import com.landable.app.common.FragmentHelper

class AddSupergroupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)
        //    webView = findViewById<View>(R.id.webView) as WebView


        loadAddPostFragment()

    }

    fun loadAddPostFragment() {
        val bundle = Bundle()
        val addPostFragment = AddSuperGroupWebFragment.newInstance()
        addPostFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            supportFragmentManager,
            getSuperGroupViewActivityContainerId(),
            addPostFragment,
            AddSuperGroupWebFragment::class.java.name
        )
    }

    private fun getSuperGroupViewActivityContainerId(): Int {
        return R.id.fragment_container
    }
}
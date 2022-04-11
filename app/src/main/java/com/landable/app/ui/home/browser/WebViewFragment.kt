package com.landable.app.ui.home.browser

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.landable.app.R
import com.landable.app.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebviewBinding
    private val mCustomView: View? = null
    private var webChromeClient: MyWebChromeClient? = null

    companion object {
        fun newInstance() = WebViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false)


        initWebView()

        webChromeClient = MyWebChromeClient(requireContext())
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webView.settings.setAppCacheEnabled(false)
        //binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        //binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.settings.saveFormData = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = false
        binding.webView.loadUrl((activity as WebViewActivity).getWebViewUrl())

       // binding.webView.canGoBack()
        return binding.root
    }

    /*fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView()
                return true
            }
            if (mCustomView == null && binding.webView.canGoBack()) {
                binding.webView.goBack()
                return true
            }
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }*/

    fun inCustomView(): Boolean {
        return mCustomView != null
    }

    fun hideCustomView() {
        webChromeClient!!.onHideCustomView()
    }


    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun initWebView() {
        var pd: ProgressDialog? = null
        if (pd == null) {
            pd = ProgressDialog((activity as WebViewActivity))
            pd.setMessage("Loading...")
            pd.show()
        }
        binding.webView.webChromeClient = MyWebChromeClient(requireContext())
        binding.webView.webViewClient = object : WebViewClient() {

            /*  override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap) {
                  super.onPageStarted(view, url,favicon)
                  // progressBar.setVisibility(View.VISIBLE);
                  if (pd == null) {
                      pd = ProgressDialog((activity as WebViewActivity))
                      pd!!.setMessage("Loading...")
                      pd!!.show()
                  }
             //     (activity as WebViewActivity).invalidateOptionMenu()
                  Log.e("onPageStarted", url!!)
                  if (url.contains("gotochat.aspx?")) {
                        //  Toast.makeText(requireContext(),url,Toast.LENGTH_LONG).show()
                      (activity as WebViewActivity).loadChatFragment()
                  }
                  else if (url.contains("addthreads.aspx")){
                      (activity as WebViewActivity).loadAddPostFragment()
                  }
              }*/

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                binding.webView.loadUrl(url)
                Log.e("url", url)

                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // progressBar.setVisibility(View.GONE);
                try {
                    if (pd.isShowing) {
                        pd.dismiss()
                    }
                    if (url.contains("gotochat.aspx?")) {
                        (activity as WebViewActivity).urlChat(url)
                        (activity as WebViewActivity).loadChatFragment()
                    } else if (url.contains("addthreads.aspx")) {
                        (activity as WebViewActivity).loadAddPostFragment()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                (activity as WebViewActivity).invalidateOptionMenu()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                (activity as WebViewActivity).invalidateOptionMenu()
            }
        }
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.isHorizontalScrollBarEnabled = false
    }

    private class MyWebChromeClient(var context: Context) :
        WebChromeClient()
}
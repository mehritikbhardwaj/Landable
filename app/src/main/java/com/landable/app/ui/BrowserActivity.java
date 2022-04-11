package com.landable.app.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.landable.app.R;
import com.landable.app.common.AppInfo;
import com.landable.app.ui.home.browser.ChatActivity;

public class BrowserActivity extends AppCompatActivity {

    String ActivityTitle;
    RelativeLayout coordinatorLayout;
    Context mContext;
    String scode = "", uid = "";
    private String url, localtitle;
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    private View mCustomView;
    private MyWebChromeClient myWebChromeClient;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        url = getIntent().getStringExtra("url");
        ActivityTitle = getIntent().getStringExtra("title");
        localtitle = getIntent().getStringExtra("localtitle");
        getSupportActionBar().setTitle(ActivityTitle);

        uid = AppInfo.INSTANCE.getUserId();
        scode = AppInfo.INSTANCE.getSCode();

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        coordinatorLayout = (RelativeLayout) findViewById(R.id.main_content);

        initWebView();

        myWebChromeClient = new MyWebChromeClient(mContext);
        webView.setWebChromeClient(new MyWebChromeClient(mContext));
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(false);
        //webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else finish();
                break;
            default:
                finish();
                break;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            }

            if ((mCustomView == null) && webView.canGoBack()) {
                webView.goBack();
                return true;
            }

        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    public boolean inCustomView() {
        return (mCustomView != null);
    }

    public void hideCustomView() {
        myWebChromeClient.onHideCustomView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog pd;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // progressBar.setVisibility(View.VISIBLE);
                if (pd == null) {
                    pd = new ProgressDialog(BrowserActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();
                }
                invalidateOptionsMenu();
                Log.e("onPageStarted", url);

                if (url.contains("gotochat.aspx?")) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else if (url.contains("addthreads.aspx")) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("url", "");
                    startActivity(intent);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                Log.e("url", url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // progressBar.setVisibility(View.GONE);
                try {
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                    if (url.contains("gotochat.aspx?") || url.contains("addthreads.aspx")) {
                        webView.goBack();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                invalidateOptionsMenu();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setHorizontalScrollBarEnabled(false);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
package com.landable.app.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.google.firebase.analytics.FirebaseAnalytics;
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
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        pd = new ProgressDialog(BrowserActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        url = getIntent().getStringExtra("url");
        ActivityTitle = getIntent().getStringExtra("title");
        localtitle = getIntent().getStringExtra("localtitle");
        getSupportActionBar().setTitle("Landable");

        FirebaseAnalytics.getInstance(this).setCurrentScreen(this,
                ActivityTitle, null);


        uid = AppInfo.INSTANCE.getUserId();
        scode = AppInfo.INSTANCE.getSCode();

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        coordinatorLayout = findViewById(R.id.main_content);

        initWebView();
        webView.loadUrl(url);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack()) {
                    if (url.contains("https://www.google.com/url?rct")) {
                        finish();
                    } else {
                        if (pd != null) {
                            pd.setMessage("Loading...");
                            pd.show();
                        }
                        webView.goBack();
                    }
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // progressBar.setVisibility(View.VISIBLE);
                if (pd != null) {
                    pd.setMessage("Loading...");
                    pd.show();
                }
                invalidateOptionsMenu();
                Log.e("onPageStarted", url);

                if (url.contains("gotochat.aspx?")) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else if (url.contains("addthreads.aspx")) {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    //  webView.goBack();
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.putExtra("url", "Supergroup");
                    startActivity(intent);
                    finish();
                } else if (url.contains(".pdf")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else if (url.contains("api.whatsapp.com")) {
                    webView.goBack();
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                Log.e("url", url);
                if (url.contains("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // progressBar.setVisibility(View.GONE);
                try {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    if (url.contains("gotochat.aspx?")) {
                        // Toast.makeText(mContext, "back", Toast.LENGTH_SHORT).show();
                        webView.goBack();
                        //  webView.loadUrl(url);

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
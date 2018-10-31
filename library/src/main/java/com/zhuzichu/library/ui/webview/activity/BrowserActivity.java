package com.zhuzichu.library.ui.webview.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.databinding.ActivityBrowserBinding;
import com.zhuzichu.library.ui.webview.X5WebView;
import com.zhuzichu.library.utils.NiceCacheUtils;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by wb.zhuzichu18 on 2018/10/30.
 */
public class BrowserActivity extends SwipeBackActivity {
    public static final String URL_DEBUGTBS = "http://debugtbs.qq.com/";
    private static final String TAG = "BrowserActivity";

    public interface Extra {
        String EXTAR_PATH = "extra_path";
    }

    private ActivityBrowserBinding mBind;
    private String mPath;
    private X5WebView mWebView;

    public static void startActivity(Context context, String path) {
        Intent intent = new Intent();
        intent.putExtra(Extra.EXTAR_PATH, path);
        intent.setClass(context, BrowserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = getLayoutInflater().inflate(R.layout.activity_browser, null);
        mBind = ActivityBrowserBinding.bind(inflate);
        setContentView(mBind.getRoot());
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        parseData();
        mWebView = mBind.webview;
        mBind.progress.setMax(100);
        mBind.setColor(ColorManager.getInstance().color);
        initTopBar();
        initWebListener();
        initWebSettings();
    }

    private void initWebListener() {
        mWebView.setWebViewClient(new WebViewClient() {
            boolean loadingFinished = true;
            boolean redirect = false;

            long last_page_start;
            long now;

            private void showSplash() {
                if (mWebView.getVisibility() == View.VISIBLE) {
                    mWebView.setVisibility(View.GONE);
                    mBind.splash.setVisibility(View.VISIBLE);
                }
            }

            private void removeSplash() {
                if (last_page_start < now) {
                    mWebView.setVisibility(View.VISIBLE);
                    mBind.splash.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                Log.i(TAG, "onPageStarted: ");
                loadingFinished = false;
                last_page_start = System.nanoTime();
                showSplash();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                if (!redirect) {
                    loadingFinished = true;
                }
                //call remove_splash in 500 miSec
                if (loadingFinished && !redirect) {
                    now = System.nanoTime();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    removeSplash();
                                }
                            },
                            500);
                } else {
                    redirect = false;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: ");
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(url);
                return false;
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
                Log.i(TAG, "onJsConfirm: ");
                return super.onJsConfirm(webView, s, s1, jsResult);
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                super.onShowCustomView(view, customViewCallback);
                Log.i(TAG, "onShowCustomView: ");
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                Log.i(TAG, "onHideCustomView: ");
            }

            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                /**
                 * 这里写入你自定义的window alert
                 */
                Log.i(TAG, "onJsAlert: ");
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                Log.i(TAG, "onProgressChanged: " + newProgress);
                if (newProgress == 100) {
                    mBind.progress.setVisibility(View.GONE);
                    mBind.progress.setProgress(0);
                } else {
                    if (mBind.progress.getVisibility() == View.GONE) {
                        mBind.progress.setVisibility(View.VISIBLE);
                    }
                    mBind.progress.setProgress(newProgress);
                }
                super.onProgressChanged(webView, newProgress);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Log.i(TAG, "onDownloadStart: " + s);
            }
        });
    }


    private void initWebSettings() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(NiceCacheUtils.getWebViewAppcacheDiskCacheDir(getApplicationContext()).toString());
        webSetting.setDatabasePath(NiceCacheUtils.getWebViewDatabasesDiskCacheDir(getApplicationContext()).toString());
        webSetting.setGeolocationDatabasePath(NiceCacheUtils.getWebViewGeolocationDiskCacheDir(getApplicationContext()).toString());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        mWebView.loadUrl(mPath);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    private void parseData() {
        mPath = getIntent().getStringExtra(Extra.EXTAR_PATH);
    }

    private void initTopBar() {
        mBind.topbar.setTitle("webview");
        mBind.topbar.setTitleGravity(Gravity.LEFT);
        mBind.topbar.addLeftBackImageButton().setOnClickListener(view -> finish());
        mBind.topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_browser_more)
                .setOnClickListener(view -> Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show());
        ImageButton ib = findViewById(R.id.topbar_right_browser_more);
        Drawable mutate = ib.getDrawable().mutate();
        mutate.setColorFilter(Nice.getColor(R.color.color_grey_333333), PorterDuff.Mode.SRC_IN);
        ib.setImageDrawable(mutate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

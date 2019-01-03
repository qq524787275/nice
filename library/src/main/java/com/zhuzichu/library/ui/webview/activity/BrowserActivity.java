package com.zhuzichu.library.ui.webview.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.R;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.databinding.ActivityBrowserBinding;
import com.zhuzichu.library.ui.webview.CustomSettings;
import com.zhuzichu.library.ui.webview.WebLayout;
import com.zhuzichu.library.utils.DrawableUtils;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by wb.zhuzichu18 on 2018/10/30.
 */
public class BrowserActivity extends SwipeBackActivity {
    private static final String TAG = "BrowserActivity";

    public interface Extra {
        String EXTAR_PATH = "extra_path";
        String EXTRA_CONTENT_TYPE = "extra_content_type";
    }

    protected AgentWeb mAgentWeb;
    private ActivityBrowserBinding mBind;
    private String mPath;
    private String mContentType;

    public static void startActivity(Context context, String path, String contentType) {
        Intent intent = new Intent();
        intent.putExtra(Extra.EXTAR_PATH, path);
        intent.putExtra(Extra.EXTRA_CONTENT_TYPE, contentType);
        intent.setClass(context, BrowserActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String path) {
        startActivity(context, path, null);
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
        mBind.setColor(ColorManager.getInstance().color);
        initTopBar();
        initWebView();
    }

    private void initWebView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBind.root, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ColorManager.getInstance().color.colorPrimary)
                .setAgentWebWebSettings(new CustomSettings())
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setWebLayout(new WebLayout())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .additionalHttpHeader("content-type", mContentType)
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(mPath);
    }


    private void parseData() {
        mPath = getIntent().getStringExtra(Extra.EXTAR_PATH);
        mContentType = getIntent().getStringExtra(Extra.EXTRA_CONTENT_TYPE);
    }

    private void initTopBar() {
        mBind.topbar.setTitle(" ");
        mBind.topbar.setTitleGravity(Gravity.LEFT);
        mBind.topbar.addLeftBackImageButton().setOnClickListener(view -> finish());
        mBind.topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_browser_more)
                .setOnClickListener(view -> Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show());
        ImageButton ib = findViewById(R.id.topbar_right_browser_more);
        ib.setImageDrawable(DrawableUtils.transformColor(ib.getDrawable(), R.color.color_grey_333333));
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","onProgress:"+newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mBind.topbar != null)
                mBind.topbar.setTitle(title);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "onActivityResult: " + "onResult:" + requestCode + " onResult:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}

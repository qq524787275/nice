package com.zhuzichu.library.ui.webview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.just.agentweb.IWebLayout;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;

public class WebLayout implements IWebLayout {
    private final FrameLayout mFrameLayout;
    private WebView mWebView;

    public WebLayout() {
        mFrameLayout = (FrameLayout) LayoutInflater.from(Nice.context).inflate(R.layout.layout_webview, null);
        mWebView = mFrameLayout.findViewById(R.id.webview);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mFrameLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }
}

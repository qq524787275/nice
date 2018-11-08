package com.zhuzichu.library.ui.webview;

import android.webkit.WebView;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.utils.NiceCacheUtils;

public class CustomSettings extends AbsAgentWebSettings {
    public CustomSettings() {
        super();
    }

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {

    }


    @Override
    public IAgentWebSettings toSetting(WebView webView) {
        super.toSetting(webView);

        //设置数据库路径  api19 已经废弃,这里只针对 webkit 起作用
        getWebSettings().setGeolocationDatabasePath(NiceCacheUtils.getWebViewGeolocationDiskCacheDir(Nice.context).toString());
        getWebSettings().setDatabasePath(NiceCacheUtils.getWebViewDatabasesDiskCacheDir(Nice.context).toString());
        getWebSettings().setAppCachePath(NiceCacheUtils.getWebViewAppcacheDiskCacheDir(Nice.context).toString());

        return this;
    }
}

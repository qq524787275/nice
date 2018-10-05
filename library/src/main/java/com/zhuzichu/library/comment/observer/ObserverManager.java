package com.zhuzichu.library.comment.observer;

import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.comment.livedatabus.LiveDataBus;
import com.zhuzichu.library.comment.observer.action.ActionOnlineStatus;
import com.zhuzichu.library.comment.observer.action.ActionOtherClients;
import com.zhuzichu.library.comment.observer.action.ActionRecentContact;

import java.util.List;

public class ObserverManager {
    private static final String TAG = "ObserverManager";
    /**
     * 联系人监听
     */
    private final static Observer<List<RecentContact>> observerRecentContact = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            Log.i(TAG, "observerRecentContact: ");
            LiveDataBus.get().with(ActionRecentContact.key, ActionRecentContact.class).postValue(new ActionRecentContact(recentContacts));
        }
    };

    /**
     * 用户在线监听
     */
    private final static Observer<StatusCode> observeOnlineStatus = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            Log.i(TAG, "onEvent: observeOnlineStatus");
            LiveDataBus.get().with(ActionOnlineStatus.key, ActionOnlineStatus.class).postValue(new ActionOnlineStatus(statusCode));
        }
    };

    /**
     * 多端登录监听
     */
    private final static Observer<List<OnlineClient>> observeOtherClients = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            Log.i(TAG, "onEvent: observeOtherClients");
            LiveDataBus.get().with(ActionOtherClients.key, ActionOtherClients.class).postValue(new ActionOtherClients(onlineClients));
        }
    };

    public static void regist() {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, true);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observeOnlineStatus, true);
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(observeOtherClients, true);
    }

    public static void unRegist() {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, false);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observeOnlineStatus, false);
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(observeOtherClients, false);
    }
}

package com.zhuzichu.library.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.utils.SingleLiveEvent;

import java.util.List;

public class VMRecentContact extends ViewModel {
    private MutableLiveData<List<RecentContact>> mLiveRecentContact;

    public MutableLiveData<List<RecentContact>> getRecentContact() {
        if (mLiveRecentContact == null) {
            mLiveRecentContact = new SingleLiveEvent<>();
            loadRecentContact();
        }
        return mLiveRecentContact;
    }

    private void loadRecentContact() {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）
                        mLiveRecentContact.setValue(recents);
                    }
                });
    }
}

package com.zhuzichu.uikit.message.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.ItemMessageBinding;
import com.zhuzichu.uikit.message.provider.MsgProviderFile;
import com.zhuzichu.uikit.message.provider.MsgProviderImage;
import com.zhuzichu.uikit.message.provider.MsgProviderLocation;
import com.zhuzichu.uikit.message.provider.MsgProviderNotification;
import com.zhuzichu.uikit.message.provider.MsgProviderText;
import com.zhuzichu.uikit.message.provider.MsgProviderTip;
import com.zhuzichu.uikit.message.provider.MsgProviderVideo;

import java.util.ArrayList;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class MessageMultipItemAdapter extends MultipleItemRvAdapter<IMMessage, MessageMultipItemAdapter.DataBindingViewHolder> {
    public static final int MSG_AUDIO = 0;
    public static final int MSG_AVCHAT = 1;
    public static final int MSG_TIP = 2;
    public static final int MSG_FILE = 3;
    public static final int MSG_TEXT = 4;
    public static final int MSG_IMAGE = 5;
    public static final int MSG_ROBOT = 6;
    public static final int MSG_LOCATION = 7;
    public static final int MSG_VIDEO = 8;
    public static final int MSG_NOTIFICATION = 9;
    public static final int MSG_UNDEF = 10;
    public static final int MSG_CUSTOM = 11;
    private BaseFragment mFragment;

    public MessageMultipItemAdapter(BaseFragment fragment) {
        super(new ArrayList<>());
        mFragment = fragment;
        //构造函数若有传其他参数可以在调用finishInitialize()之前进行赋值，赋值给全局变量
        //这样getViewType()和registerItemProvider()方法中可以获取到传过来的值
        //getViewType()中可能因为某些业务逻辑，需要将某个值传递过来进行判断，返回对应的viewType
        //registerItemProvider()中可以将值传递给ItemProvider
        finishInitialize();
    }


    @Override
    protected int getViewType(IMMessage item) {
        MsgTypeEnum msgType = item.getMsgType();
        int type;
        switch (msgType) {
//            case audio:
//                type = MSG_AUDIO;
//                break;
//            case avchat:
//                type = MSG_AVCHAT;
//                break;
            case tip:
                type = MSG_TIP;
                break;
            case file:
                type = MSG_FILE;
                break;
            case text:
                type = MSG_TEXT;
                break;
            case image:
                type = MSG_IMAGE;
                break;
//            case robot:
//                type = MSG_ROBOT;
//                break;
            case video:
                type = MSG_VIDEO;
                break;
//            case custom:
//                type = MSG_CUSTOM;
//                break;
            case location:
                type = MSG_LOCATION;
                break;
            case notification:
                type = MSG_NOTIFICATION;
                break;
//            case undef:
//                type = MSG_UNDEF;
//                break;
            default:
                type = MSG_TEXT;
                break;
        }
        Log.i(TAG, "getViewType:------------------: " + type);
        return type;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new MsgProviderText(mFragment));
        mProviderDelegate.registerProvider(new MsgProviderNotification());
        mProviderDelegate.registerProvider(new MsgProviderTip());
        mProviderDelegate.registerProvider(new MsgProviderImage(mFragment));
        mProviderDelegate.registerProvider(new MsgProviderFile(mFragment));
        mProviderDelegate.registerProvider(new MsgProviderLocation(mFragment));
        mProviderDelegate.registerProvider(new MsgProviderVideo(mFragment));
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    public static class DataBindingViewHolder extends BaseViewHolder {
        public DataBindingViewHolder(View view) {
            super(view);
        }

        public ItemMessageBinding getBinding() {
            return (ItemMessageBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}

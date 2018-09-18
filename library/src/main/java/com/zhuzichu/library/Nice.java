package com.zhuzichu.library;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.model.VMRecentContact;

import java.util.List;

public class Nice {
    public static Context context;
    private volatile static Nice mNice;
    //联系人监听
    private Observer observerRecentContact;

    /**
     * 构造方法私有化
     */
    private Nice(Context ctx) {
        context = ctx;
    }

    /**
     * DCL方式获取单例
     *
     * @return
     */
    public static Nice init(Context context) {
        if (mNice == null) {
            synchronized (Nice.class) {
                mNice = new Nice(context);
            }
        }
        return mNice;
    }


    public void registListener(FragmentActivity activity) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(initObserverRecentContact(activity), true);
    }

    public void unRegistListener(){
        if(observerRecentContact!=null)
            NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, false);

    }

    public Observer initObserverRecentContact(final FragmentActivity activity) {
      return  observerRecentContact = new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(List<RecentContact> messages) {
                ViewModelProviders.of(activity).get(VMRecentContact.class).getRecentContact().setValue(messages);
            }
        };
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView,String url){
        if (TextUtils.isEmpty(url)) {
            //如果网址为空, 默认加载ic_launcher
            imageView.setImageResource(R.mipmap.avatar_default);
        } else {
            //使用Glide加载图片
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }
}

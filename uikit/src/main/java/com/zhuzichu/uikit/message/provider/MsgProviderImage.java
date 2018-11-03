package com.zhuzichu.uikit.message.provider;

import android.os.Build;
import android.support.transition.Fade;
import android.view.View;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;
import com.zhuzichu.uikit.preview.PreviewImageAndVideoFragment;
import com.zhuzichu.uikit.widget.DetailTransition;

public class MsgProviderImage extends MsgProviderThumbBase {

    public MsgProviderImage(BaseFragment fragment) {
        super(fragment);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }

    @Override
    int getContentResId() {
        return R.layout.item_message_image;
    }

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_IMAGE;
    }

    @Override
    protected void onItemClick(IMMessage msg, View view) {
        PreviewImageAndVideoFragment f = PreviewImageAndVideoFragment.newInstance(msg);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            fragment.setExitTransition(new Fade());
            f.setEnterTransition(new Fade());
            f.setSharedElementReturnTransition(new DetailTransition());
            f.setSharedElementEnterTransition(new DetailTransition());
            // 25.1.0以下的support包,Material过渡动画只有在进栈时有,返回时没有;
            // 25.1.0+的support包，SharedElement正常
            fragment.extraTransaction()
                    .addSharedElement(view.findViewById(R.id.msg_thumbnail), "zzc")
                    .start(f);
        } else {
            fragment.start(f);
        }
    }
}

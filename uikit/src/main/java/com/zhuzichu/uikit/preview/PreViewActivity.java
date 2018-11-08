package com.zhuzichu.uikit.preview;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseActivity;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.utils.FileUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.preview.fragment.ImageAndVideFragment;

import javax.annotation.Nullable;

public class PreViewActivity extends BaseActivity {
    public interface Extra {
        String EXTRA_MESSGAE = "extra_messgae";
    }

    IMMessage message;

    public static void startActivity(AppCompatActivity activity, IMMessage msg, ImageView img) {
        Intent intent = new Intent();
        intent.putExtra(Extra.EXTRA_MESSGAE, msg);
        intent.setClass(activity, PreViewActivity.class);
        FileAttachment attachment = (FileAttachment) msg.getAttachment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !FileUtils.isGif(attachment.getExtension())) {
            //Todo gif 使用动画跳转 第一次进入 只能加载第一帧
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity, img, Nice.getString(R.string.transition_preview)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public BaseFragment setRootFragment() {
        return ImageAndVideFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseData();
        QMUIStatusBarHelper.translucent(this);
        QMUIDisplayHelper.setFullScreen(this);
    }

    private void parseData() {
        message = (IMMessage) getIntent().getSerializableExtra(Extra.EXTRA_MESSGAE);
    }

    public IMMessage getMessage() {
        return message;
    }

}

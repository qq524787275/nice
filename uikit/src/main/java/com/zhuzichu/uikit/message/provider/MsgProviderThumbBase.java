package com.zhuzichu.uikit.message.provider;

import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.utils.BitmapDecoder;
import com.zhuzichu.uikit.utils.ImageUtil;
import com.zhuzichu.uikit.widget.MsgThumbImageView;

import java.io.File;

public abstract class MsgProviderThumbBase extends MsgProviderBase {
    MsgThumbImageView thumbnail;


    @Override
    void inflateContentView() {
        thumbnail = view.findViewById(R.id.msg_thumbnail);
    }

    @Override
    void refreshView() {
        ViewCompat.setTransitionName(thumbnail, message.getUuid());
        FileAttachment msgAttachment = (FileAttachment) message.getAttachment();
        String path = msgAttachment.getPath();
        String thumbPath = msgAttachment.getThumbPath();
        if (!TextUtils.isEmpty(thumbPath)) {
            loadThumbnailImage(thumbPath, msgAttachment.getExtension());
        } else if (!TextUtils.isEmpty(path)) {
            loadThumbnailImage(thumbFromSourceFile(path), msgAttachment.getExtension());
        } else {
            loadThumbnailImage(null, msgAttachment.getExtension());
            if (message.getAttachStatus() == AttachStatusEnum.transferred
                    || message.getAttachStatus() == AttachStatusEnum.def) {
                downloadAttachment();
            }
        }
    }

    private void downloadAttachment() {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
            NIMClient.getService(MsgService.class).downloadAttachment(message, true);
    }

    private void loadThumbnailImage(String path, String ext) {
        setImageSize(path);
        if (path != null) {
            thumbnail.loadAsPath(path, getImageMaxEdge(), getImageMaxEdge(), maskBg(), ext);
        } else {
            thumbnail.loadAsResource(R.drawable.image_default, maskBg());
        }
    }


    private void setImageSize(String thumbPath) {
        int[] bounds = null;
        if (thumbPath != null) {
            bounds = BitmapDecoder.decodeBound(new File(thumbPath));
        }
        if (bounds == null) {
            if (message.getMsgType() == MsgTypeEnum.image) {
                ImageAttachment attachment = (ImageAttachment) message.getAttachment();
                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
            } else if (message.getMsgType() == MsgTypeEnum.video) {
                VideoAttachment attachment = (VideoAttachment) message.getAttachment();
                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
            }
        }

        if (bounds != null) {
            ImageUtil.ImageSize imageSize = ImageUtil.getThumbnailDisplaySize(bounds[0], bounds[1], getImageMaxEdge(), getImageMinEdge());
            setLayoutParams(imageSize.width, imageSize.height, thumbnail);
        }
    }

    private int maskBg() {
        return R.drawable.message_item_round_bg;
    }

    public static int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * DensityUtils.getScreenW(Nice.getContext()));
    }

    public static int getImageMinEdge() {
        return (int) (76.0 / 320.0 * DensityUtils.getScreenW(Nice.getContext()));
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }

    protected abstract String thumbFromSourceFile(String path);
}

package com.zhuzichu.uikit.preview.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Priority;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.glide.GlideApp;
import com.zhuzichu.library.utils.FileUtils;
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentImageVideoBinding;
import com.zhuzichu.uikit.preview.PreViewActivity;
import com.zhuzichu.uikit.preview.adapter.ImageAndVideoAdapter;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

;

public class ImageAndVideFragment extends NiceFragment<FragmentImageVideoBinding> implements ViewPager.OnPageChangeListener {
    private FragmentImageVideoBinding mBind;
    private IMMessage message;
    private ImageAndVideoAdapter mAdapter;
    protected int mPreviousPos = -1;
    private ImageViewTouch img;

    public static ImageAndVideFragment newInstance() {

        Bundle args = new Bundle();

        ImageAndVideFragment fragment = new ImageAndVideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentImageVideoBinding binding) {
        mBind = binding;
        img = binding.img;
        _status.hide();
        initView();
        loadImageAndVideoMessage();
    }

    private void initView() {
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        img.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        if (FileUtils.isGif(attachment.getExtension())) {
            GlideApp.with(img)
                    .asGif()
                    .load(attachment.getThumbPath())
                    .override(Nice.w, Nice.h)
                    .priority(Priority.HIGH)
                    .into(img);
        } else {
            GlideApp.with(img)
                    .load(attachment.getThumbPath())
                    .override(Nice.w, Nice.h)
                    .priority(Priority.HIGH)
                    .fitCenter()
                    .into(img);
        }
        mBind.vp.addOnPageChangeListener(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_image_video;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        message = ((PreViewActivity) activity).getMessage();
    }

    public void loadImageAndVideoMessage() {
        // 查找图片和视频类型
        List<MsgTypeEnum> types = new ArrayList<>();
        types.add(MsgTypeEnum.image);
        types.add(MsgTypeEnum.video);
        // 查询锚点
        IMMessage anchor = MessageBuilder.createEmptyMessage(message.getSessionId(), message.getSessionType(), 0);
        NIMClient.getService(MsgService.class).queryMessageListByTypes(types, anchor,
                0, QueryDirectionEnum.QUERY_OLD, Integer.MAX_VALUE, true).setCallback(new NiceRequestCallback<List<IMMessage>>(getActivity()) {
            @Override
            public void success(List<IMMessage> data) {
                mAdapter = new ImageAndVideoAdapter(getChildFragmentManager(), data);
                mBind.vp.setAdapter(mAdapter);
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getUuid().equals(message.getUuid())) {
                        mBind.vp.setCurrentItem(i, false);
                        break;
                    }
                }
                mBind.vp.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPreviousPos != -1 && mPreviousPos != position) {
            //todo 区分image与video
//            ((PreViewItemFragment) (mAdapter.getItem(position))).resetView();
        }
        mPreviousPos = position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

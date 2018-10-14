package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentMessageBinding;
import com.zhuzichu.uikit.utils.UserInfoUtils;

public class MessageP2pFragment extends MessageFragment {

    public static MessageP2pFragment newInstance(String sessionId, SessionTypeEnum sessionType) {
        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_SESSION_ID, sessionId);
        args.putSerializable(Extras.EXTRA_SESSION_TYPE, sessionType);
        MessageP2pFragment fragment = new MessageP2pFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentMessageBinding binding) {
        super.init(binding);
        initTopBar();
    }

    private void initTopBar() {
        mBind.topbar.setTitleGravity(Gravity.LEFT);
        Optional<String> userName = UserInfoUtils.getUserName(mSessionId);
        if (userName.isPresent())
            mBind.topbar.setTitle(userName.get());
        else
            mBind.topbar.setTitle("未设置");
        mBind.topbar.addLeftBackImageButton().setOnClickListener((view) -> pop());

        mBind.topbar.addRightImageButton(R.mipmap.icon_topbar_user, R.id.topbar_right_message_user)
                .setOnClickListener((view) -> {
                    Toast.makeText(getActivity(), "点击了", Toast.LENGTH_SHORT).show();
                });
    }
}

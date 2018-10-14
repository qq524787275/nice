package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentMessageBinding;
import com.zhuzichu.uikit.utils.TeamUtils;

public class MessageTeamFragment extends MessageFragment {
    private Team mTeam;

    public static MessageTeamFragment newInstance(String sessionId, SessionTypeEnum sessionType) {
        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_SESSION_ID, sessionId);
        args.putSerializable(Extras.EXTRA_SESSION_TYPE, sessionType);
        MessageTeamFragment fragment = new MessageTeamFragment();
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
        Optional<Team> team = TeamUtils.getTeam(mSessionId);
        if (team.isPresent()) {
            mTeam = team.get();
            mBind.topbar.setTitle(mTeam.getName() + "(" + mTeam.getMemberCount() + ")");
        } else {
            pop();
        }
        mBind.topbar.addLeftBackImageButton().setOnClickListener((view) -> pop());

        mBind.topbar.addRightImageButton(R.mipmap.icon_topbar_double_user, R.id.topbar_right_message_team)
                .setOnClickListener((view) -> {
                    Toast.makeText(getActivity(), "点击了", Toast.LENGTH_SHORT).show();
                });
    }
}

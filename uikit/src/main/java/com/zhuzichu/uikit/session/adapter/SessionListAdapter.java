package com.zhuzichu.uikit.session.adapter;

import android.databinding.ViewDataBinding;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.base.BaseNiceAdapter;
import com.zhuzichu.library.base.BaseNiceViewHolder;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.comment.glide.GlideAdapter;
import com.zhuzichu.library.utils.TimeUtil;
import com.zhuzichu.uikit.BR;
import com.zhuzichu.uikit.R;

import java.util.ArrayList;

public class SessionListAdapter extends BaseNiceAdapter<RecentContact, BaseNiceViewHolder> {

    public SessionListAdapter() {
        super(R.layout.item_recent_session, new ArrayList<>());
    }


    @Override
    protected void convert(BaseNiceViewHolder helper, RecentContact item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.color, ColorManager.getInstance().color);

        helper.setText(R.id.item_content, item.getContent())
                .setText(R.id.item_time, TimeUtil.getTimeShowString(item.getTime(), false));
        if (item.getSessionType() == SessionTypeEnum.P2P) {
            Optional<NimUserInfo> userInfoOptional = Optional.fromNullable(NIMClient.getService(UserService.class).getUserInfo(item.getContactId()));
            if (userInfoOptional.isPresent()) {
                NimUserInfo user = userInfoOptional.get();
                helper.setText(R.id.item_name, user.getName());
                GlideAdapter.loadUserAvatar(helper.getView(R.id.item_avatar), user.getAvatar());
            }
        } else if (item.getSessionType() == SessionTypeEnum.Team) {
            Optional<Team> teamOptional = Optional.fromNullable(NIMClient.getService(TeamService.class).queryTeamBlock(item.getContactId()));
            if (teamOptional.isPresent()) {
                Team team = teamOptional.get();
                helper.setText(R.id.item_name, team.getName());
                GlideAdapter.loadGroupAvatar(helper.getView(R.id.item_avatar), team.getIcon());
            }
        }

    }
}

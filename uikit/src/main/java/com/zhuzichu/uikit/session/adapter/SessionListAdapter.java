package com.zhuzichu.uikit.session.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.comment.glide.GlideAdapter;
import com.zhuzichu.library.utils.TimeUtil;
import com.zhuzichu.uikit.BR;
import com.zhuzichu.uikit.R;

import java.util.ArrayList;

public class SessionListAdapter extends BaseQuickAdapter<RecentContact, SessionListAdapter.ViewHolder> {

    public SessionListAdapter() {
        super(R.layout.item_recent_session, new ArrayList<RecentContact>());
    }


    @Override
    protected void convert(ViewHolder helper, RecentContact item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.color, ColorManager.getInstance().color);
        if (item.getSessionType() == SessionTypeEnum.P2P) {
            NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(item.getContactId());
            helper.setText(R.id.item_name, user.getName());
            GlideAdapter.loadUserAvatar((ImageView) helper.getView(R.id.item_avatar), user.getAvatar());
        } else if (item.getSessionType() == SessionTypeEnum.Team) {
            Team team = NIMClient.getService(TeamService.class).queryTeamBlock(item.getContactId());
            helper.setText(R.id.item_name, team.getName());
            GlideAdapter.loadGroupAvatar((ImageView) helper.getView(R.id.item_avatar), team.getIcon());
        }
        helper.setText(R.id.item_content, item.getContent())
                .setText(R.id.item_time, TimeUtil.getTimeShowString(item.getTime(), false));
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

    public class ViewHolder extends BaseViewHolder {
        public ViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}

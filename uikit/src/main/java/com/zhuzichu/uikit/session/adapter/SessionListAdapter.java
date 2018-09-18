package com.zhuzichu.uikit.session.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.utils.TimeUtil;
import com.zhuzichu.uikit.R;

import java.util.ArrayList;

public class SessionListAdapter extends BaseQuickAdapter<RecentContact,BaseViewHolder>{
    private RequestOptions mOptions=RequestOptions.bitmapTransform(new CircleCrop());
    public SessionListAdapter() {
        super(R.layout.item_recent_session, new ArrayList<RecentContact>());
    }

    @Override
    protected void convert(BaseViewHolder helper, RecentContact item) {
        if(item.getSessionType()== SessionTypeEnum.P2P){
            NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(item.getContactId());
            helper.setText(R.id.item_name,user.getName());
            Glide.with(mContext).load(!TextUtils.isEmpty(user.getAvatar())?user.getAvatar():R.mipmap.avatar_default).apply(mOptions).into((ImageView) helper.getView(R.id.item_avatar));
        }else if(item.getSessionType()==SessionTypeEnum.Team){
            Team team = NIMClient.getService(TeamService.class).queryTeamBlock(item.getContactId());
            helper.setText(R.id.item_name,team.getName());
            Glide.with(mContext).load(!TextUtils.isEmpty(team.getIcon())?team.getIcon():R.mipmap.avatar_default).apply(mOptions).into((ImageView) helper.getView(R.id.item_avatar));
        }
        helper.setText(R.id.item_content,item.getContent())
                .setText(R.id.item_time, TimeUtil.getTimeShowString(item.getTime(),false));
    }
}

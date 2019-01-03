package com.zhuzichu.uikit.session.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Optional;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.TimeUtils;
import com.zhuzichu.library.view.drop.DropFake;
import com.zhuzichu.library.view.drop.DropManager;
import com.zhuzichu.library.view.face.NiceFaceView;
import com.zhuzichu.uikit.BR;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.ItemRecentSessionBinding;
import com.zhuzichu.uikit.session.fragment.SessionListFragment;
import com.zhuzichu.uikit.utils.TeamUtils;
import com.zhuzichu.uikit.utils.UserInfoUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionListAdapter extends BaseDataBindingAdapter<RecentContact, ItemRecentSessionBinding> {
    private SessionListFragment.RecentContactCallBack mCallBack;
    //最近联系人排序
    private Ordering<RecentContact> mOrder = new Ordering<RecentContact>() {
        @Override
        public int compare(RecentContact left, RecentContact right) {
            Map<String, Object> mapRight = Optional.fromNullable(right.getExtension()).or(new HashMap<>());
            Map<String, Object> mapLeft = Optional.fromNullable(left.getExtension()).or(new HashMap<>());
            Long longRight = (Long) Optional.fromNullable(mapRight.get(SessionListFragment.Extras.SESSION_ON_TOP)).or(Long.MIN_VALUE);
            Long longLeft = (Long) Optional.fromNullable(mapLeft.get(SessionListFragment.Extras.SESSION_ON_TOP)).or(Long.MIN_VALUE);
            //先比较置顶 后 比较时间
            if (longLeft != Long.MIN_VALUE && longRight == Long.MIN_VALUE) {
                return -1;
            } else if (longLeft == Long.MIN_VALUE && longRight != Long.MIN_VALUE) {
                return 1;
            } else {
                return Longs.compare(Math.max(right.getTime(), longRight), Math.max(left.getTime(), longLeft));
            }
        }
    };

    public SessionListAdapter(List<RecentContact> data, SessionListFragment.RecentContactCallBack callBack) {
        super(R.layout.item_recent_session, data);
        mCallBack = callBack;
    }


    @Override
    protected void convert(BaseViewHolder helper, ItemRecentSessionBinding binding, RecentContact item) {
        binding.setVariable(BR.color, ColorManager.getInstance().color);
        Object o = Optional.fromNullable(item.getExtension()).or(new HashMap<>()).get(SessionListFragment.Extras.SESSION_ON_TOP);
        if (o == null) {
            binding.setVariable(BR.onTop, false);
        } else {
            binding.setVariable(BR.onTop, true);
        }
        NiceFaceView faceView = helper.getView(R.id.item_content);

        faceView.setText(item.getContent());
        helper.setText(R.id.item_time, TimeUtils.getTimeShowString(item.getTime(), false));
//        ImageAdapter.loadAvatar(binding.itemAvatar, binding.itemName, item.getContactId(), item.getSessionType());
        if (item.getSessionType() == SessionTypeEnum.P2P) {
            binding.itemAvatar.loadAvatar(item.getContactId());
            binding.itemName.setText(UserInfoUtils.getUserName(item.getContactId()).get());
        } else if (item.getSessionType() == SessionTypeEnum.Team) {
            binding.itemAvatar.loadTeamGroupAvatar(TeamUtils.getTeam(item.getContactId()).get());
            binding.itemName.setText(TeamUtils.getTeamName(item.getContactId()).get());
        }

        helper.addOnClickListener(R.id.item_unread);
        int unreadNum = item.getUnreadCount();
        binding.itemUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
        binding.itemUnread.setText(unreadCountShowRule(unreadNum));
        binding.itemUnread.setTouchListener(new DropFake.ITouchListener() {
            @Override
            public void onDown() {
                DropManager.getInstance().setCurrentId(item);
                DropManager.getInstance().down(binding.itemUnread, binding.itemUnread.getText());
            }

            @Override
            public void onMove(float curX, float curY) {
                DropManager.getInstance().move(curX, curY);
            }

            @Override
            public void onUp() {
                DropManager.getInstance().up();
            }
        });
    }


    public void sortRefresh() {
        Collections.sort(mData, mOrder);
        notifyDataSetChanged();
        if (mCallBack != null) {
            // 方式一：累加每个最近联系人的未读（快）
            int unreadNum = 0;
            for (RecentContact r : getData()) {
                unreadNum += r.getUnreadCount();
            }
            mCallBack.onUnreadCountChange(unreadNum);
            // 方式二：直接从SDK读取（相对慢）
//            mCallBack.onUnreadCountChange(NIMClient.getService(MsgService.class).getTotalUnreadCount());
        }
    }

    protected String unreadCountShowRule(int num) {
        if (num <= 0) {
            return String.valueOf(0);
        } else {
            if (num > 99) {
                return "...";
            } else {
                return String.valueOf(num);
            }
        }
    }
}

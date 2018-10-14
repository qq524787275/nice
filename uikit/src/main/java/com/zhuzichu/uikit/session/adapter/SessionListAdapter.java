package com.zhuzichu.uikit.session.adapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Optional;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.TimeUtils;
import com.zhuzichu.uikit.BR;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.adapter.ImageAdapter;
import com.zhuzichu.uikit.databinding.ItemRecentSessionBinding;
import com.zhuzichu.uikit.session.fragment.SessionListFragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionListAdapter extends BaseDataBindingAdapter<RecentContact, ItemRecentSessionBinding> {
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

    public SessionListAdapter(List<RecentContact> data) {
        super(R.layout.item_recent_session, data);
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
        helper.setText(R.id.item_content, item.getContent())
                .setText(R.id.item_time, TimeUtils.getTimeShowString(item.getTime(), false));
        ImageAdapter.loadAvatar(binding.itemAvatar, binding.itemName, item.getContactId(), item.getSessionType());
    }


    public void sortRefresh() {
        Collections.sort(mData, mOrder);
        notifyDataSetChanged();
    }
}

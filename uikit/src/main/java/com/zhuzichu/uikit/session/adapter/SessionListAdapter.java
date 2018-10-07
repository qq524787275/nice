package com.zhuzichu.uikit.session.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.comment.glide.GlideAdapter;
import com.zhuzichu.library.utils.TimeUtil;
import com.zhuzichu.uikit.BR;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.ItemRecentSessionBinding;

import java.util.ArrayList;

public class SessionListAdapter extends BaseDataBindingAdapter<RecentContact, ItemRecentSessionBinding> {

    public SessionListAdapter() {
        super(R.layout.item_recent_session, new ArrayList<>());
    }


    @Override
    protected void convert(BaseViewHolder helper, ItemRecentSessionBinding binding, RecentContact item) {
        binding.setVariable(BR.color, ColorManager.getInstance().color);
        helper.setText(R.id.item_content, item.getContent())
                .setText(R.id.item_time, TimeUtil.getTimeShowString(item.getTime(), false));
        GlideAdapter.loadAvatar(binding.itemAvatar, binding.itemName, item.getContactId(), item.getSessionType());
    }
}

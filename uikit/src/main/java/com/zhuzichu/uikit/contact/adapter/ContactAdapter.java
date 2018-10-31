package com.zhuzichu.uikit.contact.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.databinding.ItemIndexLableBinding;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.adapter.ImageAdapter;
import com.zhuzichu.uikit.contact.bean.FriendBean;
import com.zhuzichu.uikit.databinding.ItemIndexContactBinding;
import com.zhuzichu.uikit.event.online.OnlineStateEventManager;

import me.yokeyword.indexablerv.IndexableAdapter;

public class ContactAdapter extends IndexableAdapter<FriendBean> {
    private LayoutInflater mInflater;

    public ContactAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_index_lable, parent, false);
        ItemIndexLableBinding binding = ItemIndexLableBinding.bind(view);
        return new ViewHolderLable(binding.getRoot(), binding);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_index_contact, parent, false);
        ItemIndexContactBinding binding = ItemIndexContactBinding.bind(view);
        return new ViewHolderContact(binding.getRoot(), binding);
    }


    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        ViewHolderLable holderLable = (ViewHolderLable) holder;
        ItemIndexLableBinding bind = holderLable.getBind();
        bind.setColor(ColorManager.getInstance().color);
        bind.itemTitle.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, FriendBean entity) {
        ViewHolderContact holderContact = (ViewHolderContact) holder;
        ItemIndexContactBinding bind = holderContact.getBind();
        bind.setColor(ColorManager.getInstance().color);
        NimUserInfo item = entity.getUserInfo();
        ImageAdapter.loadUserAvatar(bind.itemAvatar, item.getAvatar());
        bind.itemName.setText(item.getName());
        String onlineStateContent = OnlineStateEventManager.getSimpleDisplay(item.getAccount());
        if (TextUtils.isEmpty(onlineStateContent)) {
            bind.itemStatus.setVisibility(View.GONE);
        } else {
            bind.itemStatus.setVisibility(View.VISIBLE);
            bind.itemStatus.setText(onlineStateContent);
        }
    }

    private class ViewHolderLable extends RecyclerView.ViewHolder {
        private ItemIndexLableBinding mBind;

        public ItemIndexLableBinding getBind() {
            return mBind;
        }

        public ViewHolderLable(View itemView, ItemIndexLableBinding binding) {
            super(itemView);
            mBind = binding;
        }
    }


    private class ViewHolderContact extends RecyclerView.ViewHolder {
        private ItemIndexContactBinding mBind;

        public ViewHolderContact(View itemView, ItemIndexContactBinding binding) {
            super(itemView);
            mBind = binding;
        }

        public ItemIndexContactBinding getBind() {
            return mBind;
        }
    }
}

package com.zhuzichu.nice.login.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.ItemMultiportBinding;

import java.util.ArrayList;

/**
 * Created by wb.zhuzichu18 on 2018/10/26.
 */
public class MultiportAdapter extends BaseDataBindingAdapter<OnlineClient, ItemMultiportBinding> {
    public MultiportAdapter() {
        super(R.layout.item_multiport, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemMultiportBinding binding, OnlineClient item) {
        binding.setColor(ColorManager.getInstance().color);
        helper.setText(R.id.item_name, getText(item.getClientType()));
        helper.addOnClickListener(R.id.item_submit);
    }

    private String getText(int clientType) {
        String clientName = "";
        switch (clientType) {
            case ClientType.Windows:
                clientName = "Windows 在线";
                break;
            case ClientType.MAC:
                clientName = "MAC 在线";
                break;
            case ClientType.Web:
                clientName = "Web 在线";
                break;
            case ClientType.Android:
                clientName = "Android 在线";
                break;
            case ClientType.iOS:
                clientName = "iOS 在线";
                break;
            default:
                break;
        }
        return clientName;
    }
}

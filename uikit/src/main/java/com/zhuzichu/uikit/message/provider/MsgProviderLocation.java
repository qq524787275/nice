package com.zhuzichu.uikit.message.provider;

import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.location.LocationInfoFragment;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;
import com.zhuzichu.uikit.utils.ImageUtil;
import com.zhuzichu.uikit.widget.MsgThumbImageView;

public class MsgProviderLocation extends MsgProviderBase {
    private MsgThumbImageView mMap;
    private TextView mAddress;


    @Override
    int getContentResId() {
        return R.layout.item_message_location;
    }

    @Override
    void inflateContentView() {
        mMap = itemView.findViewById(R.id.location_img);
        mAddress = itemView.findViewById(R.id.location_address);
    }

    @Override
    void refreshView() {
        final LocationAttachment location = (LocationAttachment) message.getAttachment();
        mAddress.setText(location.getAddress());
        int[] bound = ImageUtil.getBoundWithLength(getLocationDefEdge(), R.mipmap.bk_map, true);
        int width = bound[0];
        int height = bound[1];

        setLayoutParams(width, height, mMap);
        setLayoutParams(width, (int) (0.38 * height), mAddress);

        mMap.loadAsResource(R.mipmap.bk_map, R.drawable.message_item_round_bg);
    }

    private int getLocationDefEdge() {
        return (int) (0.5 * DensityUtils.getScreenW(mContext));
    }

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_LOCATION;
    }

    @Override
    protected void onItemClick(IMMessage msg,MessageMultipItemAdapter.DataBindingViewHolder holder) {
        LocationAttachment location = (LocationAttachment) msg.getAttachment();
        RxBus.getIntance().post(new ActionMainStartFragmnet(LocationInfoFragment.newInstance(location.getLongitude(), location.getLatitude(), location.getAddress(), msg.getFromAccount())));
    }
}

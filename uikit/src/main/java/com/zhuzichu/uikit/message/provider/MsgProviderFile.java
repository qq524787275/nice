package com.zhuzichu.uikit.message.provider;

import android.text.format.Formatter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.file.fragment.FileDisplayFragment;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.file.FileIcons;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;


/**
 * Created by wb.zhuzichu18 on 2018/10/29.
 */
public class MsgProviderFile extends MsgProviderBase {
    private ImageView fileIcon;
    private TextView fileName;
    private TextView fileSize;
    private FileAttachment msgAttachment;

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_FILE;
    }

    @Override
    int getContentResId() {
        return R.layout.item_message_file;
    }

    @Override
    void inflateContentView() {
        fileIcon = view.findViewById(R.id.file_icon);
        fileName = view.findViewById(R.id.file_name);
        fileSize = view.findViewById(R.id.file_size);
    }

    @Override
    void onItemClick() {
        RxBus.getIntance().post(new ActionMainStartFragmnet(FileDisplayFragment.newInstance(message)));
    }

    @Override
    void refreshView() {
        msgAttachment = (FileAttachment) message.getAttachment();
        int iconResId = FileIcons.bigIcon(msgAttachment.getDisplayName());
        fileIcon.setImageResource(iconResId);
        fileName.setText(msgAttachment.getDisplayName());
        fileSize.setText(Formatter.formatFileSize(mContext, msgAttachment.getSize()));
        if (isMine()) {
            fileName.setTextColor(Nice.getColor(R.color.white));
            fileSize.setTextColor(Nice.getColor(R.color.white));
        } else {
            fileName.setTextColor(Nice.getColor(R.color.color_grey_333333));
            fileSize.setTextColor(Nice.getColor(R.color.color_grey_999999));
        }
    }

    public boolean isMine() {
        return message.getFromAccount().equals(Nice.getAccount());
    }
}

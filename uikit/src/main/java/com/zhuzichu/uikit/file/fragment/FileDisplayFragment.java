package com.zhuzichu.uikit.file.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.FileUtils;
import com.zhuzichu.library.utils.OpenAnyFileUtils;
import com.zhuzichu.library.view.DownLoadView;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentFileDisplayBinding;
import com.zhuzichu.uikit.file.FileIcons;
import com.zhuzichu.uikit.observer.action.ActionAttachmentProgress;
import com.zhuzichu.uikit.observer.action.ActionMessageStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wb.zhuzichu18 on 2018/10/29.
 */
public class FileDisplayFragment extends NiceSwipeFragment<FragmentFileDisplayBinding> {

    private DownLoadView downLoadView;

    public interface Extra {
        String EXTRA_MESSAGE = "extra_message";
    }

    private FragmentFileDisplayBinding mBind;
    private IMMessage msg;
    private FileAttachment mAttachment;

    public static FileDisplayFragment newInstance(IMMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_MESSAGE, message);
        FileDisplayFragment fragment = new FileDisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentFileDisplayBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
        mBind = binding;
        msg = (IMMessage) getArguments().getSerializable(Extra.EXTRA_MESSAGE);
        mAttachment = (FileAttachment) msg.getAttachment();
        initView();
        initTopBar();
        initObserver();
    }

    private static final String TAG = "FileDisplayFragment";

    private void initObserver() {
        Disposable dispProgress = RxBus.getIntance().toObservable(ActionAttachmentProgress.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.data)
                .filter(progress -> progress.getUuid().equals(msg.getUuid()))
                .subscribe(progress -> {
                    BigDecimal divide = BigDecimal.valueOf(progress.getTransferred()).divide(BigDecimal.valueOf(progress.getTotal()), 2, RoundingMode.UP);
                    Log.i(TAG, "initObserver: " + divide.floatValue());
                    downLoadView.setProgress(divide.floatValue());
                });

        Disposable dispMessageStatus = RxBus.getIntance().toObservable(ActionMessageStatus.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.data)
                .filter(messgae -> messgae.getUuid().equals(msg.getUuid()))
                .subscribe(messgae -> {
                    msg = messgae;
                    mAttachment = (FileAttachment) messgae.getAttachment();
                    AttachStatusEnum status = messgae.getAttachStatus();
                    switch (status) {
                        case def:
                            Log.i(TAG, "initObserver: def");
                            break;
                        case transferring:
                            Log.i(TAG, "initObserver: transferring");
                            break;
                        case transferred:
//                            FileUtils.copyFile(mAttachment.getPath(), mAttachment.getPath() + "." + FileIcons.getExtensionName(mAttachment.getDisplayName().toLowerCase()));
                            downLoadView.setState(DownLoadView.State.FINISH);
                            Log.i(TAG, "initObserver: transferred");
                            break;
                        case fail:
                            Log.i(TAG, "initObserver: fail");
                            break;
                    }
                });

        RxBus.getIntance().addSubscription(this.getClass().getSimpleName() + msg.getUuid(), dispMessageStatus, dispProgress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this.getClass().getSimpleName() + msg.getUuid());
    }

    private void initView() {
        downLoadView = mBind.load;
        mBind.fileIcon.setImageResource(FileIcons.bigIcon(mAttachment.getDisplayName()));
        mBind.fileName.setText(mAttachment.getDisplayName());
        downLoadView.setNormalText("下载(" + Formatter.formatFileSize(getActivity(), mAttachment.getSize()) + ")");
        downLoadView.setOnNormalClickListener(() -> {
            NIMClient.getService(MsgService.class).downloadAttachment(msg, false);
        });
        downLoadView.setFinishText("用其他软件打开");
        downLoadView.setOnFilishClickListener(() -> {
            OpenAnyFileUtils.openFile(getActivity(), mAttachment.getPath() + "." + FileIcons.getExtensionName(mAttachment.getDisplayName().toLowerCase()));
        });
        if (TextUtils.isEmpty(mAttachment.getPath())) {
            //说明没有下载该文件
            downLoadView.setState(DownLoadView.State.NORMAL);
        } else {
            //说明已经下载了改文件
            downLoadView.setState(DownLoadView.State.FINISH);
        }
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);

    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_file_display;
    }

    private void initTopBar() {
        mBind.topbar.setTitle("文件预览");
        mBind.topbar.addLeftBackImageButton().setOnClickListener(view -> pop());
    }
}
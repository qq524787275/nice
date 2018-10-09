package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.zhuzichu.library.action.ActionSoftKeyboard;
import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.comment.observer.action.ActionReceiveMessage;
import com.zhuzichu.library.view.button.StateButton;
import com.zhuzichu.library.widget.EmojiKeyboard;
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.library.widget.OnClickListener;
import com.zhuzichu.library.widget.TextWatcherWrapper;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentMessageBinding;
import com.zhuzichu.uikit.message.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageFragment extends NiceSwipeFragment<FragmentMessageBinding> {
    private static final String TAG = "MessageFragment";
    private EditText mEtMessage;
    private ImageView mIvMore;
    private StateButton mSbSend;
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLayoutMamnager;

    public interface Extras {
        String EXTRA_SESSION_ID = "extra_session_id";
        String EXTRA_SESSION_TYPE = "extra_session_type";
    }

    // p2p对方Account或者群id
    private String mSessionId;
    private SessionTypeEnum mSessionType;
    private FragmentMessageBinding mBind;
    private EmojiKeyboard mEmojiKeyboard;

    @Override
    public Object setLayout() {
        return R.layout.fragment_message;
    }

    public static MessageFragment newInstance(String sessionId, SessionTypeEnum sessionType) {

        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_SESSION_ID, sessionId);
        args.putSerializable(Extras.EXTRA_SESSION_TYPE, sessionType);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentMessageBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
        binding.layoutInput.setColor(ColorManager.getInstance().color);
        mBind = binding;
        parseExtras();
        initView();
        initListener();
        initObserver();
    }

    private void initObserver() {
        //键盘弹出监听
        Disposable dispSoftKeyboard = RxBus.getIntance()
                .toObservable(ActionSoftKeyboard.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(50, TimeUnit.MILLISECONDS)
                .subscribe(action -> smoothToBottom(true));
        //消息接受监听
        Disposable dispReceiveMessage = RxBus.getIntance().toObservable(ActionReceiveMessage.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(actionReceiveMessage -> filterMessage(actionReceiveMessage.getData()))
                .subscribe(list -> {
                    if (list.size() != 0)
                        return;
                    mAdapter.addData(list);
                    if (isLastMessageVisible()) {
                        smoothToBottom(true);
                    } else {
                        mBind.tvNewMessage.setVisibility(View.VISIBLE);
                    }
                });
        RxBus.getIntance().addSubscription(this.getClass().getSimpleName() + mSessionId,
                dispSoftKeyboard,
                dispReceiveMessage);
    }

    private List<IMMessage> filterMessage(List<IMMessage> data) {
        ArrayList<IMMessage> list = new ArrayList<>();
        for (IMMessage item : data) {
            if (item.getSessionId().equals(mSessionId))
                list.add(item);
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this.getClass().getSimpleName() + mSessionId);
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        //文本输入框监听事件
        mEtMessage.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mSbSend.setVisibility(View.VISIBLE);
                    mIvMore.setVisibility(View.GONE);
                } else {
                    mSbSend.setVisibility(View.GONE);
                    mIvMore.setVisibility(View.VISIBLE);
                }
            }
        });

        //发送
        mSbSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                IMMessage textMessage = createTextMessage(mEtMessage.getText().toString());
                sendMessage(textMessage, false);
                mAdapter.addData(textMessage);
                smoothToBottom(true);
                mEtMessage.setText("");
            }
        });

        mBind.tvNewMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                smoothToBottom(true);
                mBind.tvNewMessage.setVisibility(View.GONE);
            }
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.msg_fail) {
                //重新发消息
                sendMessage(mAdapter.getItem(position), true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void parseExtras() {
        mSessionId = getArguments().getString(Extras.EXTRA_SESSION_ID);
        mSessionType = (SessionTypeEnum) getArguments().getSerializable(Extras.EXTRA_SESSION_TYPE);
    }

    private void initView() {
        //findView
        {
            mEtMessage = mBind.layoutInput.etInputMessage;
            mIvMore = mBind.layoutInput.ivMore;
            mSbSend = mBind.layoutInput.sbSend;
        }
        mEmojiKeyboard = new EmojiKeyboard(getActivity(), mEtMessage, mBind.layoutPanelWarpper, mIvMore, mBind.layoutContent, mBind.listMessage);
        mLayoutMamnager = new LinearLayoutManager(getContext());
        mBind.listMessage.setLayoutManager(mLayoutMamnager);
        mBind.listMessage.requestDisallowInterceptTouchEvent(false);
        mAdapter = new MessageAdapter();
        mBind.listMessage.setAdapter(mAdapter);
        mBind.fastScroller.attachRecyclerView(mBind.listMessage);
    }


    @Override
    public boolean onBackPressedSupport() {
        if (!mEmojiKeyboard.interceptBackPress()) {
            return false;
        }
        return true;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        loadFromLocal();
    }

    public void loadFromLocal() {
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), QueryDirectionEnum.QUERY_OLD, 20, true)
                .setCallback(new NiceRequestCallback<List<IMMessage>>(getActivity()) {
                    @Override
                    public void success(List<IMMessage> param) {
                        mAdapter.addData(param);
                        scrollToBottom();
                    }
                });
    }

    private IMMessage anchor() {
        return MessageBuilder.createEmptyMessage(mSessionId, mSessionType, 0);
    }

    /**
     * 瞬间到底
     */
    public void scrollToBottom() {
        int position = mAdapter.getData().size() - 1;
        if (position >= 0)
            mLayoutMamnager.scrollToPositionWithOffset(position, 0);
    }

    /**
     * 滑动到底
     */
    public void smoothToBottom(boolean isSlow) {
        int position = mAdapter.getData().size() - 1;
        if (position < 0)
            return;
        if (isSlow) {
            mBind.listMessage.smoothScrollToPosition(position);
        } else {
            mBind.listMessage.scrollToPosition(position);
        }
    }

    /**
     * 判断最后一条消息是否可见
     *
     * @return
     */
    public boolean isLastMessageVisible() {
        int position = mAdapter.getData().size() - 1;
        if (position >= 0) {
            //说明在屏幕内
            if (mLayoutMamnager.findLastVisibleItemPosition() + 1 == position)
                return true;
        }
        return false;
    }

    /**
     * 发送消息
     *
     * @param msg    带发送的消息体，由{@link MessageBuilder}构造
     * @param resend 如果是发送失败后重发，标记为true，否则填false
     */
    public void sendMessage(IMMessage msg, boolean resend) {
        if (resend)
            msg.setStatus(MsgStatusEnum.sending);
        NIMClient.getService(MsgService.class).sendMessage(msg, resend).setCallback(new NiceRequestCallback<Void>(getActivity()) {
            @Override
            public void success(Void param) {
                //Todo 发送成功
            }

            @Override
            public void finish() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 创建文本消息
     *
     * @param text
     */
    public IMMessage createTextMessage(String text) {
        IMMessage textMessage = MessageBuilder.createTextMessage(mSessionId, mSessionType, text);
        return textMessage;
    }
}

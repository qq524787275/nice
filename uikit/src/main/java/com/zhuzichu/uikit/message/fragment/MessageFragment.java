package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.library.widget.OnClickListener;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentMessageBinding;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;
import com.zhuzichu.uikit.observer.action.ActionMessageStatus;
import com.zhuzichu.uikit.observer.action.ActionReceiveMessage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageFragment extends NiceSwipeFragment<FragmentMessageBinding> {

    public interface Extras {
        String EXTRA_SESSION_ID = "extra_session_id";
        String EXTRA_SESSION_TYPE = "extra_session_type";
    }

    public MessageMultipItemAdapter mAdapter;
    public LinearLayoutManager mLayoutMamnager;
    // p2p对方Account或者群id
    public String mSessionId;
    public SessionTypeEnum mSessionType;
    public FragmentMessageBinding mBind;
    public boolean mIsFirstLoad = true;

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
        mBind = binding;
        parseExtras();
        initView();
        initListener();
        initObserver();
    }

    @Override
    public void onResume() {
        super.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(mSessionId, mSessionType);

    }

    @Override
    public void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,
                SessionTypeEnum.None);
    }

    private void initObserver() {
        //键盘弹出监听
        Disposable dispSoftKeyboard = RxBus.getIntance()
                .toObservable(ActionSoftKeyboard.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> scrollToBottom());
        //消息接受监听
        Disposable dispReceiveMessage = RxBus.getIntance().toObservable(ActionReceiveMessage.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(actionReceiveMessage -> filterMessage(actionReceiveMessage.getData()))
                .subscribe(list -> {
                    if (list.size() == 0)
                        return;
                    mAdapter.addData(list);
                    if (isLastMessageVisible()) {
                        smoothToBottom();
                    } else {
                        mBind.tvNewMessage.setVisibility(View.VISIBLE);
                    }
                });

        //监听消息状态
        Disposable dispMessageStatus = RxBus.getIntance().toObservable(ActionMessageStatus.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.getData())
                .filter(msg -> msg.getSessionId().equals(mSessionId))//过滤掉不是本会话的消息
                .subscribe(msg -> {
                    List<IMMessage> data = mAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        IMMessage item = data.get(i);
                        if (item.getUuid().equals(msg.getUuid())) {
                            data.set(i, msg);
                            mAdapter.refreshNotifyItemChanged(i);
                            break;
                        }
                    }
                });

        RxBus.getIntance().addSubscription(this.getClass().getSimpleName() + mSessionId,
                dispSoftKeyboard,
                dispMessageStatus,
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

        //发送
        mBind.input.setSendClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                IMMessage textMessage = createTextMessage(mBind.input.getInputText());
                sendMessage(textMessage, false);
                mAdapter.addData(textMessage);
                smoothToBottom();
                mBind.input.cleanInput();
            }
        });

        mBind.tvNewMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                scrollToBottom();
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

        mBind.refresh.setOnRefreshListener(refreshLayout -> loadFromLocal(mAdapter.getData().get(0)));
    }

    private void parseExtras() {
        mSessionId = getArguments().getString(Extras.EXTRA_SESSION_ID);
        mSessionType = (SessionTypeEnum) getArguments().getSerializable(Extras.EXTRA_SESSION_TYPE);
    }

    private void initView() {
        mBind.input.initContentView(mBind.layoutContent, this);
        mLayoutMamnager = new LinearLayoutManager(getContext());
        mBind.listMessage.setLayoutManager(mLayoutMamnager);
        mBind.listMessage.requestDisallowInterceptTouchEvent(false);
        mAdapter = new MessageMultipItemAdapter();
        mBind.listMessage.setAdapter(mAdapter);
        mBind.fastScroller.attachRecyclerView(mBind.listMessage);
    }


    @Override
    public boolean onBackPressedSupport() {
        if (!mBind.input.interceptBackPress()) {
            return false;
        }
        return true;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        loadFromLocal(MessageBuilder.createEmptyMessage(mSessionId, mSessionType, 0));
    }

    public void loadFromLocal(IMMessage anchor) {
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD, 20, true)
                .setCallback(new NiceRequestCallback<List<IMMessage>>(getActivity()) {
                    @Override
                    public void success(List<IMMessage> list) {
                        if (mIsFirstLoad) {
                            mAdapter.addData(list);
                            scrollToBottom();
                            mIsFirstLoad = false;
                        } else {
                            //上拉刷新数据
                            mAdapter.addData(0, list);
                            mBind.refresh.finishRefresh();
                        }
                    }
                });
    }

    /**
     * 瞬间到底
     */
    public void scrollToBottom() {
        int position = mAdapter.getData().size() - 1;
        if (position >= 0) {
            mLayoutMamnager.scrollToPositionWithOffset(position, 0);
            mBind.listMessage.postDelayed(() -> mBind.listMessage.scrollBy(0, -1), 50);
        }
    }

    /**
     * 滑动到底
     */
    public void smoothToBottom() {
        int position = mAdapter.getData().size() - 1;
        if (position < 0)
            return;
        mBind.listMessage.postDelayed(() -> mBind.listMessage.scrollToPosition(position), 200);
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
        NIMClient.getService(MsgService.class).sendMessage(msg, resend);
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

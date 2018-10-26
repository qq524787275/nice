package com.zhuzichu.nice;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.action.ActionUnreadCountChange;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.library.view.bottom.BottomBar;
import com.zhuzichu.library.view.bottom.BottomBarTab;
import com.zhuzichu.library.view.drop.DropManager;
import com.zhuzichu.nice.contact.ContactFragment;
import com.zhuzichu.nice.databinding.FragmentMainBinding;
import com.zhuzichu.nice.person.PersonFragment;
import com.zhuzichu.nice.session.SessionFragment;
import com.zhuzichu.nice.work.WorkFragment;

import io.reactivex.disposables.Disposable;

public class MainFragment extends NiceFragment<FragmentMainBinding> {
    private FragmentMainBinding mBind;
    private NiceFragment[] mFragments = new NiceFragment[4];
    public static final int SESSION = 0;
    public static final int WORK = 1;
    public static final int CONTACT = 2;
    public static final int PERSON = 3;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void init(FragmentMainBinding binding) {
        mBind = binding;
        mBind.setColor(ColorManager.getInstance().color);
        getLifecycle().addObserver(mBind.fish);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragments();
        initObserver();
        initUnreadCover();
    }

    /**
     * 初始化未读小红点
     */
    private void initUnreadCover() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mBind.unreadCover.getLayoutParams();
            layoutParams.topMargin = DensityUtils.getStatuBarH(getContext());
        }
        DropManager.getInstance().init(getContext(), mBind.unreadCover, (id, explosive) -> {
            if (id == null || !explosive) {
                return;
            }
            if (id instanceof RecentContact) {
                RecentContact r = (RecentContact) id;
                NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
            } else if (id instanceof Integer) {
                Integer position = (Integer) id;
                if (position == SESSION) {
                    NIMClient.getService(MsgService.class).clearAllUnreadCount();
                }
            }

        });
    }

    private void initObserver() {
        Disposable dispMainStart = RxBus.getIntance().doSubscribe(ActionMainStartFragmnet.class, target -> {
            if (target.animations.isPresent()) {
                ActionMainStartFragmnet.Animations animations = target.animations.get();
                extraTransaction().setCustomAnimations(
                        animations.targetFragmentEnter,
                        animations.currentFragmentPopExit,
                        animations.currentFragmentPopEnter,
                        animations.targetFragmentExit
                ).startDontHideSelf(target.data);
            } else {
                start(target.data);
            }
        });

        Disposable dispUnredCount = RxBus.getIntance().doSubscribe(ActionUnreadCountChange.class, action -> {
            mBind.bottomBar.getItem(action.position).setUnreadCount(action.count);
        });

        RxBus.getIntance().addSubscription(this, dispMainStart, dispUnredCount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
    }

    private void initView() {

        mBind.bottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon0, getString(R.string.main_session)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon1, getString(R.string.main_work)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon2, getString(R.string.main_contact)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon3, getString(R.string.main_person)));

        mBind.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _status.hide();
    }

    private void initFragments() {
        NiceFragment firstFragment = findChildFragment(SessionFragment.class);
        if (firstFragment == null) {
            mFragments[SESSION] = SessionFragment.newInstance();
            mFragments[WORK] = WorkFragment.newInstance();
            mFragments[CONTACT] = ContactFragment.newInstance();
            mFragments[PERSON] = PersonFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_tab_container, SESSION,
                    mFragments[SESSION],
                    mFragments[WORK],
                    mFragments[CONTACT],
                    mFragments[PERSON]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[SESSION] = firstFragment;
            mFragments[WORK] = findChildFragment(WorkFragment.class);
            mFragments[CONTACT] = findChildFragment(ContactFragment.class);
            mFragments[PERSON] = findChildFragment(PersonFragment.class);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableMsgNotification(false);
        //quitOtherActivities();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    private void enableMsgNotification(boolean enable) {
        boolean msg = (mBind.bottomBar.getCurrentItemPosition() != SESSION);
        if (enable | msg) {
            /**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }
}

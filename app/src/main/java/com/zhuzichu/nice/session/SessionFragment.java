package com.zhuzichu.nice.session;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tencent.smtt.sdk.QbSdk;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.action.ActionUnreadCountChange;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.bean.CountryBean;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.ui.scaner.ScannerFragment;
import com.zhuzichu.library.ui.webview.activity.BrowserActivity;
import com.zhuzichu.library.view.popou.MenuPopup;
import com.zhuzichu.nice.MainFragment;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentSessionBinding;
import com.zhuzichu.nice.login.fragment.MultiportLoginFragment;
import com.zhuzichu.uikit.message.fragment.MessageFragment;
import com.zhuzichu.uikit.message.fragment.MessageP2pFragment;
import com.zhuzichu.uikit.message.fragment.MessageTeamFragment;
import com.zhuzichu.uikit.session.fragment.SessionListFragment;
import com.zhuzichu.uikit.widget.OnlineStatusView;

import io.reactivex.disposables.Disposable;

public class SessionFragment extends NiceFragment<FragmentSessionBinding> {
    private FragmentSessionBinding mBind;
    private MenuPopup mMenuPopup;
    private SessionListFragment mSessListFragment;

    public static SessionFragment newInstance() {

        Bundle args = new Bundle();

        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_session;
    }

    @Override
    public void init(FragmentSessionBinding binding) {
        mBind = binding;
        mBind.setColor(ColorManager.getInstance().color);
        getLifecycle().addObserver(mBind.multiport);
        mSessListFragment = SessionListFragment.newInstance();
        loadRootFragment(R.id.list_session, mSessListFragment);
        initTopBar();
        initMenuPopup();
        initObserve();
        initListener();
    }


    private void initListener() {
        mSessListFragment.setRecentContactCallBack(new SessionListFragment.RecentContactCallBack() {
            @Override
            public void onSessionItemClick(RecentContact contact) {
                BaseFragment target;
                switch (contact.getSessionType()) {
                    case P2P:
                        target = MessageP2pFragment.newInstance(contact.getContactId(), contact.getSessionType());
                        break;
                    case Team:
                        target = MessageTeamFragment.newInstance(contact.getContactId(), contact.getSessionType());
                        break;
                    default:
                        target = MessageFragment.newInstance(contact.getContactId(), contact.getSessionType());
                        break;
                }
                mBind.topbar.postDelayed(() -> {
                    RxBus.getIntance().post(new ActionMainStartFragmnet(target));
                }, 200);
            }

            @Override
            public void onUnreadCountChange(int unreadNum) {
                RxBus.getIntance().post(new ActionUnreadCountChange(MainFragment.SESSION, unreadNum));
            }
        });

        mBind.multiport.setOnClickListener(view -> RxBus.getIntance().post(new ActionMainStartFragmnet(MultiportLoginFragment.newInstance(mBind.multiport.getData()), ActionMainStartFragmnet.getModalAnimations())));
    }


    private void initObserve() {
        Disposable disposable = RxBus.getIntance().doSubscribe(CountryBean.class, bean -> Toast.makeText(_mActivity, bean.getLabel(), Toast.LENGTH_SHORT).show());


        RxBus.getIntance().addSubscription(this, disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
    }

    private static final String TAG = "SessionFragment";

    private void initMenuPopup() {
        mMenuPopup = new MenuPopup(getActivity());

        mMenuPopup.addItem("扫一扫", () -> {
            RxBus.getIntance().post(new ActionMainStartFragmnet(ScannerFragment.newInstance(), ActionMainStartFragmnet.getModalAnimations()));
        });

        mMenuPopup.addItem("安装X5内核", () -> {
            if (QbSdk.getTbsVersion(getActivity()) == 0) {
                Toast.makeText(_mActivity, "还没有安装内核", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(_mActivity, "已经安装了内核", Toast.LENGTH_SHORT).show();
            }
            BrowserActivity.startActivity(getActivity(), BrowserActivity.URL_DEBUGTBS);
        });

        mMenuPopup.addItem("打开百度", () -> {
            BrowserActivity.startActivity(getActivity(), "https://www.baidu.com/");
        });
    }

    private void initTopBar() {
        mBind.topbar.setTitle(R.string.main_session);
        mBind.topbar.setTitleGravity(Gravity.CENTER);
        OnlineStatusView statusView = new OnlineStatusView(getActivity());
        getLifecycle().addObserver(statusView);
        mBind.topbar.addLeftView(statusView, R.id.topbar_left_online_status);
        mBind.topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_session_menu)
                .setOnClickListener(view -> mMenuPopup.show(mBind.topbar));

    }

}

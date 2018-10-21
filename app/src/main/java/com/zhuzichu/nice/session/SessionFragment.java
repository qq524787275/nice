package com.zhuzichu.nice.session;

import android.animation.Animator;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.action.ActionUnreadCountChange;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.bean.CountryBean;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorConfig;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.ui.country.fragment.SelectCountryFragment;
import com.zhuzichu.library.ui.scaner.ScannerFragment;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.library.view.popou.MenuPopup;
import com.zhuzichu.library.view.reveal.animation.ViewAnimationUtils;
import com.zhuzichu.nice.MainFragment;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentSessionBinding;
import com.zhuzichu.nice.login.LoginActivity;
import com.zhuzichu.nice.view.OnlineStatusView;
import com.zhuzichu.uikit.message.fragment.MessageFragment;
import com.zhuzichu.uikit.message.fragment.MessageP2pFragment;
import com.zhuzichu.uikit.message.fragment.MessageTeamFragment;
import com.zhuzichu.uikit.session.fragment.SessionListFragment;

import io.reactivex.disposables.Disposable;

public class SessionFragment extends NiceFragment<FragmentSessionBinding> {
    private static final String TAG = "SessionFragment";
    private FragmentSessionBinding mBinding;
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
        mBinding = binding;
        mBinding.setColor(ColorManager.getInstance().color);
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
                mBinding.topbar.postDelayed(() -> {
                    RxBus.getIntance().post(new ActionMainStartFragmnet(target));
                }, 200);
            }

            @Override
            public void onUnreadCountChange(int unreadNum) {
                RxBus.getIntance().post(new ActionUnreadCountChange(MainFragment.SESSION, unreadNum));
            }
        });
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

    private void initMenuPopup() {
        mMenuPopup = new MenuPopup(getActivity());
        mMenuPopup.addItem("设置", () -> {
            RxBus.getIntance().post(new ActionMainStartFragmnet(SelectCountryFragment.newInstance(), ActionMainStartFragmnet.getModalAnimations()));
        });

        mMenuPopup.addItem("注销", () -> {
            UserPreferences.saveUserToken("");
            LoginActivity.start(getActivity());
            getActivity().finish();
        });

        mMenuPopup.addItem("主题颜色", () -> {
            new ColorChooserDialog.Builder(getActivity(), R.string.app_name)
                    .titleSub(R.string.app_name)
                    .accentMode(true)
                    .theme(ColorManager.getInstance().getColorConfig().isDark ? Theme.DARK : Theme.LIGHT)
                    .doneButton(R.string.md_done_label)
                    .cancelButton(R.string.md_cancel_label)
                    .backButton(R.string.md_back_label)
                    .preselect(getResources().getColor(R.color.colorPrimary))
                    .dynamicButtonColor(true)
                    .show(getActivity());
        });

        mMenuPopup.addItem("夜间模式", () -> {
            ColorConfig color = ColorManager.getInstance().color;
            color.setDark(!color.isDark);
            // 获取FloatingActionButton的中心点的坐标
            View v = getParentFragment().getView();
            View content = v.findViewById(R.id.layout_content);
            View overlay = v.findViewById(R.id.layout_overlay);

            int centerX = (content.getLeft() + content.getRight()) / 2;
            int centerY = (content.getTop() + content.getBottom()) / 2;
            float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);
            Animator mCircularReveal = ViewAnimationUtils.createCircularReveal(content, centerX, centerY, 0, finalRadius);
            mCircularReveal.setInterpolator(new AccelerateInterpolator());
            Drawable drawable = new BitmapDrawable(QMUIDrawableHelper.createBitmapFromView(content));

            overlay.setBackgroundDrawable(drawable);
            mCircularReveal.setDuration(500).start();
        });

        mMenuPopup.addItem("扫一扫", () -> {
            RxBus.getIntance().post(new ActionMainStartFragmnet(ScannerFragment.newInstance(), ActionMainStartFragmnet.getModalAnimations()));
        });

        mMenuPopup.addItem("点击", () -> {
            Toast.makeText(_mActivity, "" + (NIMClient.getService(MsgService.class).getTotalUnreadCount()), Toast.LENGTH_SHORT).show();
        });
    }

    private void initTopBar() {
        mBinding.topbar.setTitle(R.string.main_session);
        mBinding.topbar.setTitleGravity(Gravity.CENTER);
        OnlineStatusView statusView = new OnlineStatusView(getActivity());
        getLifecycle().addObserver(statusView);
        mBinding.topbar.addLeftView(statusView, R.id.topbar_left_online_status);
        mBinding.topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_session_menu)
                .setOnClickListener(view -> mMenuPopup.show(mBinding.topbar));
    }

}

package com.zhuzichu.uikit.user.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.widget.OnClickListener;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentUserCardBinding;
import com.zhuzichu.uikit.message.fragment.MessageP2pFragment;
import com.zhuzichu.uikit.user.presenter.UserCardPresenter;
import com.zhuzichu.uikit.user.viewmodel.UserCardViewModel;

/**
 * 个人名片
 */
public class UserCardFragment extends NiceSwipeFragment<FragmentUserCardBinding> {
    private FragmentUserCardBinding mBind;
    private UserCardViewModel mViewModel;
    private String mAccount;

    public interface Extras {
        String EXTRA_ACCOUNT = "extra_account";
    }

    public static UserCardFragment newInstance(String account) {

        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_ACCOUNT, account);
        UserCardFragment fragment = new UserCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_user_card;
    }

    @Override
    public void init(FragmentUserCardBinding binding) {
        mBind = binding;
        mAccount = getArguments().getString(Extras.EXTRA_ACCOUNT);
        initTopBar();
        mViewModel = ViewModelProviders.of(this).get(UserCardViewModel.class);
        mBind.setViewmodel(mViewModel);
        mBind.setColor(ColorManager.getInstance().color);
        mBind.setPresenter(getPresenter());
    }


    private UserCardPresenter getPresenter() {
        return new UserCardPresenter() {
            @Override
            public void toChat(View view) {
                OnClickListener.noDoubleClick(() -> RxBus.getIntance()
                        .post(new ActionMainStartFragmnet(MessageP2pFragment.newInstance(mAccount, SessionTypeEnum.P2P))));
            }
        };
    }

    private void initTopBar() {
        mBind.topbar.setTitle("个人名片");
        mBind.topbar.addLeftBackImageButton().setOnClickListener(view -> pop());
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mViewModel.loadUserInfo(mAccount);
    }
}

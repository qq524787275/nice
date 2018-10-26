package com.zhuzichu.nice.login.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.utils.MD5;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.nice.MainActivity;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentLoginBinding;

public class LoginFragment extends NiceFragment<FragmentLoginBinding> {
    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding mBind;
    private QMUITipDialog mLoading;
    private AbortableFuture<LoginInfo> mLoginRequest;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void init(FragmentLoginBinding binding) {
        mBind = binding;
        initTopBar();
        initView();
        initListener();
    }

    private void initListener() {
        mBind.btnSubmit.setOnClickListener(view -> doLogin(mBind.etAccount.getText().toString(), MD5.getStringMD5(mBind.tietPassword.getText().toString())));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _status.setColor(R.color.qmui_config_color_white);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    private void initView() {
        mLoading = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在登录")
                .create();
        mLoading.setOnCancelListener(dialogInterface -> {
            if (mLoginRequest != null) {
                mLoginRequest.abort();
                mLoginRequest = null;
            }
        });
        showSoftInput(mBind.tietPassword);
    }


    private void initTopBar() {
        mBind.topbar.setTitle("登录");
    }

    public void doLogin(String account, String token) {
        mLoading.show();
        LoginInfo info = new LoginInfo(account, token); // config...
        if (mLoginRequest != null) {
            mLoginRequest.abort();
            mLoginRequest = null;
        }
        mLoginRequest = NIMClient.getService(AuthService.class).login(info);
        mLoginRequest.setCallback(new NiceRequestCallback<LoginInfo>(getActivity()) {
            @Override
            public void success(LoginInfo param) {
                UserPreferences.saveUserAccountAndToken(param.getAccount(), param.getToken());
                Nice.setAccount(param.getAccount());
                mLoading.cancel();
                MainActivity.start(getActivity());
                getActivity().finish();
            }

            @Override
            public void finish() {
                mLoading.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
    }
}

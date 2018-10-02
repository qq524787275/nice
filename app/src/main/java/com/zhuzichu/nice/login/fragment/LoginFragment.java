package com.zhuzichu.nice.login.fragment;

import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.enmu.ErrorEnmu;
import com.zhuzichu.library.utils.MD5;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.nice.MainActivity;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentLoginBinding;

public class LoginFragment extends NiceFragment<FragmentLoginBinding> {
    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding mBinding;
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
        mBinding = binding;
        initTopBar();
        initView();
        initListener();
    }

    private void initListener() {
        mBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin(mBinding.etAccount.getText().toString(), MD5.getStringMD5(mBinding.tietPassword.getText().toString()));
            }
        });


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
        mLoading.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (mLoginRequest != null) {
                    mLoginRequest.abort();
                    mLoginRequest = null;
                }
            }
        });
        showSoftInput(mBinding.tietPassword);
    }


    private void initTopBar() {
        mBinding.topbar.setTitle("登录");
    }

    public void doLogin(String account, String token) {
        mLoading.show();
        LoginInfo info = new LoginInfo(account, token); // config...
        if (mLoginRequest != null) {
            mLoginRequest.abort();
            mLoginRequest = null;
        }
        mLoginRequest = NIMClient.getService(AuthService.class).login(info);
        mLoginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Log.i(TAG, "onSuccess: ");
                UserPreferences.saveUserAccountAndToken(param.getAccount(), param.getToken());
                mLoading.cancel();
                MainActivity.start(getActivity());
                getActivity().finish();
//                startWithPop(MainFragment.newInstance());
            }

            @Override
            public void onFailed(int code) {
                Toast.makeText(getActivity(), ErrorEnmu.getErrorEnmu(code).getMsg(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailed: " + code);
                mLoading.cancel();
            }

            @Override
            public void onException(Throwable exception) {
                Log.i(TAG, "onException: ");
                mLoading.cancel();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftInput();
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
    }
}

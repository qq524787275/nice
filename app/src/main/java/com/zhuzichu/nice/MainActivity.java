package com.zhuzichu.nice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.netease.nimlib.sdk.StatusCode;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.base.BaseActivity;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.uikit.observer.ObserverManager;
import com.zhuzichu.uikit.observer.action.ActionOnlineStatus;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.library.widget.AndroidBug5497Workaround;
import com.zhuzichu.nice.login.LoginActivity;

import javax.annotation.Nullable;

import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {
    private static final String TAG = "MainActivity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseFragment setRootFragment() {
        return MainFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidBug5497Workaround.assistActivity(this);
        QMUIStatusBarHelper.translucent(this);
        ObserverManager.regist();
        initObserver();
    }

    private void initObserver() {
        Disposable disposable = RxBus.getIntance().doSubscribe(ActionOnlineStatus.class, action -> {
            StatusCode code = action.getData();
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    Log.i(TAG, "onChanged: NET_BROKEN");
                } else if (code == StatusCode.UNLOGIN) {
                    Log.i(TAG, "onChanged: UNLOGIN");
                } else if (code == StatusCode.CONNECTING) {
                    Log.i(TAG, "onChanged: CONNECTING");
                } else if (code == StatusCode.LOGINING) {
                    Log.i(TAG, "onChanged: LOGINING");
                } else {

                }
            }
        });
        RxBus.getIntance().addSubscription(this, disposable);
    }

    /**
     * @param code 登录被踢
     */
    private void kickOut(StatusCode code) {
        if (code == StatusCode.PWD_ERROR) {
            Log.i(TAG, "kickOut: user password error");
        } else {
            Log.i(TAG, "kickOut: Kicked");
        }
        onLogout();
    }

    private void onLogout() {
        UserPreferences.saveUserToken("");
        LoginActivity.start(this, true);
        finish();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        ColorManager.getInstance().getColorConfig().setColorPrimary(selectedColor);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
        ObserverManager.unRegist();
    }
}

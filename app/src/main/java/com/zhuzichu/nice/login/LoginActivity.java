package com.zhuzichu.nice.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.ClientType;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseActivity;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.permission.MPermission;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionDenied;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionGranted;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionNeverAskAgain;
import com.zhuzichu.library.utils.DialogUtils;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.login.fragment.LoginFragment;

import javax.annotation.Nullable;

public class LoginActivity extends BaseActivity {
    public interface Extra{
        String EXTRA_KICK_OUT="kick_out";
    }

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent();
        intent.putExtra(Extra.EXTRA_KICK_OUT, kickOut);
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public BaseFragment setRootFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        requestBasicPermission();
        onParseIntent();
    }

    private void requestBasicPermission() {
        MPermission.with(LoginActivity.this)
                .setRequestCode(Nice.PermissionCode.BASIC_PERMISSION_REQUEST_CODE)
                .permissions(Nice.Permission.BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(Nice.PermissionCode.BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(Nice.PermissionCode.BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(Nice.PermissionCode.BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
    }

    private void onParseIntent() {
        if (getIntent().getBooleanExtra(Extra.EXTRA_KICK_OUT, false)) {
            int type = NIMClient.getService(AuthService.class).getKickedClientType();
            String client;
            switch (type) {
                case ClientType.Web:
                    client = "网页端";
                    break;
                case ClientType.Windows:
                case ClientType.MAC:
                    client = "电脑端";
                    break;
                case ClientType.REST:
                    client = "服务端";
                    break;
                default:
                    client = "移动端";
                    break;
            }
            DialogUtils.showInfoDialog(this, getString(R.string.kickout_notify), String.format(getString(R.string.kickout_content), client));
        }
    }
}

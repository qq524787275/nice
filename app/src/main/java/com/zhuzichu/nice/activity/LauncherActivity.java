package com.zhuzichu.nice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.nice.MainActivity;
import com.zhuzichu.nice.login.LoginActivity;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(UserPreferences.getUserToken())){
            LoginActivity.start(this);
        }else {
            MainActivity.start(this);
        }
        finish();
    }
}

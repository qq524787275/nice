package com.zhuzichu.nice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhuzichu.nice.login.LoginActivity;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        LoginActivity.start(this);
        finish();
    }
}

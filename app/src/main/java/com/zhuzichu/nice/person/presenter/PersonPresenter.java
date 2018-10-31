package com.zhuzichu.nice.person.presenter;

import android.view.View;

import com.zhuzichu.library.base.BasePresenter;

public interface PersonPresenter extends BasePresenter {
    void goDetail(View view);

    void goSetting(View view);

    void switchColor(View view);

    void switchDarkChanged(View view, boolean isChecked);

    void switchDark(View view);

    void logout(View view);
}

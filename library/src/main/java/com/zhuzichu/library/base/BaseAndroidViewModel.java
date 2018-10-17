package com.zhuzichu.library.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class BaseAndroidViewModel extends AndroidViewModel {
    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
    }
}

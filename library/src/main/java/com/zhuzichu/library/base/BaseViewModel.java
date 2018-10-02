package com.zhuzichu.library.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.zhuzichu.library.Nice;
import com.zhuzichu.library.comment.color.ColorConfig;

public class BaseViewModel extends AndroidViewModel{
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}

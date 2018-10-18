package com.zhuzichu.library.comment.bus.livedata;

@SuppressWarnings("WeakerAccess")
public class MutableLiveEvent<T> extends LiveEvent<T> {
    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }
}
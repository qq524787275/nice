package com.zhuzichu.library.comment.bus.livedata;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.Map;

public class LiveDataEventBus {

    private final Map<String, BusLiveEvent<Object>> mCacheBus;

    private LiveDataEventBus() {
        mCacheBus = new ArrayMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataEventBus DEFAULT_BUS = new LiveDataEventBus();
    }

    private static LiveDataEventBus get() {
        return SingletonHolder.DEFAULT_BUS;
    }

    public static MutableLiveEvent<Object> with(@Nullable String key) {
        return get().withInfo(key, Object.class, false);
    }

    public static <T> MutableLiveEvent<T> with(@Nullable String key, Class<T> type) {
        return get().withInfo(key, type, false);
    }

    public static <T> MutableLiveEvent<T> with(@Nullable String key, Class<T> type, boolean needCurrentDataWhenNewObserve) {
        return get().withInfo(key, type, needCurrentDataWhenNewObserve);
    }

    private <T> MutableLiveEvent<T> withInfo(String key, Class<T> type, boolean needData) {
        if (!mCacheBus.containsKey(key)) {
            mCacheBus.put(key, new BusLiveEvent<>(key));
        }
        BusLiveEvent<Object> data = mCacheBus.get(key);
        data.mNeedCurrentDataWhenFirstObserve = needData;
        return (MutableLiveEvent<T>) mCacheBus.get(key);
    }

    public Map getCacheBus(){
        return mCacheBus;
    }

    private static class BusLiveEvent<T> extends MutableLiveEvent<T> {

        // 比如BusLiveData 在添加 observe的时候，同一个界面对应的一个事件只能注册一次
        // 自己添加逻辑定制
        private String mEventType;

        //首次注册的时候，是否需要当前LiveData 最新数据
        private boolean mNeedCurrentDataWhenFirstObserve;

        private BusLiveEvent(String eventType) {
            this.mEventType = eventType;
        }

        //主动触发数据更新事件才通知所有Observer
        private boolean mIsStartChangeData = false;

        @Override
        public void setValue(T value) {
            mIsStartChangeData = true;
            super.setValue(value);
        }

        @Override
        public void postValue(T value) {
            mIsStartChangeData = true;
            super.postValue(value);
        }

        //添加注册对应事件type的监听
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observe(owner, new ObserverWrapper<>(observer, this));
        }

        //数据更新一直通知刷新
        @Override
        public void observeForever(@NonNull Observer<T> observer) {
            super.observeForever(observer);
        }

        @Override
        public void removeObserver(@NonNull Observer<T> observer) {
            super.removeObserver(observer);
            LiveDataEventBus.get().getCacheBus().remove(mEventType);
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;
        private BusLiveEvent<T> liveData;

        private ObserverWrapper(Observer<T> observer, BusLiveEvent<T> liveData) {
            this.observer = observer;
            this.liveData = liveData;
            //mIsStartChangeData 可过滤掉liveData首次创建监听，之前的遗留的值
            liveData.mIsStartChangeData = liveData.mNeedCurrentDataWhenFirstObserve;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (liveData.mIsStartChangeData) {
                if (observer != null) {
                    observer.onChanged(t);
                }
            }
        }
    }
}
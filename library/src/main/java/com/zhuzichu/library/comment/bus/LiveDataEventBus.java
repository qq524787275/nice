package com.zhuzichu.library.comment.bus;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 目前还有bug
 */
@Deprecated
public class LiveDataEventBus {
    private static final String TAG = "LiveDataEventBus";
    private final Map<String, BusLiveData<Object>> mCacheBus;

    private LiveDataEventBus() {
        mCacheBus = new ArrayMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataEventBus DEFAULT_BUS = new LiveDataEventBus();
    }

    private static LiveDataEventBus get() {
        return SingletonHolder.DEFAULT_BUS;
    }


    public static MutableLiveData<String> with(@Nullable String key) {
        return get().withInfo(key, String.class);
    }

    public static <T> MutableLiveData<T> with(@Nullable String key, Class<T> type) {
        return get().withInfo(key, type);
    }


    private <T> MutableLiveData<T> withInfo(String key, Class<T> type) {
        if (!mCacheBus.containsKey(key)) {
            mCacheBus.put(key, new BusLiveData<>());
        }
        return (MutableLiveData<T>) mCacheBus.get(key);
    }

    private static class BusLiveData<T> extends MutableLiveData<T> implements Observable<T> {

        private class PostValueTask implements Runnable {
            private Object newValue;

            public PostValueTask(@NonNull Object newValue) {
                this.newValue = newValue;
            }

            @Override
            public void run() {
                setValue((T) newValue);
            }
        }

        private Map<Observer, Observer> observerMap = new HashMap<>();
        private Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void postValue(T value) {
            mainHandler.post(new PostValueTask(value));
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observe(owner, observer);
        }

        public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observe(owner, observer);
        }

        @Override
        public void observeForever(@NonNull Observer<T> observer) {
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, observer);
            }
            super.observeForever(observerMap.get(observer));
        }

        public void observeStickyForever(@NonNull Observer<T> observer) {
            super.observeForever(observer);
        }

        @Override
        public void removeObserver(@NonNull Observer<T> observer) {
            Observer realObserver = null;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }
            super.removeObserver(realObserver);
        }
    }

    public interface Observable<T> {
        void setValue(T value);

        void postValue(T value);

        void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer);

        void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer);

        void observeForever(@NonNull Observer<T> observer);

        void observeStickyForever(@NonNull Observer<T> observer);

        void removeObserver(@NonNull Observer<T> observer);
    }
}
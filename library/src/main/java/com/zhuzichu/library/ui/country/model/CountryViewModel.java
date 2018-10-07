package com.zhuzichu.library.ui.country.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuzichu.library.bean.CountryBean;
import com.zhuzichu.library.utils.AssetsUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wb.zhuzichu18 on 2018/9/7.
 */
public class CountryViewModel extends AndroidViewModel {

    private MutableLiveData<List<CountryBean>> mLiveCountrys = new MutableLiveData<>();
    private Context mContext;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public CountryViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void loadCountrys() {
        mCompositeDisposable.add(Observable.
                create((ObservableOnSubscribe<List<CountryBean>>) emitter -> {
                    List<CountryBean> dtoCountries = new Gson().fromJson(AssetsUtils.getJson("country.json", mContext), new TypeToken<List<CountryBean>>() {
                    }.getType());
                    emitter.onNext(dtoCountries);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dtoCountries -> mLiveCountrys.setValue(dtoCountries)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }

    public MutableLiveData<List<CountryBean>> getLiveCountrys() {
        return mLiveCountrys;
    }
}

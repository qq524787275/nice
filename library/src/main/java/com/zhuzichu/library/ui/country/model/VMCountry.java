package com.zhuzichu.library.ui.country.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuzichu.library.dto.DTOCountry;
import com.zhuzichu.library.utils.AssetsUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wb.zhuzichu18 on 2018/9/7.
 */
public class VMCountry extends AndroidViewModel {

    private MutableLiveData<List<DTOCountry>> mLiveCountrys = new MutableLiveData<>();
    private Context mContext;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public VMCountry(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void loadCountrys() {
        mCompositeDisposable.add(Observable.
                create(new ObservableOnSubscribe<List<DTOCountry>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<DTOCountry>> emitter) throws Exception {
                        List<DTOCountry> dtoCountries = new Gson().fromJson(AssetsUtils.getJson("country.json", mContext), new TypeToken<List<DTOCountry>>() {
                        }.getType());
                        emitter.onNext(dtoCountries);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DTOCountry>>() {
                    @Override
                    public void accept(List<DTOCountry> dtoCountries) throws Exception {
                        mLiveCountrys.setValue(dtoCountries);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }

    public MutableLiveData<List<DTOCountry>> getLiveCountrys() {
        return mLiveCountrys;
    }
}

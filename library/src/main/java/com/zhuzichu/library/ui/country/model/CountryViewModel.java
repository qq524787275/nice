package com.zhuzichu.library.ui.country.model;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.zhuzichu.library.base.BaseViewModel;
import com.zhuzichu.library.bean.CountryBean;
import com.zhuzichu.library.ui.country.model.entry.CountryData;
import com.zhuzichu.library.ui.country.repositorys.CountryRepository;
import com.zhuzichu.library.utils.ThreadManager;
import com.zhuzichu.library.utils.livedata.LiveDataEventBus;

import java.util.List;

/**
 * Created by wb.zhuzichu18 on 2018/9/7.
 */
public class CountryViewModel extends BaseViewModel {
    public static final String TAG="CountryViewModel";

    private ThreadManager threadManager=new ThreadManager(ThreadManager.Type.CachedThread,-1);

    private MutableLiveData<CountryData> mLiveCountrys = new MutableLiveData<>();

    public void loadCountrys(Context mContext) {
        threadManager.execute(new Runnable() {
            @Override
            public void run() {
                List<CountryBean> dtoCountries = CountryRepository.getCountrys(mContext);
                LiveDataEventBus.with(TAG).postValue(new CountryData(dtoCountries,CountryRepository.getHotCountrys(dtoCountries)));
            }
        });
    }

    public MutableLiveData<CountryData> getLiveCountrys() {
        return mLiveCountrys;
    }
}

package com.zhuzichu.library.ui.country.repositorys;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuzichu.library.bean.CountryBean;
import com.zhuzichu.library.utils.AssetsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkangfa on 2018/10/15.
 */

public class CountryRepository {

    /**
     * 获取全部国家数据
     * @param mContext
     * @return
     */
    public static List<CountryBean> getCountrys(Context mContext){
        String json = AssetsUtils.getJson("country.json", mContext);
        List<CountryBean> dtoCountries = new Gson().fromJson(json, new TypeToken<List<CountryBean>>() {}.getType());
        return dtoCountries==null?new ArrayList<>():dtoCountries;
    }

    /**
     * 筛选出热门国家
     * @param dtoCountries
     * @return
     */
    public static List<CountryBean> getHotCountrys(List<CountryBean> dtoCountries){
        List<CountryBean> hotCountrys = new ArrayList<>();
        for (CountryBean item : dtoCountries) {
            boolean ishot = item.isIshot();
            if (ishot) {
                hotCountrys.add(item);
            }
        }
        return hotCountrys;
    }

}

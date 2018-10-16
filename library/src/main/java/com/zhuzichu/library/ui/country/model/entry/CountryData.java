package com.zhuzichu.library.ui.country.model.entry;

import com.zhuzichu.library.bean.CountryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 国家实体类(所有国家以及热门国家)
 * Created by huangkangfa on 2018/10/15.
 */

public class CountryData {
    private List<CountryBean> countrys;
    private List<CountryBean> hotCountrys;

    public CountryData() {
        this.countrys=new ArrayList<>();
        this.hotCountrys=new ArrayList<>();
    }

    public CountryData(List<CountryBean> countrys, List<CountryBean> hotCountrys) {
        this.countrys = countrys;
        this.hotCountrys = hotCountrys;
    }

    public List<CountryBean> getCountrys() {
        return countrys;
    }

    public void setCountrys(List<CountryBean> countrys) {
        this.countrys = countrys;
    }

    public List<CountryBean> getHotCountrys() {
        return hotCountrys;
    }

    public void setHotCountrys(List<CountryBean> hotCountrys) {
        this.hotCountrys = hotCountrys;
    }
}

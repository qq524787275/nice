package com.zhuzichu.library.ui.country.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.R;
import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.bean.CountryBean;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.databinding.FragmentSelectCountryBinding;
import com.zhuzichu.library.ui.country.adapter.CountryAdapter;
import com.zhuzichu.library.ui.country.model.CountryViewModel;
import com.zhuzichu.library.widget.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;

public class SelectCountryFragment extends NiceSwipeFragment<FragmentSelectCountryBinding> {
    private static final String TAG = "SelectCountryFragment";
    public static final String SELECT_COUNTRY = "select_country";
    private FragmentSelectCountryBinding mBinding;
    private CountryAdapter mAdapter;
    private CountryViewModel mCountryViewModel;
    //防止多点触控

    public static SelectCountryFragment newInstance() {

        Bundle args = new Bundle();

        SelectCountryFragment fragment = new SelectCountryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_select_country;
    }

    @Override
    public void init(FragmentSelectCountryBinding binding) {
        mBinding = binding;
        initTopBar();
        initView();
        initData();
    }

    private void initTopBar() {
        mBinding.topbar.setTitle("地区选择");
        mBinding.topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _status.setColor(R.color.qmui_config_color_white);
        _status.autoColorPrimary(false);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    private void initData() {
        mCountryViewModel = ViewModelProviders.of(this).get(CountryViewModel.class);
        mCountryViewModel.getLiveCountrys().observe(this, new Observer<List<CountryBean>>() {
            @Override
            public void onChanged(@Nullable List<CountryBean> dtoCountries) {
                spliteHotCountry(dtoCountries);
            }


        });
        Log.i(TAG, "initData: ");
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        mCountryViewModel.loadCountrys();
    }

    private void spliteHotCountry(List<CountryBean> dtoCountries) {
        List<CountryBean> countrys = new ArrayList<>();
        List<CountryBean> hotCountrys = new ArrayList<>();
        for (CountryBean item : dtoCountries) {
            boolean ishot = item.isIshot();
            if (ishot) {
                hotCountrys.add(item);
            } else {
                countrys.add(item);
            }
        }
        mAdapter.setDatas(dtoCountries);
        mBinding.layoutIndex.addHeaderAdapter(new SimpleHeaderAdapter<>(mAdapter, "热", "常用国家和地区", hotCountrys));
        mBinding.layoutEmpty.hide();
    }

    private void initView() {
        mBinding.layoutIndex.setLayoutManager(new LinearLayoutManager(getContext()));
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        mBinding.layoutIndex.setCompareMode(IndexableLayout.MODE_FAST);
        mAdapter = new CountryAdapter(getContext());
        mBinding.layoutIndex.setAdapter(mAdapter);
        mAdapter.setOnItemContentClickListener((v, originalPosition, currentPosition, entity) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - OnClickListener.lastClickTime > OnClickListener.minTime) {
                OnClickListener.lastClickTime = currentTime;
                RxBus.getIntance().post(entity);
                pop();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QMUIStatusBarHelper.setStatusBarDarkMode(getActivity());
    }
}

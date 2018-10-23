package com.zhuzichu.uikit.search.fragment;

import android.os.Bundle;
import android.view.View;

import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentSearchBinding;
import com.zhuzichu.uikit.search.presenter.SearchPresenter;

import javax.annotation.Nullable;

public class SearchFragment extends NiceSwipeFragment<FragmentSearchBinding> {
    private FragmentSearchBinding bind;

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_search;
    }

    @Override
    public void init(FragmentSearchBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
        binding.setPresenter(getPresenter());
        bind = binding;
    }

    private SearchPresenter getPresenter() {
        return new SearchPresenter() {
            @Override
            public void cancel(View view) {
                pop();
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showSoftInput(bind.search.getEditText());
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }
}

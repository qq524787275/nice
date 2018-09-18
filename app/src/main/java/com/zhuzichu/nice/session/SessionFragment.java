package com.zhuzichu.nice.session;

import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.dto.DTOCountry;
import com.zhuzichu.library.ui.country.fragment.SelectCountryFragment;
import com.zhuzichu.library.utils.LiveDataEventBus;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentSessionBinding;
import com.zhuzichu.uikit.session.fragment.SessionListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SessionFragment extends NiceFragment {
    private static final String TAG = "SessionFragment";
    private FragmentSessionBinding mBinding;
    private QMUIListPopup mMenuListPopup;

    public static SessionFragment newInstance() {

        Bundle args = new Bundle();

        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_session;
    }
    @Override
    public void init(ViewDataBinding binding) {
        mBinding = (FragmentSessionBinding) binding;

        loadRootFragment(R.id.list_session, SessionListFragment.newInstance());
        initTopBar();
        initMenuPopup();
        initObserve();
    }


    private void initObserve() {
        LiveDataEventBus.with(SelectCountryFragment.SELECT_COUNTRY).observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object o) {
                DTOCountry dtoCountry=(DTOCountry)o;
                Toast.makeText(_mActivity, dtoCountry.getLabel(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onChanged: "+dtoCountry.getLabel());
            }
        });
    }

    private void initMenuPopup() {
        String[] listItems = new String[]{
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
        };
        List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
        mMenuListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_NONE, adapter);
        mMenuListPopup.create(QMUIDisplayHelper.dp2px(getContext(), 250), QMUIDisplayHelper.dp2px(getContext(), 200), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((NiceFragment)getParentFragment()).start(SelectCountryFragment.newInstance());
                mMenuListPopup.dismiss();
            }
        });
        mMenuListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    private void initTopBar() {
        mBinding.topbar.setTitle(R.string.main_session);
        mBinding.topbar.addRightImageButton(R.mipmap.ic_add, R.id.topbar_right_session_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                mMenuListPopup.setPreferredDirection(QMUIPopup.DIRECTION_NONE);
                mMenuListPopup.show(view);
            }
        });
    }

}

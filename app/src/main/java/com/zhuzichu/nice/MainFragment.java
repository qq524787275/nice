package com.zhuzichu.nice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.view.bottom.BottomBar;
import com.zhuzichu.library.view.bottom.BottomBarTab;
import com.zhuzichu.nice.contact.ContactFragment;
import com.zhuzichu.nice.databinding.FragmentMainBinding;
import com.zhuzichu.nice.person.PersonFragment;
import com.zhuzichu.nice.session.SessionFragment;
import com.zhuzichu.nice.work.WorkFragment;

public class MainFragment extends NiceFragment<FragmentMainBinding> {
    private FragmentMainBinding mBinding;
    private NiceFragment[] mFragments=new NiceFragment[4];
    public static final int SESSION = 0;
    public static final int WORK = 1;
    public static final int CONTACT = 2;
    public static final int PERSON = 3;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void init(FragmentMainBinding binding) {
        mBinding = binding;
        mBinding.setColor(ColorManager.getInstance().color);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragments();
    }

    private void initView() {

        mBinding.bottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon0, getString(R.string.main_session)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon1, getString(R.string.main_work)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.main_tab_icon2, getString(R.string.main_contact)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap. main_tab_icon3, getString(R.string.main_person)));


        mBinding.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _status.hide();
    }

    private void initFragments() {
        NiceFragment firstFragment = findChildFragment(SessionFragment.class);
        if (firstFragment == null) {
            mFragments[SESSION] = SessionFragment.newInstance();
            mFragments[WORK] = WorkFragment.newInstance();
            mFragments[CONTACT] = ContactFragment.newInstance();
            mFragments[PERSON] = PersonFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_tab_container, SESSION,
                    mFragments[SESSION],
                    mFragments[WORK],
                    mFragments[CONTACT],
                    mFragments[PERSON]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[SESSION] = firstFragment;
            mFragments[WORK] = findChildFragment(WorkFragment.class);
            mFragments[CONTACT] = findChildFragment(ContactFragment.class);
            mFragments[PERSON] = findChildFragment(PersonFragment.class);
        }

    }
}

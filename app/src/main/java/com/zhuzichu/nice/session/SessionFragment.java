package com.zhuzichu.nice.session;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.color.ColorConfig;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.dto.DTOCountry;
import com.zhuzichu.library.ui.country.fragment.SelectCountryFragment;
import com.zhuzichu.library.utils.LiveDataEventBus;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentSessionBinding;
import com.zhuzichu.nice.login.LoginActivity;
import com.zhuzichu.uikit.session.fragment.SessionListFragment;

public class SessionFragment extends NiceFragment<FragmentSessionBinding> {
    private static final String TAG = "SessionFragment";
    private FragmentSessionBinding mBinding;
    private PopupMenu mPopupMenu;

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
    public void init(FragmentSessionBinding binding) {
        mBinding = binding;
        mBinding.setColor(ColorManager.getInstance().color);
        loadRootFragment(R.id.list_session, SessionListFragment.newInstance());
        initTopBar();
        initMenuPopup();
        initObserve();
    }


    private void initObserve() {
        LiveDataEventBus.with(SelectCountryFragment.SELECT_COUNTRY, DTOCountry.class).observe(this, new Observer<DTOCountry>() {
            @Override
            public void onChanged(@Nullable DTOCountry country) {
                Toast.makeText(_mActivity, country.getLabel(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onChanged: " + country.getLabel());
            }
        });
    }

    private void initMenuPopup() {
        mPopupMenu = new PopupMenu(_mActivity, mBinding.topbar, GravityCompat.END);
        mPopupMenu.inflate(R.menu.session_pop);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_setting:
                        ((NiceFragment) getParentFragment()).extraTransaction()
                                .setCustomAnimations(R.anim.v_fragment_enter, 0, 0, R.anim.v_fragment_exit)
                                .startDontHideSelf(SelectCountryFragment.newInstance());
                        break;
                    case R.id.action_logout:
                        UserPreferences.saveUserToken("");
                        LoginActivity.start(getActivity());
                        getActivity().finish();
                        break;
                    case R.id.action_color:
                        new ColorChooserDialog.Builder(getActivity(), R.string.app_name)
                                .titleSub(R.string.app_name)  // title of dialog when viewing shades of a color
                                .accentMode(true)  // when true, will display accent palette instead of primary palette
                                .doneButton(R.string.md_done_label)  // changes label of the done button
                                .cancelButton(R.string.md_cancel_label)  // changes label of the cancel button
                                .backButton(R.string.md_back_label)  // changes label of the back button
                                .preselect(getResources().getColor(R.color.colorPrimary))  // optionally preselects a color
                                .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                                .show(getActivity()); // an AppCompatActivity which implements ColorCallback
                        break;
                    case R.id.action_dark:
                        ColorConfig color = ColorManager.getInstance().color;
                        color.setDark(!color.isDark);
                        break;
                }
                mPopupMenu.dismiss();
                return true;
            }
        });
    }

    private void initTopBar() {
        mBinding.topbar.setTitle(R.string.main_session);
        mBinding.topbar.addRightImageButton(R.mipmap.icon_topbar_overflow, R.id.topbar_right_session_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupMenu.show();
            }
        });
    }

}

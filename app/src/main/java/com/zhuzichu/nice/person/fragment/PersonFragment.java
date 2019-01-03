package com.zhuzichu.nice.person.fragment;

import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorConfig;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.library.view.reveal.animation.ViewAnimationUtils;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentPersonBinding;
import com.zhuzichu.nice.login.LoginActivity;
import com.zhuzichu.nice.person.presenter.PersonPresenter;
import com.zhuzichu.nice.person.viewmodel.PersonViewModel;

public class PersonFragment extends NiceFragment<FragmentPersonBinding> {
    private PersonViewModel mViewModel;

    public static PersonFragment newInstance() {

        Bundle args = new Bundle();

        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBind.setColor(ColorManager.getInstance().color);
        mBind.setPresenter(getPresenter());
        mViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        mBind.setViewmodel(mViewModel);
        initTopBar();
    }

    private PersonPresenter getPresenter() {
        return new PersonPresenter() {
            @Override
            public void goDetail(View view) {
                Toast.makeText(_mActivity, "去详情", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void goSetting(View view) {
                RxBus.getIntance().post(new ActionMainStartFragmnet(SettingFragment.newInstance()));
            }

            @Override
            public void switchColor(View view) {
                new ColorChooserDialog.Builder(getActivity(), R.string.app_name)
                        .titleSub(R.string.app_name)
                        .accentMode(true)
                        .theme(ColorManager.getInstance().getColorConfig().isDark ? Theme.DARK : Theme.LIGHT)
                        .doneButton(R.string.md_done_label)
                        .cancelButton(R.string.md_cancel_label)
                        .backButton(R.string.md_back_label)
                        .preselect(getResources().getColor(R.color.colorPrimary))
                        .dynamicButtonColor(true)
                        .show(getActivity());
            }

            @Override
            public void switchDarkChanged(View view, boolean isChecked) {
                ColorConfig color = ColorManager.getInstance().color;
                color.setDark(isChecked);
                // 获取FloatingActionButton的中心点的坐标
                View v = getParentFragment().getView();
                View content = v.findViewById(R.id.layout_content);
                View overlay = v.findViewById(R.id.layout_overlay);

                int centerX = (content.getLeft() + content.getRight()) / 2;
                int centerY = (content.getTop() + content.getBottom()) / 2;
                float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);
                Animator mCircularReveal = ViewAnimationUtils.createCircularReveal(content, centerX, centerY, 0, finalRadius);
                mCircularReveal.setInterpolator(new AccelerateInterpolator());
                Drawable drawable = new BitmapDrawable(QMUIDrawableHelper.createBitmapFromView(content));

                overlay.setBackgroundDrawable(drawable);
                mCircularReveal.setDuration(500).start();
            }

            @Override
            public void switchDark(View view) {
                mBind.cbDark.setChecked(!mBind.cbDark.isChecked());
            }

            @Override
            public void logout(View view) {
                UserPreferences.saveUserToken("");
                LoginActivity.start(getActivity());
                getActivity().finish();
            }
        };
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewModel.loadPersonInfo();
    }

    private void initTopBar() {
        mBind.topbar.setTitle("我");
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_person;
    }
}

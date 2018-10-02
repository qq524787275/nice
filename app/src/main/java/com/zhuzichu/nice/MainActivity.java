package com.zhuzichu.nice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseActivity;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.color.ColorManager;

import javax.annotation.Nullable;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback{

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public BaseFragment setRootFragment() {
        return MainFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        Nice.init(this).registListener(this);
    }
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        Toast.makeText(this, ""+ QMUIColorHelper.colorToString(selectedColor), Toast.LENGTH_SHORT).show();
        ColorManager.getInstance().getColorConfig().setColorPrimary(selectedColor);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }
}

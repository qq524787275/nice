package com.zhuzichu.nice.work;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentWorkBinding;

public class WorkFragment extends NiceFragment<FragmentWorkBinding> {
    public static WorkFragment newInstance() {

        Bundle args = new Bundle();

        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBind.setColor(ColorManager.getInstance().color);
        mBind.btn.setOnClickListener(v -> mBind.load.start());
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_work;
    }
}

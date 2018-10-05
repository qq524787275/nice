package com.zhuzichu.library.view.popou;

import android.databinding.ViewDataBinding;

import com.zhuzichu.library.BR;
import com.zhuzichu.library.R;
import com.zhuzichu.library.base.BaseNiceAdapter;
import com.zhuzichu.library.base.BaseNiceViewHolder;
import com.zhuzichu.library.comment.color.ColorManager;

import java.util.ArrayList;

public class MenuAdapter extends BaseNiceAdapter<MenuItem, BaseNiceViewHolder> {


    public MenuAdapter() {
        super(R.layout.item_menu, new ArrayList<MenuItem>());
    }

    @Override
    protected void convert(BaseNiceViewHolder helper, MenuItem item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.menuItem, item);
        binding.setVariable(BR.color, ColorManager.getInstance().color);
    }
}
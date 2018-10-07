package com.zhuzichu.library.view.popou;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhuzichu.library.BR;
import com.zhuzichu.library.R;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.databinding.ItemMenuBinding;

import java.util.ArrayList;

public class MenuAdapter extends BaseDataBindingAdapter<MenuItem, ItemMenuBinding> {


    public MenuAdapter() {
        super(R.layout.item_menu, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemMenuBinding binding, MenuItem item) {
        binding.setVariable(BR.menuItem, item);
        binding.setVariable(BR.color, ColorManager.getInstance().color);
    }
}
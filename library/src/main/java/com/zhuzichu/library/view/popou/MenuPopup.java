package com.zhuzichu.library.view.popou;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.qmuiteam.qmui.util.QMUIResHelper;
import com.zhuzichu.library.R;

public class MenuPopup extends PopupWindow {
    private Context mContext;
    private View mRoot;
    private MenuAdapter mAdapter;

    public MenuPopup(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        mRoot = LayoutInflater.from(mContext).inflate(R.layout.popup_menu, null);
        setBackgroundDrawable(new ColorDrawable());
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(mRoot);
        //设置宽和高解决5.0以下版本不显示
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        RecyclerView rv = mRoot.findViewById(R.id.menu);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MenuAdapter();
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mAdapter.getData().get(position).listener.onMenuClick();
            dismiss();
        });
    }

    public MenuPopup addItem(int icon, String title, onMenuItemClickListener clickListener) {
        MenuItem item = new MenuItem(icon, title);
        item.listener = clickListener;
        mAdapter.addData(item);
        return this;
    }

    public MenuPopup addItem(String title, onMenuItemClickListener clickListener) {
        MenuItem item = new MenuItem(-1, title);
        item.listener = clickListener;
        mAdapter.addData(item);
        return this;
    }

    public interface onMenuItemClickListener {
        void onMenuClick();
    }

    public void show(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            showAsDropDown(anchor, 0, 0, Gravity.TOP | Gravity.END);
        } else {
            showAtLocation(anchor.getRootView(), Gravity.TOP | Gravity.END, 0,2* QMUIResHelper.getAttrDimen(mContext, R.attr.qmui_topbar_height));
        }
//        YoYo.with(Techniques.BounceIn).duration(300).playOn(mRoot);
    }
}



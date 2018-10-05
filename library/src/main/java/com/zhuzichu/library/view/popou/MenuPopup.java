package com.zhuzichu.library.view.popou;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhuzichu.library.R;
import com.zhuzichu.library.comment.animation.Techniques;
import com.zhuzichu.library.comment.animation.YoYo;

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

        RecyclerView rv = mRoot.findViewById(R.id.menu);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MenuAdapter();
        rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.getData().get(position).listener.onMenuClick();
                dismiss();
            }
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
        showAsDropDown(anchor, 0, 0, Gravity.TOP | Gravity.END);
        YoYo.with(Techniques.BounceIn).duration(300).playOn(mRoot);
    }
}



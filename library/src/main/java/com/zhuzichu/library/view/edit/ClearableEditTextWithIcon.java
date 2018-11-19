package com.zhuzichu.library.view.edit;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhuzichu.library.R;
import com.zhuzichu.library.widget.TextWatcherWrapper;


public class ClearableEditTextWithIcon extends FrameLayout {
    private ImageView clear;
    private EditText edit;

    public ClearableEditTextWithIcon(Context context) {
        this(context, null);
    }

    public ClearableEditTextWithIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearableEditTextWithIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.view_clearab_edittext, this);
        initView();
    }

    private void initView() {
        clear = findViewById(R.id.clear);
        edit = findViewById(R.id.edit);
        clear.setVisibility(TextUtils.isEmpty(edit.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
        edit.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable s) {
                clear.setVisibility(TextUtils.isEmpty(s.toString()) ? View.INVISIBLE : View.VISIBLE);
            }
        });

        clear.setOnClickListener(v -> {
            edit.setText("");
        });
    }

    public EditText getEditText() {
        return edit;
    }
}

package com.zhuzichu.uikit.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhuzichu.library.utils.ScreenUtils;
import com.zhuzichu.library.view.button.StateButton;
import com.zhuzichu.library.widget.TextWatcherWrapper;
import com.zhuzichu.uikit.R;

public class InputView extends LinearLayout {
    private View contentView;
    //文本输入框
    private EditText input;
    //表情面板
    private View emojiPanelView;
    private StateButton send;
    private View plus;
    private InputMethodManager inputMethodManager;
    private SharedPreferences sharedPreferences;

    private static final String EMOJI_KEYBOARD = "EmojiKeyboard";
    private static final String KEY_SOFT_KEYBOARD_HEIGHT = "SoftKeyboardHeight";
    private static final int SOFT_KEYBOARD_HEIGHT_DEFAULT = 654;
    private Handler handler;

    public InputView(Context context) {
        this(context, null);
    }

    public InputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_input, this);
        init();
    }

    public void initContentView(View contentView) {
        this.contentView = contentView;

        this.input.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && this.emojiPanelView.isShown()) {
                lockContentViewHeight();
                hideEmojiPanel(true);
                unlockContentViewHeight();
            }
            return false;
        });

        this.contentView.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (this.emojiPanelView.isShown()) {
                    hideEmojiPanel(false);
                } else if (isSoftKeyboardShown()) {
                    hideSoftKeyboard();
                }
            }
            return false;
        });
        //用于弹出表情面板的View
        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputView.this.emojiPanelView.isShown()) {
                    lockContentViewHeight();
                    hideEmojiPanel(true);
                    unlockContentViewHeight();
                } else {
                    if (isSoftKeyboardShown()) {
                        lockContentViewHeight();
                        showEmojiPanel();
                        unlockContentViewHeight();
                    } else {
                        showEmojiPanel();
                    }
                }
            }
        });
        this.inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.sharedPreferences = getContext().getSharedPreferences(EMOJI_KEYBOARD, Context.MODE_PRIVATE);
        ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.handler = new Handler();

        if (!sharedPreferences.contains(KEY_SOFT_KEYBOARD_HEIGHT)) {
            handler.postDelayed(() -> showSoftKeyboard(true), 200);
        }
    }

    private void init() {
        emojiPanelView = findViewById(R.id.layout_panel_warpper);
        input = findViewById(R.id.input_msg);
        plus = findViewById(R.id.plus);
        send = findViewById(R.id.send);

        input.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    send.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.GONE);
                } else {
                    send.setVisibility(View.GONE);
                    plus.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 当点击返回键时需要先隐藏表情面板
     */
    public boolean interceptBackPress() {
        if (emojiPanelView.isShown()) {
            hideEmojiPanel(false);
            return true;
        }
        return false;
    }

    /**
     * 锁定内容View以防止跳闪
     */
    private void lockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = contentView.getHeight();
        layoutParams.weight = 0;
    }

    /**
     * 释放锁定的内容View
     */
    private void unlockContentViewHeight() {
        handler.postDelayed(() -> ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1, 200);
    }

    /**
     * 获取键盘的高度
     */
    private int getSoftKeyboardHeight() {
        Rect rect = new Rect();
        ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //屏幕当前可见高度，不包括状态栏
        int displayHeight = rect.bottom - rect.top;
        //屏幕可用高度
        int availableHeight = ScreenUtils.getAvailableScreenHeight(((Activity) getContext()));
        //用于计算键盘高度
        int softInputHeight = availableHeight - displayHeight - ScreenUtils.getStatusBarHeight(((Activity) getContext()));
        if (softInputHeight != 0) {
            // 因为考虑到用户可能会主动调整键盘高度，所以只能是每次获取到键盘高度时都将其存储起来
            sharedPreferences.edit().putInt(KEY_SOFT_KEYBOARD_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 获取本地存储的键盘高度值或者是返回默认值
     */
    private int getSoftKeyboardHeightLocalValue() {
        return sharedPreferences.getInt(KEY_SOFT_KEYBOARD_HEIGHT, SOFT_KEYBOARD_HEIGHT_DEFAULT);
    }

    /**
     * 判断是否显示了键盘
     */
    private boolean isSoftKeyboardShown() {
        return getSoftKeyboardHeight() != 0;
    }

    /**
     * 令编辑框获取焦点并显示键盘
     */
    private void showSoftKeyboard(boolean saveSoftKeyboardHeight) {
        input.requestFocus();
        inputMethodManager.showSoftInput(input, 0);
        if (saveSoftKeyboardHeight) {
            handler.postDelayed(() -> getSoftKeyboardHeight(), 200);
        }
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    /**
     * 显示表情面板
     */
    private void showEmojiPanel() {
        int softKeyboardHeight = getSoftKeyboardHeight();
        if (softKeyboardHeight == 0) {
            softKeyboardHeight = getSoftKeyboardHeightLocalValue();
        } else {
            hideSoftKeyboard();
        }
        emojiPanelView.getLayoutParams().height = softKeyboardHeight;
        emojiPanelView.setVisibility(View.VISIBLE);
        if (emojiPanelVisibilityChangeListener != null) {
            emojiPanelVisibilityChangeListener.onShowEmojiPanel();
        }
    }

    /**
     * 隐藏表情面板，同时指定是否随后开启键盘
     */
    private void hideEmojiPanel(boolean showSoftKeyboard) {
        if (emojiPanelView.isShown()) {
            emojiPanelView.setVisibility(View.GONE);
            if (showSoftKeyboard) {
                showSoftKeyboard(false);
            }
            if (emojiPanelVisibilityChangeListener != null) {
                emojiPanelVisibilityChangeListener.onHideEmojiPanel();
            }
        }
    }

    public interface OnEmojiPanelVisibilityChangeListener {

        void onShowEmojiPanel();

        void onHideEmojiPanel();
    }

    private OnEmojiPanelVisibilityChangeListener emojiPanelVisibilityChangeListener;

    public void setEmoticonPanelVisibilityChangeListener(OnEmojiPanelVisibilityChangeListener emojiPanelVisibilityChangeListener) {
        this.emojiPanelVisibilityChangeListener = emojiPanelVisibilityChangeListener;
    }

    public void setSendColor(int color) {
        send.setNormalBackgroundColor(color);
    }

    public void setInputBackgroundColor(int color) {
        setBackgroundColor(color);
    }


    public void setInputTextColor(int color) {
        input.setTextColor(color);
    }

    public void setSendClickListener(OnClickListener sendClickListener) {
        send.setOnClickListener(sendClickListener);
    }

    public String getInputText() {
        return input.getText().toString();
    }

    public void cleanInput() {
        input.setText("");
    }

    @BindingAdapter({"inputBackgroundColor"})
    public static void inputBackgroundColor(InputView view, int color) {
        view.setInputBackgroundColor(color);
    }


    @BindingAdapter({"inputTextColor"})
    public static void inputTextColor(InputView view, int color) {
        view.setInputTextColor(color);
    }

    @BindingAdapter({"sendColor"})
    public static void sendColor(InputView view, int color) {
        view.setSendColor(color);
    }
}
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

import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.utils.ScreenUtils;
import com.zhuzichu.library.view.button.StateButton;
import com.zhuzichu.library.widget.TextWatcherWrapper;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.fragment.MessageEmotionFragment;
import com.zhuzichu.uikit.message.fragment.MessagePlusFragment;

public class InputView extends LinearLayout implements View.OnClickListener {
    private View mContentView;
    private EditText mInput;
    private View mPanelWarpper;
    private StateButton mSend;
    private View mPlus;
    private View mEmotion;
    private View mVoice;
    private View mKeyboard;
    private StateButton mRecord;
    private InputMethodManager mInputMethodManager;
    private SharedPreferences mSharedPreferences;
    private Handler mHandler;
    private BaseFragment mFragment;
    private MessagePlusFragment mPlushFragment;
    private MessageEmotionFragment mEmotionFragment;

    private static final String EMOJI_KEYBOARD = "EmojiKeyboard";
    private static final String KEY_SOFT_KEYBOARD_HEIGHT = "SoftKeyboardHeight";
    private static final int SOFT_KEYBOARD_HEIGHT_DEFAULT = 654;

    //当前id
    private int currentId;

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

    public void initContentView(View contentView, BaseFragment baseFragment) {
        this.mContentView = contentView;
        this.mFragment = baseFragment;
        mPlushFragment = MessagePlusFragment.newInstance();
        mEmotionFragment = MessageEmotionFragment.newInstance();
        //用于弹出表情面板的View
        mPlus.setOnClickListener(this);
        mEmotion.setOnClickListener(this);

        mVoice.setOnClickListener(v -> {
            if (mPanelWarpper.isShown()) {
                hideEmojiPanel(false);
            } else if (isSoftKeyboardShown()) {
                hideSoftKeyboard();
            }
            mKeyboard.setVisibility(VISIBLE);
            mVoice.setVisibility(GONE);
            mRecord.setVisibility(VISIBLE);
            mInput.setVisibility(GONE);
        });

        mKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPanelWarpper.isShown()) {
                    hideEmojiPanel(false);
                } else if (isSoftKeyboardShown()) {
                    hideSoftKeyboard();
                }

                mKeyboard.setVisibility(GONE);
                mVoice.setVisibility(VISIBLE);
                mRecord.setVisibility(GONE);
                mInput.setVisibility(VISIBLE);
            }
        });

        this.mInput.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && mPanelWarpper.isShown()) {
                lockContentViewHeight();
                hideEmojiPanel(true);
                unlockContentViewHeight();
            }
            return false;
        });

        this.mContentView.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (mPanelWarpper.isShown()) {
                    hideEmojiPanel(false);
                } else if (isSoftKeyboardShown()) {
                    hideSoftKeyboard();
                }
            }
            return false;
        });


        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mSharedPreferences = getContext().getSharedPreferences(EMOJI_KEYBOARD, Context.MODE_PRIVATE);
        ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mHandler = new Handler();

        if (!mSharedPreferences.contains(KEY_SOFT_KEYBOARD_HEIGHT)) {
            mHandler.postDelayed(() -> showSoftKeyboard(true), 200);
        }
    }

    private void init() {
        mPanelWarpper = findViewById(R.id.layout_panel_warpper);
        mInput = findViewById(R.id.input_msg);
        mPlus = findViewById(R.id.plus);
        mSend = findViewById(R.id.send);
        mEmotion = findViewById(R.id.emotion);
        mVoice = findViewById(R.id.voice);
        mKeyboard = findViewById(R.id.keyboard);
        mRecord = findViewById(R.id.record);

        mInput.addTextChangedListener(new TextWatcherWrapper() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mSend.setVisibility(View.VISIBLE);
                    mPlus.setVisibility(View.GONE);
                } else {
                    mPlus.setVisibility(View.VISIBLE);
                    mSend.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 当点击返回键时需要先隐藏表情面板
     */
    public boolean interceptBackPress() {
        if (mPanelWarpper.isShown()) {
            hideEmojiPanel(false);
            return true;
        }
        return false;
    }

    /**
     * 锁定内容View以防止跳闪
     */
    private void lockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        layoutParams.height = mContentView.getHeight();
        layoutParams.weight = 0;
    }

    /**
     * 释放锁定的内容View
     */
    private void unlockContentViewHeight() {
        mHandler.postDelayed(() -> ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1, 200);
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
            mSharedPreferences.edit().putInt(KEY_SOFT_KEYBOARD_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 获取本地存储的键盘高度值或者是返回默认值
     */
    private int getSoftKeyboardHeightLocalValue() {
        return mSharedPreferences.getInt(KEY_SOFT_KEYBOARD_HEIGHT, SOFT_KEYBOARD_HEIGHT_DEFAULT);
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
        mInput.requestFocus();
        mInputMethodManager.showSoftInput(mInput, 0);
        if (saveSoftKeyboardHeight) {
            mHandler.postDelayed(() -> getSoftKeyboardHeight(), 200);
        }
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
    }

    /**
     * 显示表情面板
     */
    private void showEmojiPanel(View view) {
        if (view.getId() == R.id.plus) {
            //加载MessagePlusFragment
            mFragment.loadRootFragment(R.id.layout_panel, mPlushFragment, false, true);
        } else if (view.getId() == R.id.emotion) {
            //加载MessageEmotionFragment
            mFragment.loadRootFragment(R.id.layout_panel, mEmotionFragment, false, true);
        }

        int softKeyboardHeight = getSoftKeyboardHeight();
        if (softKeyboardHeight == 0) {
            softKeyboardHeight = getSoftKeyboardHeightLocalValue();
        } else {
            hideSoftKeyboard();
        }
        mPanelWarpper.getLayoutParams().height = softKeyboardHeight;
        mPanelWarpper.setVisibility(View.VISIBLE);
        if (emojiPanelVisibilityChangeListener != null) {
            emojiPanelVisibilityChangeListener.onShowEmojiPanel();
        }
    }

    /**
     * 隐藏表情面板，同时指定是否随后开启键盘
     */
    private void hideEmojiPanel(boolean showSoftKeyboard) {
        if (mPanelWarpper.isShown()) {
            mPanelWarpper.setVisibility(View.GONE);
            if (showSoftKeyboard) {
                showSoftKeyboard(false);
            }
            if (emojiPanelVisibilityChangeListener != null) {
                emojiPanelVisibilityChangeListener.onHideEmojiPanel();
            }
        }
    }

    @Override
    public void onClick(View v) {
        com.zhuzichu.library.widget.OnClickListener.noDoubleClick(() -> {
            if (isSoftKeyboardShown()) {
                lockContentViewHeight();
                showEmojiPanel(v);
                unlockContentViewHeight();
            } else {
                showEmojiPanel(v);
            }
        });
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
        mSend.setNormalBackgroundColor(color);
    }

    public void setInputBackgroundColor(int color) {
        setBackgroundColor(color);
    }


    public void setInputTextColor(int color) {
        mInput.setTextColor(color);
    }

    public void setSendClickListener(OnClickListener sendClickListener) {
        mSend.setOnClickListener(sendClickListener);
    }

    public String getInputText() {
        return mInput.getText().toString();
    }

    public void cleanInput() {
        mInput.setText("");
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
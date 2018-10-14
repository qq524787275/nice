package com.zhuzichu.library.view.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuzichu.library.R;
import com.zhuzichu.library.view.textview.HtmlTextView;

/**
 * Created by wb.zhuzichu18 on 2018/10/12.
 */
public class CollapsibleCard extends FrameLayout {
    private Context mContext;
    private String mCardTitle;
    private View mRoot;
    private RelativeLayout mTitleContainer;
    private TextView mCardTitleView;
    private HtmlTextView mCardDescriptionView;
    private ImageView mExpandIcon;
    private boolean expanded = false;
    private Transition mToggle;

    public CollapsibleCard(@NonNull Context context) {
        this(context, null);
    }

    public CollapsibleCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsibleCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CollapsibleCard, 0, 0);
        mCardTitle = typedArray.getString(R.styleable.CollapsibleCard_cardTitle);
        String cardDescription = typedArray.getString(R.styleable.CollapsibleCard_cardDescription);
        typedArray.recycle();
        mRoot = LayoutInflater.from(mContext).inflate(R.layout.view_collapsible_card, this, true);
        mTitleContainer = mRoot.findViewById(R.id.title_container);
        mCardTitleView = mRoot.findViewById(R.id.card_title);
        mCardTitleView.setText(mCardTitle);
        mCardDescriptionView = mRoot.findViewById(R.id.card_description);
        mCardDescriptionView.setText(cardDescription);
        mExpandIcon = mRoot.findViewById(R.id.expand_icon);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mExpandIcon.setImageTintList(AppCompatResources.getColorStateList(mContext, R.color.collapsing_section));
//        }

        mToggle = TransitionInflater.from(mContext)
                .inflateTransition(R.transition.info_card_toggle);
        mTitleContainer.setOnClickListener(view -> {
            toggleExpanded();
        });
    }

    private void toggleExpanded() {
        expanded = !expanded;
        if (expanded) {
            mToggle.setDuration(300L);
        } else {
            mToggle.setDuration(200L);
        }
        TransitionManager.beginDelayedTransition((ViewGroup) mRoot.getParent(), mToggle);
        if (expanded) {
            mCardDescriptionView.setVisibility(VISIBLE);
        } else {
            mCardDescriptionView.setVisibility(GONE);
        }

        if (expanded) {
            mExpandIcon.setRotation(180f);
        } else {
            mExpandIcon.setRotation(0f);
        }
        mExpandIcon.setActivated(expanded);
        mCardDescriptionView.setActivated(expanded);
        setTitleContentDescription(mCardTitle);

    }

    private void setTitleContentDescription(String cardTitle) {
        if (expanded) {
            mCardTitleView.setContentDescription(cardTitle + "," + "已展开");
        } else {
            mCardTitleView.setContentDescription(cardTitle + "," + "已收起");
        }
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.expanded = expanded;
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState)
            super.onRestoreInstanceState(state);

    }

    public static class SavedState extends BaseSavedState {
        boolean expanded = false;

        public SavedState(Parcelable source) {
            super(source);
        }

        public SavedState(Parcel source) {
            super(source);
            int b = source.readByte();
            expanded = b != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            if (expanded) {
                out.writeByte((byte) 0);
            } else {
                out.writeByte((byte) 1);
            }
        }

        public static Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

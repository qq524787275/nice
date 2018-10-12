package com.zhuzichu.library.view.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wb.zhuzichu18 on 2018/10/12.
 */
public class HtmlTextView extends AppCompatTextView {
    public HtmlTextView(Context context) {
        this(context, null);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) && (getText() instanceof Spanned)) {
            float x = event.getX();
            float y = event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            x += getScaleX();
            y += getScaleY();

            Layout layout = getLayout();
            int line = layout.getLineForVertical((int) y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = ((Spanned) getText()).getSpans(off, off, ClickableSpan.class);
            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(this);
                }
                return true;
            }
        }
        return false;
    }
}

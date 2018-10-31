package com.zhuzichu.uikit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.zhuzichu.library.comment.glide.GlideApp;
import com.zhuzichu.uikit.R;

import java.io.File;

public class MsgThumbImageView extends AppCompatImageView {
    public MsgThumbImageView(Context context) {
        super(context);
    }

    public MsgThumbImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MsgThumbImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * *************************** 自定义绘制 **********************************
     */

    private Drawable mask; // blend mask drawable

    private static final Paint paintMask = createMaskPaint();

    private static Paint createMaskPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mask != null) {
            // bounds
            int width = getWidth();
            int height = getHeight();

            // create blend layer
            canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

            //
            // mask
            //
            if (mask != null) {
                mask.setBounds(0, 0, width, height);
                mask.draw(canvas);
            }

            //
            // source
            //
            {
                canvas.saveLayer(0, 0, width, height, paintMask, Canvas.ALL_SAVE_FLAG);
                super.onDraw(canvas);
                canvas.restore();
            }

            // apply blend layer
            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    /**
     * *************************** 对外接口 **********************************
     */

    public void loadAsResource(final int resId, final int maskId) {
        setBlendDrawable(maskId);
        GlideApp.with(getContext().getApplicationContext()).load(resId).into(this);
    }

    public void loadAsPath(final String path, final int width, final int height, final int maskId, final String ext) {
        if (TextUtils.isEmpty(path)) {
            loadAsResource(R.drawable.image_default, maskId);
            return;
        }

        setBlendDrawable(maskId);

        RequestBuilder builder;
        if (isGif(ext)) {
            builder = GlideApp.with(getContext().getApplicationContext()).asGif().load(new File(path));
        } else {
            RequestOptions options = new RequestOptions()
                    .override(width, height)
                    .fitCenter()
                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default);

            builder = GlideApp.with(getContext().getApplicationContext())
                    .asBitmap()
                    .apply(options)
                    .load(new File(path));
        }
        builder.into(this);
    }

    private void setBlendDrawable(int maskId) {
        mask = maskId != 0 ? getResources().getDrawable(maskId) : null;
    }

    public static boolean isGif(String extension) {
        return !TextUtils.isEmpty(extension) && extension.toLowerCase().equals("gif");
    }
}

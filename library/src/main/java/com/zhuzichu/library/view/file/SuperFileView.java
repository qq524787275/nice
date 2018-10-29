package com.zhuzichu.library.view.file;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.smtt.sdk.TbsReaderView;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;

import java.io.File;

/**
 * Created by wb.zhuzichu18 on 2018/10/29.
 */
public class SuperFileView extends FrameLayout implements TbsReaderView.ReaderCallback {
    private TbsReaderView mTbsReaderView;
    private Context mContext;

    public SuperFileView(@NonNull Context context) {
        this(context, null);
    }

    public SuperFileView(@NonNull Context context, @NonNull AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperFileView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTbsReaderView = new TbsReaderView(context, this);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
        this.mContext = context;
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    private static final String TAG = "SuperFileView";
    public void displayFile(File file,String fileTyle) {
        Bundle bundle = new Bundle();
        String path = file.toString();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, path);
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, path);
        Toast.makeText(mContext, ""+fileTyle, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "displayFile: "+file.toString());
        boolean bool = this.mTbsReaderView.preOpen(fileTyle, false);
        if (bool) {
            Log.i(TAG, "displayFile: 执行了");
            this.mTbsReaderView.openFile(bundle);
        }
    }
}

package com.zhuzichu.library.view.file;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.TbsReaderView;

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

    public void displayFile(File file, String fileTyle) {
        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
        String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
        File bsReaderTempFile = new File(bsReaderTemp);

        if (!bsReaderTempFile.exists()) {
            boolean mkdir = bsReaderTempFile.mkdir();
            if (!mkdir) {
            }
        }

        Bundle bundle = new Bundle();
        String path = file.toString();
//        path="/storage/emulated/0/zzc/zzc1";
        bundle.putString(TbsReaderView.KEY_FILE_PATH, path+"."+fileTyle.toLowerCase());
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
        Log.i(TAG, "displayFile: " +  path);
        Log.i(TAG, "fileTyle: " + fileTyle);
        boolean bool = this.mTbsReaderView.preOpen(fileTyle.toLowerCase(), false);
        if (bool) {
            Log.i(TAG, "displayFile: 执行了");
            this.mTbsReaderView.openFile(bundle);
        }
    }

    public void onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
}

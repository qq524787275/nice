package com.zhuzichu.library.ui.scaner;


import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;
import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.permission.MPermission;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionDenied;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionGranted;
import com.zhuzichu.library.comment.permission.annotation.OnMPermissionNeverAskAgain;
import com.zhuzichu.library.comment.scaner.CameraManager;
import com.zhuzichu.library.comment.scaner.PlanarYUVLuminanceSource;
import com.zhuzichu.library.databinding.FragmentScannerBinding;
import com.zhuzichu.library.ui.matisse.Matisse;
import com.zhuzichu.library.ui.matisse.MimeType;
import com.zhuzichu.library.ui.matisse.engine.impl.GlideEngine;
import com.zhuzichu.library.ui.matisse.filter.Filter;
import com.zhuzichu.library.ui.matisse.filter.GifSizeFilter;
import com.zhuzichu.library.utils.BeepUtils;
import com.zhuzichu.library.utils.DialogUtils;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 二维码扫描
 */
public class ScannerFragment extends NiceSwipeFragment<FragmentScannerBinding> {

    @Override
    public Object setLayout() {
        return R.layout.fragment_scanner;
    }

    public static ScannerFragment newInstance() {

        Bundle args = new Bundle();

        ScannerFragment fragment = new ScannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private MultiFormatReader multiFormatReader;
    private FragmentScannerBinding mBind;
    /**
     * 是否有预览
     */
    private boolean hasSurface;
    /**
     * 扫描边界的宽度
     */
    private int mCropWidth = 0;

    /**
     * 扫描边界的高度
     */
    private int mCropHeight = 0;
    /**
     * 扫描处理
     */
    private CaptureActivityHandler handler;

    /**
     * 闪光灯
     */
    private boolean mFlashing;

    @Override
    public void init(FragmentScannerBinding binding) {
        mBind = binding;
        //申请权限
        requestBasicPermission();
        //初始化解码器
        initDecode();
        //初始化 CameraManager
        CameraManager.init(getActivity());
        mFlashing = true;
        hasSurface = false;
        mBind.setPresenter(getPresenter());
    }

    private ScannerPresenter getPresenter() {
        return new ScannerPresenter() {
            @Override
            public void light(View view) {
                if (mFlashing) {
                    // 开闪光灯
                    CameraManager.get().openLight();
                } else {
                    // 关闪光灯
                    CameraManager.get().offLight();
                }
                mFlashing = !mFlashing;
            }

            @Override
            public void back(View view) {
                pop();
            }

            @Override
            public void select(View view) {
                Matisse.from(getActivity())
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(1);
                Toast.makeText(getActivity(), "暂未实现", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initScanerAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mBind.captureScanLine.startAnimation(animation);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        //扫描动画初始化
        initScanerAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始相机
        initCapturePreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCapturePreview() {
        SurfaceHolder surfaceHolder = mBind.capturePreview.getHolder();
        if (hasSurface) {
            //Camera初始化
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (!hasSurface) {
                        hasSurface = true;
                        initCamera(holder);
                    }
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    hasSurface = false;

                }
            });
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    /**
     * //Camera初始化
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {

        try {
            CameraManager.get().openDriver(surfaceHolder);
            Point point = CameraManager.get().getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = mBind.captureCropLayout.getWidth() * width.get() / mBind.captureContainter.getWidth();
            int cropHeight = mBind.captureCropLayout.getHeight() * height.get() / mBind.captureContainter.getHeight();
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler();
        }
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public void setCropWidth(int cropWidth) {
        mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;
    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }

    private void initDecode() {
        multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
            ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
            ONE_D_FORMATS.add(BarcodeFormat.ITF);
            Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
            QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
            Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
            DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(ONE_D_FORMATS);
            decodeFormats.addAll(QR_CODE_FORMATS);
            decodeFormats.addAll(DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        multiFormatReader.setHints(hints);
    }

    private void requestBasicPermission() {
        MPermission.with(this)
                .setRequestCode(Nice.PermissionCode.SCANNER_PERMISSION_REQUEST_CODE)
                .permissions(Nice.Permission.SCANNER_PERMISSION)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(Nice.PermissionCode.SCANNER_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(Nice.PermissionCode.SCANNER_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(Nice.PermissionCode.SCANNER_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
        pop();
    }


    final class CaptureActivityHandler extends Handler {

        DecodeThread decodeThread = null;
        private State state;

        public CaptureActivityHandler() {
            decodeThread = new DecodeThread();
            decodeThread.start();
            state = State.SUCCESS;
            CameraManager.get().startPreview();
            restartPreviewAndDecode();
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == R.id.auto_focus) {
                if (state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                }
            } else if (message.what == R.id.restart_preview) {
                restartPreviewAndDecode();
            } else if (message.what == R.id.decode_succeeded) {
                state = State.SUCCESS;
                handleDecode((Result) message.obj);// 解析成功，回调
            } else if (message.what == R.id.decode_failed) {
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            }
        }

        public void quitSynchronously() {
            state = State.DONE;
            decodeThread.interrupt();
            CameraManager.get().stopPreview();
            removeMessages(R.id.decode_succeeded);
            removeMessages(R.id.decode_failed);
            removeMessages(R.id.decode);
            removeMessages(R.id.auto_focus);
        }

        private void restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        }
    }

    private void handleDecode(Result result) {
        BeepUtils.playBeep(getActivity(), true);
        DialogUtils.showInfoDialog(getActivity(), result.getText(), (dialog, which) -> {
            if (handler != null)
                handler.sendEmptyMessage(R.id.restart_preview);
        });
    }

    final class DecodeThread extends Thread {

        private final CountDownLatch handlerInitLatch;
        private Handler handler;

        DecodeThread() {
            handlerInitLatch = new CountDownLatch(1);
        }

        Handler getHandler() {
            try {
                handlerInitLatch.await();
            } catch (InterruptedException ie) {
                // continue?
            }
            return handler;
        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new DecodeHandler();
            handlerInitLatch.countDown();
            Looper.loop();
        }
    }

    private void decode(byte[] data, int width, int height) {
        Result rawResult = null;
        //modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }
        // Here we are swapping, that's the difference to #11
        int tmp = width;
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException e) {
            // continue
        } finally {
            multiFormatReader.reset();
        }

        if (rawResult != null) {
            Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable("barcode_bitmap", source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
        } else {
            Message message = Message.obtain(handler, R.id.decode_failed);
            message.sendToTarget();
        }
    }

    final class DecodeHandler extends Handler {
        DecodeHandler() {
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == R.id.decode) {
                decode((byte[]) message.obj, message.arg1, message.arg2);
            } else if (message.what == R.id.quit) {
                Looper.myLooper().quit();
            }
        }
    }

    /**
     * 是否开启
     */
    private void light() {
        if (mFlashing) {
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            // 关闪光灯
            CameraManager.get().offLight();
        }
        mFlashing = !mFlashing;
    }

    private enum State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }
}

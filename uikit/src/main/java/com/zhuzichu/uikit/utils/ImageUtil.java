package com.zhuzichu.uikit.utils;

import com.zhuzichu.library.Nice;

import java.io.InputStream;

public class ImageUtil {
    public static class ImageSize {
        public int width = 0;
        public int height = 0;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static ImageSize getThumbnailDisplaySize(float srcWidth, float srcHeight, float dstMaxWH, float dstMinWH) {
        if (srcWidth <= 0 || srcHeight <= 0) { // bounds check
            return new ImageSize((int) dstMinWH, (int) dstMinWH);
        }

        float shorter;
        float longer;
        boolean widthIsShorter;

        //store
        if (srcHeight < srcWidth) {
            shorter = srcHeight;
            longer = srcWidth;
            widthIsShorter = false;
        } else {
            shorter = srcWidth;
            longer = srcHeight;
            widthIsShorter = true;
        }

        if (shorter < dstMinWH) {
            float scale = dstMinWH / shorter;
            shorter = dstMinWH;
            if (longer * scale > dstMaxWH) {
                longer = dstMaxWH;
            } else {
                longer *= scale;
            }
        } else if (longer > dstMaxWH) {
            float scale = dstMaxWH / longer;
            longer = dstMaxWH;
            if (shorter * scale < dstMinWH) {
                shorter = dstMinWH;
            } else {
                shorter *= scale;
            }
        }

        //restore
        if (widthIsShorter) {
            srcWidth = shorter;
            srcHeight = longer;
        } else {
            srcWidth = longer;
            srcHeight = shorter;
        }

        return new ImageSize((int) srcWidth, (int) srcHeight);
    }

    public static int[] getBoundWithLength(int maxSide, Object imageObject, boolean resizeToDefault) {
        int width = -1;
        int height = -1;

        int[] bound;
        if (String.class.isInstance(imageObject)) {
            bound = BitmapDecoder.decodeBound((String) imageObject);
            width = bound[0];
            height = bound[1];
        } else if (Integer.class.isInstance(imageObject)) {
            bound = BitmapDecoder.decodeBound(Nice.getContext().getResources(), (Integer) imageObject);
            width = bound[0];
            height = bound[1];
        } else if (InputStream.class.isInstance(imageObject)) {
            bound = BitmapDecoder.decodeBound((InputStream) imageObject);
            width = bound[0];
            height = bound[1];
        }
        int defaultWidth = maxSide;
        int defaultHeight = maxSide;
        if (width <= 0 || height <= 0) {
            width = defaultWidth;
            height = defaultHeight;
        } else if (resizeToDefault) {
            if (width > height) {
                height = (int) (defaultWidth * ((float) height / (float) width));
                width = defaultWidth;
            } else {
                width = (int) (defaultHeight * ((float) width / (float) height));
                height = defaultHeight;
            }
        }
        return new int[]{width, height};
    }
}

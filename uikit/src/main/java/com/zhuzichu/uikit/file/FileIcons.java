package com.zhuzichu.uikit.file;

import com.zhuzichu.uikit.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wb.zhuzichu18 on 2018/10/29.
 */
public class FileIcons {
    private static final Map<String, Integer> smallIconMap = new HashMap<>();

    public static final String XLS = "xls";

    public static final String PPT = "ppt";

    public static final String DOC = "doc";

    public static final String XLSX = "xlsx";

    public static final String PPTX = "pptx";

    public static final String DOCX = "docx";

    public static final String PDF = "pdf";

    public static final String HTML = "html";

    public static final String HTM = "htm";

    public static final String TXT = "txt";

    public static final String RAR = "rar";

    public static final String ZIP = "zip";

    public static final String KEY = "7z";

    public static final String MP4 = "mp4";

    public static final String MP3 = "mp3";

    public static final String PNG = "png";

    public static final String GIF = "gif";

    public static final String JPG = "jpg";

    public static final String JPEG = "jpeg";

    static {
        smallIconMap.put(XLS, R.drawable.file_ic_session_excel);
        smallIconMap.put(PPT, R.drawable.file_ic_session_ppt);
        smallIconMap.put(DOC, R.drawable.file_ic_session_word);
        smallIconMap.put(XLSX, R.drawable.file_ic_session_excel);
        smallIconMap.put(PPTX, R.drawable.file_ic_session_ppt);
        smallIconMap.put(DOCX, R.drawable.file_ic_session_word);
        smallIconMap.put(PDF, R.drawable.file_ic_session_pdf);
        smallIconMap.put(HTML, R.drawable.file_ic_session_html);
        smallIconMap.put(HTM, R.drawable.file_ic_session_html);
        smallIconMap.put(TXT, R.drawable.file_ic_session_txt);
        smallIconMap.put(RAR, R.drawable.file_ic_session_rar);
        smallIconMap.put(ZIP, R.drawable.file_ic_session_zip);
        smallIconMap.put(KEY, R.drawable.file_ic_session_zip);
        smallIconMap.put(MP4, R.drawable.file_ic_session_mp4);
        smallIconMap.put(MP3, R.drawable.file_ic_session_mp3);
        smallIconMap.put(PNG, R.drawable.file_ic_session_png);
        smallIconMap.put(GIF, R.drawable.file_ic_session_gif);
        smallIconMap.put(JPG, R.drawable.file_ic_session_jpg);
        smallIconMap.put(JPEG, R.drawable.file_ic_session_jpg);
    }

    private static final Map<String, Integer> bigIconMap = new HashMap<>();

    static {
        bigIconMap.put(XLS, R.drawable.file_ic_detail_excel);
        bigIconMap.put(PPT, R.drawable.file_ic_detail_ppt);
        bigIconMap.put(DOC, R.drawable.file_ic_detail_word);
        bigIconMap.put(XLSX, R.drawable.file_ic_detail_excel);
        bigIconMap.put(PPTX, R.drawable.file_ic_detail_ppt);
        bigIconMap.put(DOCX, R.drawable.file_ic_detail_word);
        bigIconMap.put(PDF, R.drawable.file_ic_detail_pdf);
        bigIconMap.put(HTML, R.drawable.file_ic_detail_html);
        bigIconMap.put(HTM, R.drawable.file_ic_detail_html);
        bigIconMap.put(TXT, R.drawable.file_ic_detail_txt);
        bigIconMap.put(RAR, R.drawable.file_ic_detail_rar);
        bigIconMap.put(ZIP, R.drawable.file_ic_detail_zip);
        bigIconMap.put(KEY, R.drawable.file_ic_detail_zip);
        bigIconMap.put(MP4, R.drawable.file_ic_detail_mp4);
        bigIconMap.put(MP3, R.drawable.file_ic_detail_mp3);
        bigIconMap.put(PNG, R.drawable.file_ic_detail_png);
        bigIconMap.put(GIF, R.drawable.file_ic_detail_gif);
        bigIconMap.put(JPG, R.drawable.file_ic_detail_jpg);
        bigIconMap.put(JPEG, R.drawable.file_ic_detail_jpg);
    }

    public static int smallIcon(String fileName) {
        String ext = getExtensionName(fileName).toLowerCase();
        Integer resId = smallIconMap.get(ext);
        if (resId == null) {
            return R.drawable.file_ic_session_unknow;
        }

        return resId.intValue();
    }

    public static int bigIcon(String fileName) {
        String ext = getExtensionName(fileName).toLowerCase();
        Integer resId = bigIconMap.get(ext);
        if (resId == null) {
            return R.drawable.file_ic_detail_unknow;
        }

        return resId.intValue();
    }

    // 获取文件扩展名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }
}

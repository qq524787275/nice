package com.zhuzichu.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhuzichu.library.Nice;
import com.zhuzichu.library.comment.color.ColorConfig;

public class UserPreferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_COLOR_CONFIG = "key_color_config";

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserAccountAndToken(String account, String token) {
        saveUserAccount(account);
        saveUserToken(token);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static void saveColorConfig(ColorConfig colorConfig) {
        String json = Convert.toJson(colorConfig);
        saveString(KEY_COLOR_CONFIG, json);
    }

    public static ColorConfig getColorConfig() {
        String json = getString(KEY_COLOR_CONFIG);
        ColorConfig colorConfig = Convert.fromJson(json, ColorConfig.class);
        return colorConfig;
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return Nice.context.getSharedPreferences("Nice", Context.MODE_PRIVATE);
    }
}

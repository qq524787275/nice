package com.zhuzichu.uikit.event.online;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wb.zhuzichu18 on 2018/10/19.
 */
public class OnlineStateEventConfig {

    // 多端在线状态配置解析
    public static final String KEY_NET_STATE = "net_state";
    public static final String KEY_ONLINE_STATE = "online_state";  //0 在线  1忙碌  2离开";


    public static String buildConfig(int netState, int onlineState) {
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_NET_STATE, netState);
            json.put(KEY_ONLINE_STATE, onlineState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static OnlineState parseConfig(String config, int clientType) {
        if (TextUtils.isEmpty(config)) {
            return null;
        }
        OnlineState state = null;
        try {
            JSONObject json = new JSONObject(config);
            int netState = json.getInt(KEY_NET_STATE);
            int onlineState = json.getInt(KEY_ONLINE_STATE);
            state = new OnlineState(clientType, netState, onlineState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }

}

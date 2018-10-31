package com.zhuzichu.uikit.event.online;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.event.EventSubscribeService;
import com.netease.nimlib.sdk.event.model.Event;
import com.netease.nimlib.sdk.event.model.EventSubscribeRequest;
import com.netease.nimlib.sdk.event.model.NimOnlineStateEvent;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.observer.action.ActionOnlineStateChange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OnlineStateEventManager {
    private static final String TAG = "OnlineStateEventManager";
    public static final long SUBSCRIBE_EXPIRY = 60 * 60 * 24;
    private static final String NET_TYPE_2G = "2G";
    private static final String NET_TYPE_3G = "3G";
    private static final String NET_TYPE_4G = "4G";
    private static final String NET_TYPE_WIFI = "WiFi";
    private static final String UNKNOWN = "未知";

    public static void subscribeOnlineStateEvent(List<String> accounts) {
        OnlineStateEventCache.addSubsAccounts(accounts);
        NIMClient.getService(EventSubscribeService.class).subscribeEvent(getOnlineStateEvent(accounts)).setCallback(new RequestCallbackWrapper<List<String>>() {
            @Override
            public void onResult(int code, List<String> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    if (result != null) {
                        //部分订阅失败的账号
                        OnlineStateEventCache.removeSubsAccounts(result);
                        Log.i(TAG, "onResult:---------订阅失败的账号有--------------: " + result.size());
                    }
                } else {
                    OnlineStateEventCache.removeSubsAccounts(accounts);
                }
            }
        });
    }


    private static EventSubscribeRequest getOnlineStateEvent(List<String> accounts) {
        EventSubscribeRequest eventSubscribeRequest = new EventSubscribeRequest();
        eventSubscribeRequest.setEventType(NimOnlineStateEvent.EVENT_TYPE);
        eventSubscribeRequest.setPublishers(accounts);
        eventSubscribeRequest.setExpiry(SUBSCRIBE_EXPIRY);
        eventSubscribeRequest.setSyncCurrentValue(true);
        return eventSubscribeRequest;
    }

    public static void receivedOnlineStateEvents(List<Event> events) {
        Set<String> changed = new HashSet<String>();
        for (Event event : events) {
            if (NimOnlineStateEvent.isOnlineStateEvent(event)) {
                // 获取优先级最高的在线客户端的状态
                OnlineState state = getDisplayOnlineState(event);
                changed.add(event.getPublisherAccount());
                // 将事件缓存
                OnlineStateEventCache.cacheOnlineState(event.getPublisherAccount(), state);
            }
        }
        RxBus.getIntance().post(new ActionOnlineStateChange(changed));
    }

    private static OnlineState getDisplayOnlineState(Event event) {
        // 获取多端的在线信息
        Map<Integer, OnlineState> multiClientStates = getOnlineStateFromEvent(event);
        // 取优先级最高的展示
        if (multiClientStates == null || multiClientStates.isEmpty()) {
            return null;
        }
        OnlineState result;
        if (isOnline(result = multiClientStates.get(ClientType.Windows))) {
            return result;
        } else if (isOnline(result = multiClientStates.get(ClientType.MAC))) {
            return result;
        } else if (isOnline(result = multiClientStates.get(ClientType.iOS))) {
            return result;
        } else if (isOnline(result = multiClientStates.get(ClientType.Android))) {
            return result;
        } else if (isOnline(result = multiClientStates.get(ClientType.Web))) {
            return result;
        }
        return null;
    }

    private static boolean isOnline(OnlineState state) {
        return state != null && state.getOnlineState() != (OnlineStateCode.Offline);
    }

    /**
     * 从事件中获取该账户的多端在线状态信息
     *
     * @param event
     * @return
     */
    private static Map<Integer, OnlineState> getOnlineStateFromEvent(Event event) {
        if (!NimOnlineStateEvent.isOnlineStateEvent(event)) {
            return null;
        }
        // 解析
        List<Integer> clients = NimOnlineStateEvent.getOnlineClients(event);
        if (clients == null) {
            return null;
        }
        Map<Integer, OnlineState> onlineStates = new HashMap<>();
        for (int i = 0; i < clients.size(); i++) {
            int clientType = clients.get(i);
            OnlineState state = OnlineStateEventConfig.parseConfig(event.getConfigByClient(clientType), clientType);
            if (state == null) {
                state = new OnlineState(clientType, NetStateCode.Unkown, OnlineStateCode.Online);
            }
            onlineStates.put(clientType, state);
        }

        return onlineStates;
    }

    /**
     * 在线状态显示文案
     *
     * @param context
     * @param state
     * @param simple
     * @return
     */
    public static String getOnlineClientContent(Context context, OnlineState state, boolean simple) {
        // 离线
        if (!isOnline(state)) {
            return context.getString(R.string.off_line);
        }
        // 忙碌
        if (state.getOnlineState() == OnlineStateCode.Busy) {
            return context.getString(R.string.on_line_busy);
        }
        int type = state.getOnlineClient();
        String result = null;
        switch (type) {
            case ClientType.Windows:
                result = context.getString(R.string.on_line_pc);
                break;
            case ClientType.MAC:
                result = context.getString(R.string.on_line_mac);
                break;
            case ClientType.Web:
                result = context.getString(R.string.on_line_web);
                break;
            case ClientType.Android:
                result = getMobileOnlineClientString(context, state, false, simple);
                break;
            case ClientType.iOS:
                result = getMobileOnlineClientString(context, state, true, simple);
                break;
            default:
                break;
        }
        return result;
    }

    private static boolean validNetType(OnlineState state) {
        if (state == null) {
            return false;
        }
        NetStateCode netState = state.getNetState();
        return netState != null && netState != NetStateCode.Unkown;
    }

    private static String getMobileOnlineClientString(Context context, OnlineState state, boolean ios, boolean simple) {
        String result;
        String client = ios ? context.getString(R.string.client_ios) : context.getString(R.string.client_aos);
        if (!validNetType(state)) {
            result = client + context.getString(R.string.on_line);
        } else {
            if (simple) {
                // 简单展示
                result = getDisplayNetState(state.getNetState()) + context.getString(R.string.on_line);
            } else {
                // 详细展示
                result = client + " - " + getDisplayNetState(state.getNetState()) + context.getString(R.string.on_line);
            }
        }
        return result;
    }

    private static String getDisplayNetState(NetStateCode netStateCode) {
        if (netStateCode == null || netStateCode == NetStateCode.Unkown) {
            return UNKNOWN;
        }
        if (netStateCode == NetStateCode._2G) {
            return NET_TYPE_2G;
        } else if (netStateCode == NetStateCode._3G) {
            return NET_TYPE_3G;
        } else if (netStateCode == NetStateCode._4G) {
            return NET_TYPE_4G;
        } else {
            return NET_TYPE_WIFI;
        }
    }

    public static String getSimpleDisplay(String account) {
        String content = getDisplayContent(account, true);
        if (!TextUtils.isEmpty(content)) {
            content = "[" + content + "]";
        }
        return content;
    }

    private static String getDisplayContent(String account, boolean simple) {
        if (account == null || account.equals(Nice.getAccount())) {
            return "";
        }
        OnlineState onlineState = OnlineStateEventCache.getOnlineState(account);
        return getOnlineClientContent(Nice.getContext(), onlineState, simple);
    }
}

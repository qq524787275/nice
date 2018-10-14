package com.zhuzichu.uikit.utils;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

public class UserInfoUtils {
    /**
     * @param contactId
     * @return
     */
    public static Optional<NimUserInfo> getUserInfo(String contactId) {
        return Optional.fromNullable(NIMClient.getService(UserService.class).getUserInfo(contactId));
    }

    /**
     * @param contactId
     * @return
     */
    public static Optional<String> getUserName(String contactId) {
        Optional<NimUserInfo> userInfo = getUserInfo(contactId);
        if (userInfo.isPresent()) {
            return Optional.fromNullable(userInfo.get().getName());
        }
        return Optional.fromNullable(null);
    }
}

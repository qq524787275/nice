package com.zhuzichu.uikit.utils;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.Nice;

public class TeamUtils {
    /**
     * @param contactId
     * @return
     */
    public static Optional<Team> getTeam(String contactId) {
        return Optional.fromNullable(NIMClient.getService(TeamService.class).queryTeamBlock(contactId));
    }

    /**
     * @param contactId
     * @return
     */
    public static Optional<String> getTeamName(String contactId) {
        Optional<Team> team = getTeam(contactId);
        if (team.isPresent()) {
            return Optional.fromNullable(team.get().getName());
        }
        return Optional.fromNullable(null);
    }

    public static String getTeamMemberDisplayNameYou(String tid, String account) {
        if (account.equals(Nice.getAccount())) {
            return "你";
        }
        return getDisplayNameWithoutMe(tid, account);
    }


    /**
     * 获取显示名称。用户本人也显示昵称
     * 备注>群昵称>昵称
     */
    private static String getDisplayNameWithoutMe(String tid, String account) {
        Friend friend = NIMClient.getService(FriendService.class).getFriendByAccount(account);
        if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
            return friend.getAlias();
        }

        String memberNick = getTeamNick(tid, account);
        if (!TextUtils.isEmpty(memberNick)) {
            return memberNick;
        }
        Optional<String> userName = UserInfoUtils.getUserName(account);
        if (!userName.isPresent())
            return account;
        return userName.get();
    }


    public static String getTeamNick(String tid, String account) {
        Optional<Team> team = getTeam(account);
        if (team.isPresent() && team.get().getType() == TeamTypeEnum.Advanced) {
            TeamMember member = NIMClient.getService(TeamService.class).queryTeamMemberBlock(tid, account);
            if (member != null && !TextUtils.isEmpty(member.getTeamNick())) {
                return member.getTeamNick();
            }
        }
        return null;
    }
}

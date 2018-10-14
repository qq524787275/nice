package com.zhuzichu.uikit.utils;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

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
}

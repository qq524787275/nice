package com.zhuzichu.uikit.utils;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamAllMuteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import java.util.List;
import java.util.Map;

/**
 * Created by wb.zhuzichu18 on 2018/10/17.
 */
public class TeamNotificationUtils {

    public static String getDisplayText(IMMessage data) {
        NotificationAttachment attachment = (NotificationAttachment) data.getAttachment();
        String fromAccount = data.getFromAccount();
        String tid = data.getSessionId();
        String text;
        switch (attachment.getType()) {
            case InviteMember:
                text = buildInviteMemberNotification(((MemberChangeAttachment) attachment), fromAccount, tid);
                break;
            case KickMember:
                text = buildKickMemberNotification(((MemberChangeAttachment) attachment), tid);
                break;
            case LeaveTeam:
                text = buildLeaveTeamNotification(fromAccount, tid);
                break;
            case DismissTeam:
                text = buildDismissTeamNotification(fromAccount, tid);
                break;
            case UpdateTeam:
                text = buildUpdateTeamNotification(tid, fromAccount, (UpdateTeamAttachment) attachment);
                break;
            case PassTeamApply:
                text = buildManagerPassTeamApplyNotification((MemberChangeAttachment) attachment, tid);
                break;
            case TransferOwner:
                text = buildTransferOwnerNotification(fromAccount, (MemberChangeAttachment) attachment, tid);
                break;
            case AddTeamManager:
                text = buildAddTeamManagerNotification((MemberChangeAttachment) attachment, tid);
                break;
            case RemoveTeamManager:
                text = buildRemoveTeamManagerNotification((MemberChangeAttachment) attachment, tid);
                break;
            case AcceptInvite:
                text = buildAcceptInviteNotification(fromAccount, (MemberChangeAttachment) attachment, tid);
                break;
            case MuteTeamMember:
                text = buildMuteTeamNotification((MuteMemberAttachment) attachment, tid);
                break;
            default:
                text = getTeamMemberDisplayName(fromAccount, tid) + ": unknown message";
                break;
        }
        return text;
    }


    private static String getTeamMemberDisplayName(String account, String tid) {
        return TeamUtils.getTeamMemberDisplayNameYou(tid, account);
    }

    private static String buildMemberListString(List<String> members, String fromAccount, String tid) {
        StringBuilder sb = new StringBuilder();
        for (String account : members) {
            if (!TextUtils.isEmpty(fromAccount) && fromAccount.equals(account)) {
                continue;
            }
            sb.append(getTeamMemberDisplayName(account, tid));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private static String buildInviteMemberNotification(MemberChangeAttachment a, String fromAccount, String tid) {
        StringBuilder sb = new StringBuilder();
        String selfName = getTeamMemberDisplayName(fromAccount, tid);

        sb.append(selfName);
        sb.append("邀请 ");
        sb.append(buildMemberListString(a.getTargets(), fromAccount, tid));
        Optional<Team> team = TeamUtils.getTeam(tid);
        if (!team.isPresent() || team.get().getType() == TeamTypeEnum.Advanced) {
            sb.append(" 加入群");
        } else {
            sb.append(" 加入讨论组");
        }

        return sb.toString();
    }

    private static String buildKickMemberNotification(MemberChangeAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildMemberListString(a.getTargets(), null, tid));
        Optional<Team> team = TeamUtils.getTeam(tid);
        if (!team.isPresent() || team.get().getType() == TeamTypeEnum.Advanced) {
            sb.append(" 已被移出群");
        } else {
            sb.append(" 已被移出讨论组");
        }

        return sb.toString();
    }

    private static String buildLeaveTeamNotification(String fromAccount, String tid) {
        String tip;
        Optional<Team> team = TeamUtils.getTeam(tid);
        if (!team.isPresent() || team.get().getType() == TeamTypeEnum.Advanced) {
            tip = " 离开了群";
        } else {
            tip = " 离开了讨论组";
        }
        return getTeamMemberDisplayName(fromAccount, tid) + tip;
    }

    private static String buildDismissTeamNotification(String fromAccount, String tid) {
        return getTeamMemberDisplayName(fromAccount, tid) + " 解散了群";
    }

    private static String buildUpdateTeamNotification(String tid, String account, UpdateTeamAttachment a) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TeamFieldEnum, Object> field : a.getUpdatedFields().entrySet()) {
            if (field.getKey() == TeamFieldEnum.Name) {
                sb.append("名称被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Introduce) {
                sb.append("群介绍被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Announcement) {
                sb.append(TeamUtils.getTeamMemberDisplayNameYou(tid, account) + " 修改了群公告");
            } else if (field.getKey() == TeamFieldEnum.VerifyType) {
                VerifyTypeEnum type = (VerifyTypeEnum) field.getValue();
                String authen = "群身份验证权限更新为";
                if (type == VerifyTypeEnum.Free) {
                    sb.append(authen + "允许任何人加入");
                } else if (type == VerifyTypeEnum.Apply) {
                    sb.append(authen + "需要身份验证");
                } else {
                    sb.append(authen + "不允许任何人申请加入");
                }
            } else if (field.getKey() == TeamFieldEnum.Extension) {
                sb.append("群扩展字段被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Ext_Server) {
                sb.append("群扩展字段(服务器)被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.ICON) {
                sb.append("群头像已更新");
            } else if (field.getKey() == TeamFieldEnum.InviteMode) {
                sb.append("群邀请他人权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamUpdateMode) {
                sb.append("群资料修改权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.BeInviteMode) {
                sb.append("群被邀请人身份验证权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamExtensionUpdateMode) {
                sb.append("群扩展字段修改权限被更新为 " + field.getValue());
            } else if (field.getKey() == TeamFieldEnum.AllMute) {
                TeamAllMuteModeEnum teamAllMuteModeEnum = (TeamAllMuteModeEnum) field.getValue();
                if (teamAllMuteModeEnum == TeamAllMuteModeEnum.Cancel) {
                    sb.append("取消群全员禁言");
                } else {
                    sb.append("群全员禁言");
                }
            } else {
                sb.append("群" + field.getKey() + "被更新为 " + field.getValue());
            }
            sb.append("\r\n");
        }
        if (sb.length() < 2) {
            return "未知通知";
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    private static String buildManagerPassTeamApplyNotification(MemberChangeAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();
        sb.append("管理员通过用户 ");
        sb.append(buildMemberListString(a.getTargets(), null, tid));
        sb.append(" 的入群申请");

        return sb.toString();
    }

    private static String buildTransferOwnerNotification(String from, MemberChangeAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTeamMemberDisplayName(from, tid));
        sb.append(" 将群转移给 ");
        sb.append(buildMemberListString(a.getTargets(), null, tid));

        return sb.toString();
    }

    private static String buildAddTeamManagerNotification(MemberChangeAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null, tid));
        sb.append(" 被任命为管理员");

        return sb.toString();
    }

    private static String buildRemoveTeamManagerNotification(MemberChangeAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null, tid));
        sb.append(" 被撤销管理员身份");

        return sb.toString();
    }

    private static String buildAcceptInviteNotification(String from, MemberChangeAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTeamMemberDisplayName(from, tid));
        sb.append(" 接受了 ").append(buildMemberListString(a.getTargets(), null, tid)).append(" 的入群邀请");

        return sb.toString();
    }

    private static String buildMuteTeamNotification(MuteMemberAttachment a, String tid) {
        StringBuilder sb = new StringBuilder();

        sb.append(buildMemberListString(a.getTargets(), null, tid));
        sb.append("被管理员");
        sb.append(a.isMute() ? "禁言" : "解除禁言");

        return sb.toString();
    }
}
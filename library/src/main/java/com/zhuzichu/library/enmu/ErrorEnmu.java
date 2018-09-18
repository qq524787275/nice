package com.zhuzichu.library.enmu;

/**
 * Created by zzc on 2018-07-23.
 */


public enum ErrorEnmu {
    Code_200(200, "操作成功"),
    Code_201(201, "客户端版本不对，需升级sdk"),
    Code_301(301, "被封禁"),
    Code_302(302, "用户名或密码错误"),
    Code_315(315, "IP限制"),
    Code_403(403, "非法操作或没有权限"),
    Code_404(404, "对象不存在"),
    Code_405(405, "参数长度过长"),
    Code_406(406, "对象只读"),
    Code_408(408, "客户端请求超时"),
    Code_413(413, "验证失败(短信服务)"),
    Code_414(414, "参数错误"),
    Code_415(415, "客户端网络问题"),
    Code_416(416, "频率控制"),
    Code_417(417, "重复操作"),
    Code_418(418, "通道不可用(短信服务)"),
    Code_419(419, "数量超过上限"),
    Code_422(422, "账号被禁用"),
    Code_431(431, "HTTP重复请求"),
    Code_500(500, "服务器内部错误"),
    Code_503(503, "服务器繁忙"),
    Code_508(508, "消息撤回时间超限"),
    Code_509(509, "无效协议"),
    Code_514(514, "服务不可用"),
    Code_998(998, "解包错误"),
    Code_999(999, "打包错误"),
    Code_801(801, "群人数达到上限"),
    Code_802(802, "没有权限"),
    Code_803(803, "群不存在"),
    Code_804(804, "用户不在群"),
    Code_805(805, "群类型不匹配"),
    Code_806(806, "创建群数量达到限制"),
    Code_807(807, "群成员状态错误"),
    Code_808(808, "申请成功"),
    Code_809(809, "已经在群内"),
    Code_810(810, "邀请成功"),
    Code_10086(10086, "未找到错误码");

    private int code;
    private String msg;

    ErrorEnmu(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorEnmu getErrorEnmu(int code) {
        for (ErrorEnmu nimCodeEnmu : values()) {
            if (code == nimCodeEnmu.code)
                return nimCodeEnmu;
        }
        return Code_10086;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package edu.guet.studentworkmanagementsystem.exception;

import lombok.Getter;

@Getter
public enum ServiceExceptionEnum {
    // -1xxx: 认证与权限异常
    AUTHENTICATION_FAILURE(-1000, "认证失败"),
    INSUFFICIENT_PERMISSIONS(-1001, "权限不足"),
    ACCOUNT_NOT_FOUND(-1002, "用户未找到"),
    ID_NO_PASSWORD_WRONG(-1003, "工号/学号或密码错误"),
    ACCOUNT_EXISTED(-1004, "用户已经存在"),
    TOKEN_ERROR(-1005, "Token异常"),
    UN_LOGIN(-1006, "还未登录或Token过期"),
    // -2xxx: 用户操作不当造成异常
    METHOD_ARGUMENT_NOT_VALID(-2000, ""),
    KEY_ARGUMENT_NOT_INPUT(-2001, "关键信息未输入"),
    SELECT_NOT_IN(-2002, "该选项不存在"),
    METHOD_NOT_SUPPORT(-2003, "方法不支持"),
    TOO_MANY_REQUEST(-2004, "请求过于频繁"),
    // -3xxx: 资源访问错误
    OPERATE_ERROR(-3000, "操作失败"),
    NOT_FOUND(-3001, "未找到相关记录"),
    NOT_RESOURCE(-3002, "无此资源: "),
    GET_RESOURCE_TIMEOUT(-3003, "获取资源超时"),
    GET_RESOURCE_INTERRUPTED(-3004, "获取资源被终止"),
    // -5xxx: 来源于服务或系统错误
    UNKNOWN_ERROR(-5000, "未知异常"),
    NULL_POINTER(-5001, "空指针异常");
    private final int code;
    private final String msg;
    ServiceExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

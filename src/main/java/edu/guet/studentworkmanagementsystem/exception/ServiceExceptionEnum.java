package edu.guet.studentworkmanagementsystem.exception;

import lombok.Getter;

@Getter
public enum ServiceExceptionEnum {
    AUTHENTICATION_FAILURE(-200, "认证失败"),
    INSUFFICIENT_PERMISSIONS(-201, "权限不足"),
    ACCOUNT_NOT_FOUND(-202, "用户不存在"),
    EMAIL_NO_PASSWORD_WRONG(-203, "工号/学号或密码错误"),
    KEY_EXISTED(-204, "主键重复"),
    METHOD_ARGUMENT_NOT_VALID(-205, ""),
    OPERATE_ERROR(-206, "操作失败"),
    KEY_ARGUMENT_NOT_INPUT(-207, "关键信息未输入"),
    TOKEN_ERROR(-208, "Token异常"),
    NOT_FOUND(-209, "未找到目标"),
    UN_LOGIN(-210, "还未登录或Token过期"),
    JSON_ERROR(-211, "JSON解析出错"),
    SELECT_NOT_IN(-212, "该选项不存在"),
    METHOD_NOT_SUPPORT(-2000, "方法不支持"),
    UNKNOWN_ERROR(-2001, "未知异常"),
    NOT_RESOURCE(-2002, "无此资源: "),
    NULL_POINTER(-2003, "出现空指针异常"),
    TOO_MANY_REQUEST(-2004, "请求过于频繁");
    private final int code;
    private final String msg;
    ServiceExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

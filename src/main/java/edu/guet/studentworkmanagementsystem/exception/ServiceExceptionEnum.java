package edu.guet.studentworkmanagementsystem.exception;

import lombok.Getter;

@Getter
public enum ServiceExceptionEnum {
    // -1xxx: 认证与权限异常
    AUTHENTICATION_FAILURE(-1000, "认证失败"),
    INSUFFICIENT_PERMISSIONS(-1001, "权限不足"),
    ACCOUNT_NOT_FOUND(-1002, "用户未找到"),
    ID_NO_PASSWORD_WRONG(-1003, "工号/学号或密码错误"),
    TOKEN_ERROR(-1004, "Token异常"),
    UN_LOGIN(-1005, "还未登录或Token过期"),
    ACCOUNT_DISABLED(-1006, "用户已被禁用, 联系管理员解除"),
    // -2xxx: 用户操作不当造成异常
    METHOD_ARGUMENT_NOT_VALID(-2000, ""),
    KEY_ARGUMENT_NOT_INPUT(-2001, "关键信息未输入"),
    SELECT_NOT_IN(-2002, "该选项不存在"),
    METHOD_NOT_SUPPORT(-2003, "方法不支持"),
    TOO_MANY_REQUEST(-2004, "请求过于频繁"),
    TYPE_ERROR(-2005, "参数存在错误"),
    STUDENT_ID_OR_ID_NUMBER_REPEAT(-2006, "所填写的数据中存在学号/身份证重复"),
    STUDENT_ID_OR_ID_NUMBER_EXISTED(-2007, "该学号/身份证号已经被使用"),
    ACCOUNT_EXISTED(-2009, "该工号(学号)/邮箱已经被使用"),
    // -3xxx: 资源访问错误
    OPERATE_ERROR(-3000, "操作失败"),
    NOT_FOUND(-3001, "未找到相关记录"),
    NOT_RESOURCE(-3002, "无此资源: "),
    GET_RESOURCE_TIMEOUT(-3003, "获取资源超时"),
    GET_RESOURCE_INTERRUPTED(-3004, "获取资源被终止"),
    // -5xxx: 来源于服务或系统错误
    UNKNOWN_ERROR(-5000, "未知异常"),
    NULL_POINTER(-5001, "空指针异常"),
    EMAIL_SEND_FAILURE(-5002, "邮件发送失败"),
    ;
    private final int code;
    private final String msg;
    ServiceExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

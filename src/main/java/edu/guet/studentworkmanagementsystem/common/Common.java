package edu.guet.studentworkmanagementsystem.common;

import lombok.Getter;

@Getter
public enum Common {
    LOGIN_UID("uid:"),
    UPDATE_PASSWORD("code_by:"),
    ANONYMOUS_USER("anonymousUser"),
    STUDENT("学生"),
    STATUS_PERMISSION("student:status"),
    STATUS_PERMISSION_ALL("student:status:all"),
    HEADER_TEACHER("班主任"),
    SOLO("单人"),
    TEAM("团队"),
    PASS("已通过"),
    REJECT("已拒绝"),
    WAITING("审核中"),
    ;
    private final String value;
    Common(String str) {
        this.value = str;
    }
}

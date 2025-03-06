package edu.guet.studentworkmanagementsystem.common;

import lombok.Getter;

@Getter
public enum Common {
    LOGIN_UID("uid:"),
    UPDATE_PASSWORD("code_by:"),
    ANONYMOUS_USER("anonymousUser"),
    STUDENT("学生"),
    STUDENT_ID("studentId"),
    ;
    private final String value;
    Common(String str) {
        this.value = str;
    }
}

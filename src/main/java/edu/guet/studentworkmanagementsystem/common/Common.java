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
    NORMAL_COUNT("在籍"),
    SUSPEND_COUNT("休学"),
    MILITARY_COUNT("应征入伍保留学籍"),
    RETURN_COUNT("复学"),
    TRANSFER_IN_COUNT("转入"),
    TRANSFER_OUT_COUNT("转出"),
    DROP_OF_ENROLLMENT_COUNT("放弃入学资格"),
    RETAIN_ENROLLMENT_COUNT("保留入学资格"),
    GRADUATION_COUNT("结业"),
    GRAD_COUNT("毕业"),
    DROPPED_COUNT("退学"),
    RECHRISTEN_COUNT("改名"),
    DEATH_COUNT("死亡"),
    MALE_COUNT("男"),
    FEMALE_COUNT("女"),
    MASS_COUNT("群众"),
    LEAGUE_COUNT("共青团员"),
    PARTY_COUNT("中共党员"),
    PREPARE_COUNT("预备党员"),
    DISABILITY_COUNT("残疾学生"),
    MINORITY_COUNT("少数民族"),
    HAN_RIGHT_LIKE("汉%"),
    HAN_CHINESE_RIGHT_LIKE("汉族%"),
    ;
    private final String value;
    Common(String str) {
        this.value = str;
    }
}

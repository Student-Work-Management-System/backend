package edu.guet.studentworkmanagementsystem.common;

import java.util.HashMap;

public class Majors {
    public static final HashMap<String, String> majorName2MajorId = new HashMap<>(){{
        put("计算机科学与技术", "1");
        put("软件工程", "2");
        put("信息安全", "3");
        put("物联网工程", "4");
        put("智能科学与技术", "5");
        put("网络空间安全", "6");
    }};
}

package edu.guet.studentworkmanagementsystem;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
        String key = "$2a$10$EOWyTIhOPiIDaO40I7znfuTmttRRCe3rEzDM9aS9/pWcLe8ZT5fSO";
        String uuid = UUID.randomUUID().toString();
        String token = JWT.create()
                .setPayload("uid", "uid:1")
                .setJWTId(uuid)
                .setSubject("StudentWorkManagementSystem")
                .setIssuer("BridgeFishDev")
                .setIssuedAt(DateUtil.date())
                .setExpiresAt(Date.from(Instant.now().plusSeconds(604800)))
                .setKey(key.getBytes())
                .sign();
        System.out.println(key);
        System.out.println(token);
    }
}

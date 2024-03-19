package edu.guet.studentworkmanagementsystem;

import cn.hutool.jwt.JWT;
import edu.guet.studentworkmanagementsystem.securiy.SecurityUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private UserDetailsService userDetailsService;
    @Test
    void contextLoads() {
        byte[] key = "edu.guet".getBytes();
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername("admin");
        String token = JWT.create()
                .setPayload("redisKey", securityUser.getUser().getUid())
                .setKey(key)
                .sign();
        boolean verify = JWT.of(token).setKey(key).verify();
        if (verify)
            System.out.println("True");
        else
            System.out.println("False");
    }
}

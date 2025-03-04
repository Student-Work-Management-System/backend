package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.UsernameTrie;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUser;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentBasicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private StudentBasicMapper studentBasicMapper;
    @Test
    void contextLoads() {
    }
    void testUsernameTrie() {
        UsernameTrie root = new UsernameTrie();
        List<RegisterUser> registerUsers = createRegisterUsers();
        root.buildTrie(registerUsers);
        if (root.search("1014")) {
            UsernameTrie.TrieNode node = root.getNode("1014");
            System.out.println(node.getRoles());
        }
    }

    private List<RegisterUser> createRegisterUsers() {
        List<RegisterUser> registerUsers = new ArrayList<>();
        RegisterUser registerUser1 = createRegisterUser("1001", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser2 = createRegisterUser("1002", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser3 = createRegisterUser("1003", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser4 = createRegisterUser("1004", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser5 = createRegisterUser("1004", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser6 = createRegisterUser("1005", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser7 = createRegisterUser("1006", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser8 = createRegisterUser("1007", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser9 = createRegisterUser("1008", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser10 = createRegisterUser("1009", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser11 = createRegisterUser("1010", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser12 = createRegisterUser("1011", "123456", "111", "111@qqq.com", "12345");
        RegisterUser registerUser13 = createRegisterUser("1012", "123456", "111", "111@qqq.com", "12345");

        registerUsers.add(registerUser1);
        registerUsers.add(registerUser2);
        registerUsers.add(registerUser3);
        registerUsers.add(registerUser4);
        registerUsers.add(registerUser5);
        registerUsers.add(registerUser6);
        registerUsers.add(registerUser7);
        registerUsers.add(registerUser8);
        registerUsers.add(registerUser9);
        registerUsers.add(registerUser10);
        registerUsers.add(registerUser11);
        registerUsers.add(registerUser12);
        registerUsers.add(registerUser13);
        return registerUsers;
    }

    private RegisterUser createRegisterUser(String username, String password, String realName, String email, String phone) {
        return RegisterUser.builder()
                .username(username)
                .password(password)
                .realName(realName)
                .email(email)
                .phone(phone)
                .roles(Set.of("5"))
                .build();
    }
}

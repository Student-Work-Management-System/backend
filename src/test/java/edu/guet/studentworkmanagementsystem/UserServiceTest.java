package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.UsernameTrie;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUser;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testAddUser() {
        RegisterUser registerUser1 = createRegisterUser("2100301832", "2100301832", "2100301832", "2100301832@guet.edu.com", "2100301832");
        BaseResponse<Object> response = userService.addUser(registerUser1);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void testAddUsers() {
        List<RegisterUser> registerUsers = createRegisterUsers();
        BaseResponse<Object> response = userService.addUsers(new ValidateList<>(registerUsers));
        System.out.println(response.getMessage() + ": " + response.getData());
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
        RegisterUser registerUser2 = createRegisterUser("1002", "123456", "112", "112@qqq.com", "12346");
        RegisterUser registerUser3 = createRegisterUser("1003", "123456", "113", "113@qqq.com", "12347");
        RegisterUser registerUser4 = createRegisterUser("1004", "123456", "114", "114@qqq.com", "12348");
        RegisterUser registerUser5 = createRegisterUser("1013", "123456", "115", "115@qqq.com", "12349");
        RegisterUser registerUser6 = createRegisterUser("1005", "123456", "116", "116@qqq.com", "12350");
        RegisterUser registerUser7 = createRegisterUser("1006", "123456", "117", "117@qqq.com", "12351");
        RegisterUser registerUser8 = createRegisterUser("1007", "123456", "118", "118@qqq.com", "12352");
        RegisterUser registerUser9 = createRegisterUser("1008", "123456", "119", "119@qqq.com", "12353");
        RegisterUser registerUser10 = createRegisterUser("1009", "123456", "120", "120@qqq.com", "12354");
        RegisterUser registerUser11 = createRegisterUser("1010", "123456", "121", "121@qqq.com", "12355");
        RegisterUser registerUser12 = createRegisterUser("1011", "123456", "122", "122@qqq.com", "12356");
        RegisterUser registerUser13 = createRegisterUser("1012", "123456", "123", "123@qqq.com", "12357");

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
    public User createUser(RegisterUser registerUser) {
        return User.builder()
                .username(registerUser.getUsername())
                .realName(registerUser.getRealName())
                .email(registerUser.getEmail())
                .phone(registerUser.getPhone())
                .password(registerUser.getPassword())
                .createdAt(LocalDate.now())
                .enabled(true)
                .build();
    }

    @Test
    void getPassword() {
        String encode = passwordEncoder.encode("admin");
        System.out.println(encode);
    }
}

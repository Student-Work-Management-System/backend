package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.UsernameTrie;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserRequest;
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
        RegisterUserRequest registerUserRequest1 = createRegisterUser("2100301832", "2100301832", "2100301832", "2100301832@guet.edu.com", "2100301832");
        BaseResponse<Object> response = userService.addUser(registerUserRequest1);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void testAddUsers() {
        List<RegisterUserRequest> registerUserRequests = createRegisterUsers();
        BaseResponse<Object> response = userService.addUsers(new ValidateList<>(registerUserRequests));
        System.out.println(response.getMessage() + ": " + response.getData());
    }

    void testUsernameTrie() {
        UsernameTrie root = new UsernameTrie();
        List<RegisterUserRequest> registerUserRequests = createRegisterUsers();
        root.buildTrie(registerUserRequests);
        if (root.search("1014")) {
            UsernameTrie.TrieNode node = root.getNode("1014");
            System.out.println(node.getRoles());
        }
    }
    private List<RegisterUserRequest> createRegisterUsers() {
        List<RegisterUserRequest> registerUserRequests = new ArrayList<>();
        RegisterUserRequest registerUserRequest1 = createRegisterUser("1001", "123456", "111", "111@qqq.com", "12345");
        RegisterUserRequest registerUserRequest2 = createRegisterUser("1002", "123456", "112", "112@qqq.com", "12346");
        RegisterUserRequest registerUserRequest3 = createRegisterUser("1003", "123456", "113", "113@qqq.com", "12347");
        RegisterUserRequest registerUserRequest4 = createRegisterUser("1004", "123456", "114", "114@qqq.com", "12348");
        RegisterUserRequest registerUserRequest5 = createRegisterUser("1013", "123456", "115", "115@qqq.com", "12349");
        RegisterUserRequest registerUserRequest6 = createRegisterUser("1005", "123456", "116", "116@qqq.com", "12350");
        RegisterUserRequest registerUserRequest7 = createRegisterUser("1006", "123456", "117", "117@qqq.com", "12351");
        RegisterUserRequest registerUserRequest8 = createRegisterUser("1007", "123456", "118", "118@qqq.com", "12352");
        RegisterUserRequest registerUserRequest9 = createRegisterUser("1008", "123456", "119", "119@qqq.com", "12353");
        RegisterUserRequest registerUserRequest10 = createRegisterUser("1009", "123456", "120", "120@qqq.com", "12354");
        RegisterUserRequest registerUserRequest11 = createRegisterUser("1010", "123456", "121", "121@qqq.com", "12355");
        RegisterUserRequest registerUserRequest12 = createRegisterUser("1011", "123456", "122", "122@qqq.com", "12356");
        RegisterUserRequest registerUserRequest13 = createRegisterUser("1012", "123456", "123", "123@qqq.com", "12357");

        registerUserRequests.add(registerUserRequest1);
        registerUserRequests.add(registerUserRequest2);
        registerUserRequests.add(registerUserRequest3);
        registerUserRequests.add(registerUserRequest4);
        registerUserRequests.add(registerUserRequest5);
        registerUserRequests.add(registerUserRequest6);
        registerUserRequests.add(registerUserRequest7);
        registerUserRequests.add(registerUserRequest8);
        registerUserRequests.add(registerUserRequest9);
        registerUserRequests.add(registerUserRequest10);
        registerUserRequests.add(registerUserRequest11);
        registerUserRequests.add(registerUserRequest12);
        registerUserRequests.add(registerUserRequest13);
        return registerUserRequests;
    }
    private RegisterUserRequest createRegisterUser(String username, String password, String realName, String email, String phone) {
        return RegisterUserRequest.builder()
                .username(username)
                .password(password)
                .realName(realName)
                .email(email)
                .phone(phone)
                .roles(Set.of("5"))
                .build();
    }
    public User createUser(RegisterUserRequest registerUserRequest) {
        return User.builder()
                .username(registerUserRequest.getUsername())
                .realName(registerUserRequest.getRealName())
                .email(registerUserRequest.getEmail())
                .phone(registerUserRequest.getPhone())
                .password(registerUserRequest.getPassword())
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

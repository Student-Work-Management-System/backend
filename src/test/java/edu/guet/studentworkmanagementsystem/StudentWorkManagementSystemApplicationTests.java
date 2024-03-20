package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.RolePermissionVO;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private UserService userService;
    @Test
    void contextLoads() {
        BaseResponse<List<RolePermissionVO>> roles = userService.getAllRole();
        System.out.println(roles);
    }
}

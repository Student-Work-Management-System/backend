package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private LeaveService leaveService;
    @Test
    void contextLoads() {
    }
}

package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private StudentService service;
    @Test
    void contextLoads() {
    }
}

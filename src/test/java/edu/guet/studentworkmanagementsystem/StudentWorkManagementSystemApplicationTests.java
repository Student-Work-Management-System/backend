package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.mapper.student.StudentBasicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private StudentBasicMapper studentBasicMapper;
    @Test
    void contextLoads() {
    }
}

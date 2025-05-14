package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.StringHttpMessageConverter;


@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;

    @Test
    void deleteStudent() {
        String studentId = "22";
        BaseResponse<Object> response = studentService.deleteStudent(studentId);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void recoveryStudent() {
        String studentId = "22";
        BaseResponse<Object> response = studentService.recoveryStudent(studentId);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void testGetArchive() {
        String studentId = "1001";
        BaseResponse<StudentArchive> response = studentService.getStudentArchive(studentId);
        System.out.println(response.getCode() + ": " + response.getMessage());
        StudentArchive data = response.getData();
        System.out.println(data);
    }
}

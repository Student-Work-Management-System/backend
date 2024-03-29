package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentVO;
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
        StudentQuery query = new StudentQuery();
        BaseResponse<Page<StudentVO>> students = service.getStudents(query);
        System.out.println(students);
    }
}

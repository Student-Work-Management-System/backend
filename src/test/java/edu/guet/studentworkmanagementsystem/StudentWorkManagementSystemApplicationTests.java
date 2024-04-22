package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private EmploymentService employmentService;
    @Test
    void contextLoads() {
        BaseResponse<Object> response = employmentService.deleteStudentEmployment("4");
        System.out.println(response);
    }
}

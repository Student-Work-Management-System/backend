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
        EmploymentQuery query = new EmploymentQuery();
        query.setSearch("");
        query.setGraduationYear("2022");
        query.setPageNo(1);
        query.setPageSize(10);
        BaseResponse<Page<StudentEmploymentVO>> studentEmployment = employmentService.getStudentEmployment(query);
        Page<StudentEmploymentVO> data = studentEmployment.getData();
        System.out.println(data);
    }
}

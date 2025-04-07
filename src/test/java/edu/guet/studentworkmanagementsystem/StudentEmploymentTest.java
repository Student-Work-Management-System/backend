package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentItem;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentEmploymentTest {
    @Autowired
    private EmploymentService employmentService;
    @Test
    void getPageStudentEmployments() {
        EmploymentQuery query = EmploymentQuery.builder().build();
        BaseResponse<Page<StudentEmploymentItem>> studentEmployment = employmentService.getStudentEmployment(query);
        Page<StudentEmploymentItem> data = studentEmployment.getData();
        System.out.println(data);
    }
}

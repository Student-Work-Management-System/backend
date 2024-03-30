package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StatusQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusVO;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private StatusService statusService;
    @Test
    void contextLoads() {
        StatusQuery query = new StatusQuery();
        BaseResponse<Page<StudentStatusVO>> allRecords = statusService.getAllRecords(query);
        System.out.println(allRecords);
    }
}

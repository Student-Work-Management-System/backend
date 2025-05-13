package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.leave.LeaveStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.leave.StudentLeaveStatGroup;
import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class LeaveTest {
    @Autowired
    private LeaveService leaveService;
    @Test
    void testStat() {
        LeaveStatQuery query = new LeaveStatQuery();
        BaseResponse<List<StudentLeaveStatGroup>> response = leaveService.getStat(query);
        int code = response.getCode();
        String msg = response.getMessage();
        System.out.println(code + " - " + msg);
        List<StudentLeaveStatGroup> data = response.getData();
        System.out.println(data.size());
    }
}

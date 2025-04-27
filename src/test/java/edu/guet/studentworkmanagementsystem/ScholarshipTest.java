package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatGroup;
import edu.guet.studentworkmanagementsystem.service.scholarship.ScholarshipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ScholarshipTest {
    @Autowired
    private ScholarshipService scholarshipService;
    @Test
    void testStat() {
        ScholarshipStatQuery build = ScholarshipStatQuery.builder().build();
        BaseResponse<List<StudentScholarshipStatGroup>> stat = scholarshipService.getStat(build);
        List<StudentScholarshipStatGroup> data = stat.getData();
        System.out.println(data);
    }
}

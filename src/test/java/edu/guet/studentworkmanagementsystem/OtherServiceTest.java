package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OtherServiceTest {
    @Autowired
    private OtherService otherService;
    @Test
    public void getAllGrades() {
        long start = System.currentTimeMillis();
        List<Grade> gradeList = otherService.getGradeList();
        long end = System.currentTimeMillis();
        List<Grade> gradeList1 = otherService.getGradeList();
        long end1 = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(end1 - start);
    }
}

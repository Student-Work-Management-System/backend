package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkMember;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentPaper;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AcademicWorkTest {
    @Autowired
    private AcademicWorkService academicWorkService;

    @Test
    void addPaper() {
        StudentPaper paper = StudentPaper.builder()
                .periodicalName("测试期刊")
                .jrcPartition("一区")
                .casPartition("一区")
                .recordedTime(LocalDate.now())
                .searchedTime(LocalDate.now())
                .isMeeting(true)
                .isChineseCore(true)
                .isEI(true)
                .isEIRecorded(true)
                .build();
        AcademicWorkMember member1 = new AcademicWorkMember("1", "1", false);
        AcademicWorkMember member2 = new AcademicWorkMember("2", "1001",true );
        AcademicWorkMember member3 = new AcademicWorkMember("3", "1002",true );
        AcademicWorkMember member4 = new AcademicWorkMember("4", "1003",true );
        AcademicWorkMember member5 = new AcademicWorkMember("5", "1004",true );
        ArrayList<AcademicWorkMember> team = new ArrayList<>(List.of(member1, member2, member3, member4, member5));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .uid("1")
                .workName("测试论文")
                .type("paper")
                .evidence("https://www.baidu.com")
                .team(team)
                .academicWork(paper)
                .build();
        BaseResponse<Object> response = academicWorkService.insertStudentAcademicWork(build);
        System.out.println(response.getCode());
    }
}

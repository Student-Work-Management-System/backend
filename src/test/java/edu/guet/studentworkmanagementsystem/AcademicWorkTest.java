package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkMember;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Paper;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Patent;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Soft;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkItem;
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
        Paper paper = Paper.builder()
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
        AcademicWorkMember member1 = new AcademicWorkMember("1", "1");
        AcademicWorkMember member2 = new AcademicWorkMember("2", "1001");
        AcademicWorkMember member3 = new AcademicWorkMember("3", "1002");
        AcademicWorkMember member4 = new AcademicWorkMember("4", "1003");
        AcademicWorkMember member5 = new AcademicWorkMember("5", "1004");
        ArrayList<AcademicWorkMember> team = new ArrayList<>(List.of(member1, member2, member3, member4, member5));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .workName("测试论文")
                .type("paper")
                .evidence("https://www.baidu.com")
                .team(team)
                .abstractAcademicWork(paper)
                .build();
        BaseResponse<Object> response = academicWorkService.insertAcademicWork(build);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void addSoft() {
        Soft soft = Soft.builder()
                .publishInstitution("测试")
                .publishDate(LocalDate.now())
                .build();
        AcademicWorkMember member1 = new AcademicWorkMember("1", "1");
        ArrayList<AcademicWorkMember> team = new ArrayList<>(List.of(member1));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .username("1")
                .workName("测试论文")
                .type("paper")
                .evidence("https://www.baidu.com")
                .team(team)
                .abstractAcademicWork(soft)
                .build();
        BaseResponse<Object> response = academicWorkService.insertAcademicWork(build);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void addPatent() {
        Patent patent = Patent.builder()
                .publishState(Common.WAITING.getValue())
                .acceptDate(LocalDate.now())
                .authorizationDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .build();
        AcademicWorkMember member1 = new AcademicWorkMember("1", "1");
        ArrayList<AcademicWorkMember> team = new ArrayList<>(List.of(member1));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .username("1")
                .workName("测试论文")
                .type("paper")
                .evidence("https://www.baidu.com")
                .team(team)
                .abstractAcademicWork(patent)
                .build();
        BaseResponse<Object> response = academicWorkService.insertAcademicWork(build);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void getOwnTest() {
        String studentId = "admin";
        BaseResponse<List<StudentAcademicWorkItem>> own = academicWorkService.getOwnAcademicWork(studentId);
        List<StudentAcademicWorkItem> data = own.getData();
        System.out.println(data);
    }

    @Test
    void pageTest() {
        AcademicWorkQuery build = AcademicWorkQuery.builder()
                .search("")
                .build();
        BaseResponse<Page<StudentAcademicWorkItem>> allStudentAcademicWork = academicWorkService.getAllAcademicWork(build);
        Page<StudentAcademicWorkItem> data = allStudentAcademicWork.getData();
        System.out.println(data);
    }

    @Test
    void statTest() {
        BaseResponse<AcademicWorkStatGroup> response = academicWorkService.getStat();
        int code = response.getCode();
        String msg = response.getMessage();
        System.out.println(code + " - " + msg);
        AcademicWorkStatGroup data = response.getData();
        System.out.println(data);
    }
}

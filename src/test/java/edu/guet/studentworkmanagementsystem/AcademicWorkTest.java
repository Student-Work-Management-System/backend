package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkMemberRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWorkPaper;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademiciWorkPatent;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWorkSoft;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkItem;
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
        AcademicWorkPaper academicWorkPaper = AcademicWorkPaper.builder()
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
        AcademicWorkMemberRequest member1 = new AcademicWorkMemberRequest("1", "1");
        AcademicWorkMemberRequest member2 = new AcademicWorkMemberRequest("2", "1001");
        AcademicWorkMemberRequest member3 = new AcademicWorkMemberRequest("3", "1002");
        AcademicWorkMemberRequest member4 = new AcademicWorkMemberRequest("4", "1003");
        AcademicWorkMemberRequest member5 = new AcademicWorkMemberRequest("5", "1004");
        ArrayList<AcademicWorkMemberRequest> team = new ArrayList<>(List.of(member1, member2, member3, member4, member5));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .workName("测试论文")
                .type("academicWorkPaper")
                .evidence("https://www.baidu.com")
                .team(team)
                .academicWork(academicWorkPaper)
                .build();
        BaseResponse<Object> response = academicWorkService.insertAcademicWork(build);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void addSoft() {
        AcademicWorkSoft academicWorkSoft = AcademicWorkSoft.builder()
                .publishInstitution("测试")
                .publishDate(LocalDate.now())
                .build();
        AcademicWorkMemberRequest member1 = new AcademicWorkMemberRequest("1", "1");
        ArrayList<AcademicWorkMemberRequest> team = new ArrayList<>(List.of(member1));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .username("1")
                .workName("测试论文")
                .type("paper")
                .evidence("https://www.baidu.com")
                .team(team)
                .academicWork(academicWorkSoft)
                .build();
        BaseResponse<Object> response = academicWorkService.insertAcademicWork(build);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void addPatent() {
        AcademiciWorkPatent academiciWorkPatent = AcademiciWorkPatent.builder()
                .publishState(Common.WAITING.getValue())
                .acceptDate(LocalDate.now())
                .authorizationDate(LocalDate.now())
                .publishDate(LocalDate.now())
                .build();
        AcademicWorkMemberRequest member1 = new AcademicWorkMemberRequest("1", "1");
        ArrayList<AcademicWorkMemberRequest> team = new ArrayList<>(List.of(member1));
        AcademicWorkRequest build = AcademicWorkRequest.builder()
                .username("1")
                .workName("测试论文")
                .type("paper")
                .evidence("https://www.baidu.com")
                .team(team)
                .academicWork(academiciWorkPatent)
                .build();
        BaseResponse<Object> response = academicWorkService.insertAcademicWork(build);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void getOwnTest() {
        String studentId = "admin";
        BaseResponse<List<AcademicWorkItem>> own = academicWorkService.getOwnAcademicWork(studentId);
        List<AcademicWorkItem> data = own.getData();
        System.out.println(data);
    }

    @Test
    void pageTest() {
        AcademicWorkQuery build = AcademicWorkQuery.builder()
                .search("")
                .build();
        BaseResponse<Page<AcademicWorkItem>> allStudentAcademicWork = academicWorkService.getAllAcademicWork(build);
        Page<AcademicWorkItem> data = allStudentAcademicWork.getData();
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

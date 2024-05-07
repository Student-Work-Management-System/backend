package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionVO;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private CompetitionService competitionService;
    @Test
    void contextLoads() {
        BaseResponse<List<StudentCompetitionVO>> ownStudentCompetition = competitionService.getOwnStudentCompetition("2100300316");
        System.out.println(ownStudentCompetition.getData());
    }
}

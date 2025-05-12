package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.controller.CompetitionController;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.CompetitionStatGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@SpringBootTest
public class CompetitionTest {
    @Autowired
    private CompetitionController controller;
    @Test
    @WithMockUser(authorities = { "student_competition:select" })
    public void testStat() {
        CompetitionStatQuery query = new CompetitionStatQuery();
        BaseResponse<List<CompetitionStatGroup>> response = controller.getStat(query);
        int code = response.getCode();
        String msg = response.getMessage();
        System.out.println(code + ": " + msg);
        List<CompetitionStatGroup> groups = response.getData();
        System.out.println(groups);
    }
}

package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private CompetitionService competitionService;
    @Test
    void contextLoads() {
    }
}

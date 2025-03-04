package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.update.UpdateChain;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentBasic;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentBasicMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@SpringBootTest
class StudentWorkManagementSystemApplicationTests {
    @Autowired
    private StudentBasicMapper studentBasicMapper;
    @Test
    void contextLoads() {
    }
}

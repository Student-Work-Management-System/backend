package edu.guet.studentworkmanagementsystem;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatGroup;
import edu.guet.studentworkmanagementsystem.service.cadre.CadreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CadreTest {
    @Autowired
    private CadreService cadreService;
    @Test
    void testStat() {
        CadreStatQuery query = CadreStatQuery.builder().build();
        BaseResponse<List<StudentCadreStatGroup>> cadreStat = cadreService.getCadreStat(query);
        List<StudentCadreStatGroup> data = cadreStat.getData();
        System.out.println(data);
    }
}

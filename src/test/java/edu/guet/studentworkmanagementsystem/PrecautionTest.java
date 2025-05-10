package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.controller.PrecautionController;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionItem;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionStatGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@SpringBootTest
public class PrecautionTest {
    @Autowired
    private PrecautionController controller;
    @Test
    @WithMockUser(authorities = {"student_precaution:insert"})
    void testAdd() {
        String detail = """
                高等数学-上(平时成绩：90, 考试成绩：21, 总成绩: 44)
                程序设计与问题求解(平时成绩：55, 考试成绩：33, 实验成绩: 33,总成绩: 23)
                线性代数(平时成绩：50, 考试成绩：33, 总成绩: 44)
                思想政治(平时成绩：44, 考试成绩：32, 总成绩: 39)
                大学英语(平时成绩：60, 考试成绩：59, 总成绩: 51)
                """;
        StudentPrecaution build = StudentPrecaution.builder()
                .studentId("1001")
                .levelCode("2")
                .term("2021-2022_1")
                .detail(detail)
                .build();
        BaseResponse<Object> response = controller.insertPrecaution(build);
        int code = response.getCode();
        String message = response.getMessage();
        System.out.println("code: " + code + ", message: " + message);
    }

    @Test
    @WithMockUser(authorities = {"student_precaution:insert"})
    void testAdds() {
        List<StudentPrecaution> list = List.of(
                StudentPrecaution.builder()
                        .studentId("1")
                        .levelCode("2")
                        .term("2021-2022_1")
                        .detail("""
            高等数学-上(平时成绩：90, 考试成绩：21, 总成绩: 44)
            程序设计与问题求解(平时成绩：55, 考试成绩：33, 实验成绩: 33, 总成绩: 23)
            线性代数(平时成绩：50, 考试成绩：33, 总成绩: 44)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1002")
                        .levelCode("1")
                        .term("2021-2022_2")
                        .detail("""
            思想政治(平时成绩：44, 考试成绩：32, 总成绩: 39)
            大学英语(平时成绩：60, 考试成绩：59, 总成绩: 51)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1003")
                        .levelCode("0")
                        .term("2022-2023_1")
                        .detail("""
            离散数学(平时成绩：75, 考试成绩：20, 总成绩: 38)
            数据结构(平时成绩：60, 考试成绩：35, 总成绩: 40)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1004")
                        .levelCode("2")
                        .term("2022-2023_2")
                        .detail("""
            高等数学-下(平时成绩：66, 考试成绩：25, 总成绩: 41)
            概率论与数理统计(平时成绩：50, 考试成绩：30, 总成绩: 39)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1005")
                        .levelCode("1")
                        .term("2023-2024_1")
                        .detail("""
            线性代数(平时成绩：45, 考试成绩：42, 总成绩: 43)
            操作系统(平时成绩：59, 考试成绩：33, 总成绩: 44)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1006")
                        .levelCode("0")
                        .term("2023-2024_2")
                        .detail("""
            计算机网络(平时成绩：70, 考试成绩：20, 总成绩: 38)
            数据库原理(平时成绩：68, 考试成绩：31, 总成绩: 42)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1007")
                        .levelCode("1")
                        .term("2021-2022_1")
                        .detail("""
            Java程序设计(平时成绩：72, 考试成绩：28, 总成绩: 45)
            Python编程基础(平时成绩：60, 考试成绩：29, 总成绩: 40)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1008")
                        .levelCode("2")
                        .term("2022-2023_1")
                        .detail("""
            形式与政策(平时成绩：58, 考试成绩：25, 总成绩: 37)
            毛泽东思想(平时成绩：60, 考试成绩：26, 总成绩: 38)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1009")
                        .levelCode("0")
                        .term("2022-2023_2")
                        .detail("""
            数字逻辑(平时成绩：65, 考试成绩：32, 总成绩: 43)
            嵌入式系统基础(平时成绩：50, 考试成绩：34, 总成绩: 40)
        """)
                        .build(),

                StudentPrecaution.builder()
                        .studentId("1010")
                        .levelCode("1")
                        .term("2023-2024_1")
                        .detail("""
            计算机组成原理(平时成绩：70, 考试成绩：33, 总成绩: 45)
            软件工程导论(平时成绩：55, 考试成绩：38, 总成绩: 43)
        """)
                        .build()
        );
        ValidateList<StudentPrecaution> studentPrecautions = new ValidateList<>(list);
        BaseResponse<Object> response = controller.importPrecaution(studentPrecautions);
        int code = response.getCode();
        String message = response.getMessage();
        System.out.println("code: " + code + ", message: " + message);
    }

    @Test
    @WithMockUser(authorities = {"student_precaution:update"})
    void testUpdate() {
        StudentPrecaution build = StudentPrecaution.builder()
                .studentPrecautionId("1")
                .levelCode("0")
                .build();
        BaseResponse<Object> response = controller.updatePrecaution(build);
        int code = response.getCode();
        String message = response.getMessage();
        System.out.println("code: " + code + ", message: " + message);
    }

    @Test
    @WithMockUser(authorities = {"student_precaution:select"})
    void testSelect() {
        PrecautionQuery query = PrecautionQuery.builder().build();
        BaseResponse<Page<StudentPrecautionItem>> response = controller.getAllRecords(query);
        int code = response.getCode();
        String message = response.getMessage();
        System.out.println("code: " + code + ", message: " + message);
        Page<StudentPrecautionItem> data = response.getData();
        System.out.println(data.getTotalRow());
    }

    @Test
    @WithMockUser(authorities = {"student_precaution:select"})
    void testStat() {
        PrecautionStatQuery query = PrecautionStatQuery.builder().build();
        BaseResponse<List<StudentPrecautionStatGroup>> response = controller.stat(query);
        int code = response.getCode();
        String message = response.getMessage();
        System.out.println("code: " + code + ", message: " + message);
        List<StudentPrecautionStatGroup> data = response.getData();
        System.out.println(data);
    }
}

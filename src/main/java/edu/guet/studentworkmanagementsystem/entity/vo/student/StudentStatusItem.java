package edu.guet.studentworkmanagementsystem.entity.vo.student;

import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatusItem implements Serializable {
    private String majorName;
    // 总人数
    private Integer totalCount;
    // 在籍人数
    private Integer normalCount;
    // 休学
    private Integer suspendCount;
    // 入伍
    private Integer militaryCount;
    // 复学
    private Integer returnCount;
    // 转入
    private Integer transferInCount;
    // 转出
    private Integer transferOutCount;
    // 性别
    private Integer maleCount;
    private Integer femaleCount;
    // 群众数量
    private Integer massCount;
    // 共青团员
    private Integer leagueCount;
    // 中共党员
    private Integer partyCount;
    // 预备党员
    private Integer prepareCount;
    // 残疾学生
    private Integer disabilityCount;
    // 少数民族
    private Integer minorityCount;
    private List<Student> originData;
}

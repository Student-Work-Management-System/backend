package edu.guet.studentworkmanagementsystem.entity.vo.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatItem implements Serializable {
    private String majorName;
    // 总人数
    private Long totalCount;
    // 在籍
    private Long normalCount;
    // 休学
    private Long suspendCount;
    // 入伍
    private Long militaryCount;
    // 复学
    private Long returnCount;
    // 转入
    private Long transferInCount;
    // 转出
    private Long transferOutCount;
    // 放弃入学资格
    private Long dropOfEnrollmentCount;
    // 保留入学资格
    private Long retainEnrollmentCount;
    // 结业
    private Long graduationCount;
    // 毕业
    private Long gradCount;
    // 退学
    private Long droppedCount;
    // 改名
    private Long rechristenCount;
    // 死亡
    private Long deathCount;
    // 性别
    private Long maleCount;
    private Long femaleCount;
    // 群众数量
    private Long massCount;
    // 共青团员
    private Long leagueCount;
    // 中共党员
    private Long partyCount;
    // 预备党员
    private Long prepareCount;
    // 残疾学生
    private Long disabilityCount;
    // 少数民族
    private Long minorityCount;
}

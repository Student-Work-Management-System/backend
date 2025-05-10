package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionStatRow {
    private String gradeName;         // 年级名称
    private String majorName;         // 专业名称
    private String competitionTotalName;   // 比赛名称（拼接后的总名，如“蓝桥杯 - 算法赛道 - 本科组” / "中国软件杯 - A02赛题"）
    private String type;              // 类别，如 A类、B类
    private String level;             // 奖项等级，如 国家一等奖
    private Integer count;            // 获奖数量
}

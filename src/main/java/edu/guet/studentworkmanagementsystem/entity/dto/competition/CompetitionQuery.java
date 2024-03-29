package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionQuery implements Serializable {
    private String studentId;
    /**
     * 姓名: 模糊查询参数
     */
    private String name;
    private String grade;
    private String majorId;
    private String awardDate;
    private Integer pageNo;
    private Integer pageSize;
}

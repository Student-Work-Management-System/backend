package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import edu.guet.studentworkmanagementsystem.entity.po.competition.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCompetitionDTO implements Serializable {
    private String competitionId;
    /**
     * 填报人学号/队长学号
     */
    private String headerId;
    /**
     * 证明材料，这里填写文件地址
     */
    private String evidence;
    /**
     * 奖项级别，由学生填报
     */
    private String awardLevel;
    /**
     * 获奖日期
     */
    private Date awardDate;
    /**
     * 团队成员, 单人比赛时为空。团队赛时应该填入的格式为 [{ order: 1, studentId:"",realName:""}....]
     */
    private List<Member> members;
}

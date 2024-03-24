package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Members;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCompetitionVO implements Serializable {
    /**
     * 竞赛名称
     */
    private String competitionName;
    /**
     * 竞赛性质： 团队/单人
     */
    private String competitionNature;
    /**
     * 竞赛级别
     */
    private String competitionLevel;
    /**
     * 填报人学号/队长学号
     */
    private String headerId;
    private String headerName;
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
    private LocalDate awardDate;
    /**
     * 团队成员, 单人比赛时为空。团队赛时应该填入的格式为 [{ order: 1, studentId:"",realName:""}....]
     */
    private Members members;
    /**
     * 上报后审核状态
     */
    private String reviewState;
    /**
     * 仅当状态预修改为 已拒绝 时才可以填写
     */
    private String rejectReason;
    public StudentCompetitionVO(Competition competition, StudentCompetition studentCompetition, Student student) throws JsonProcessingException {
        this.competitionName = competition.getCompetitionName();
        this.competitionNature = competition.getCompetitionNature();
        this.competitionLevel = competition.getCompetitionLevel();
        this.headerId = studentCompetition.getHeaderId();
        this.headerName = student.getName();
        this.awardDate = studentCompetition.getAwardDate();
        this.awardLevel = studentCompetition.getAwardLevel();
        this.members = JsonUtil.mapper.readValue(studentCompetition.getMembers(), Members.class);
        this.reviewState = studentCompetition.getReviewState();
        this.rejectReason = studentCompetition.getRejectReason();
        this.evidence = studentCompetition.getEvidence();
    }
}

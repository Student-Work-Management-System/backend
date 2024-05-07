package edu.guet.studentworkmanagementsystem.entity.po.competition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionDTO;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 学生上传的竞赛审核表 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_competition")
public class StudentCompetition implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String studentCompetitionId;
    /**
     * 竞赛id
     */
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
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate awardDate;
    /**
     * 团队成员, 单人比赛时为空。团队赛时应该填入的格式为 [{ order: 1, studentId:"",realName:""}....]
     */
    private String members;
    /**
     * 上报后审核状态
     */
    private String reviewState;
    /**
     * 仅当状态预修改为 已拒绝 时才可以填写
     */
    private String rejectReason;
    /**
     * 审核人id
     */
    private String auditorId;
    public StudentCompetition(StudentCompetitionDTO studentCompetitionDTO) throws JsonProcessingException {
        this.competitionId = studentCompetitionDTO.getCompetitionId();
        this.headerId = studentCompetitionDTO.getHeaderId();
        this.evidence = studentCompetitionDTO.getEvidence();
        this.awardDate = studentCompetitionDTO.getAwardDate();
        this.awardLevel = studentCompetitionDTO.getAwardLevel();
        this.members = JsonUtil.mapper.writeValueAsString(studentCompetitionDTO.getMembers());
        this.reviewState = "审核中";
        this.rejectReason = null;
        this.auditorId = null;
    }
}

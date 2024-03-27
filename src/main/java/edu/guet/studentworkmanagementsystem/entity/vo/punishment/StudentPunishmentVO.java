package edu.guet.studentworkmanagementsystem.entity.vo.punishment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentVO implements Serializable {
    private Long studentPunishmentId;
    private String studentId;
    private String name;
    private String majorIn;
    /**
     * 处分级别：警告、严重警告、记过、留校查看、开除学籍
     */
    private String punishmentLevel;
    /**
     * 处分理由
     */
    private String punishmentReason;
    /**
     * 处分日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate punishmentDate;
    public StudentPunishmentVO(Student student, StudentPunishment studentPunishment) {
        this.studentPunishmentId = studentPunishment.getStudentPunishmentId();
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.majorIn = student.getMajorIn();
        this.punishmentDate = studentPunishment.getPunishmentDate();
        this.punishmentLevel = studentPunishment.getPunishmentLevel();
        this.punishmentReason = studentPunishment.getPunishmentReason();
    }
}

package edu.guet.studentworkmanagementsystem.entity.vo.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveVO implements Serializable {
    private String studentId;
    private String name;
    private String majorIn;
    private String leaveType;
    private String leaveReason;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDate;
    private Long leaveDuration;
    public StudentLeaveVO(Student student, StudentLeave studentLeave) {
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.majorIn = student.getMajorIn();
        this.leaveType = studentLeave.getLeaveType();
        this.leaveReason = studentLeave.getLeaveReason();
        this.leaveDate = studentLeave.getLeaveDate();
        this.leaveDuration = studentLeave.getLeaveDuration();
    }
}

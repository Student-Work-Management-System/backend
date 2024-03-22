package edu.guet.studentworkmanagementsystem.entity.vo.leave;

import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveVO implements Serializable {
    private Student student;
    private String leaveType;
    private String leaveReason;
    private Date leaveDate;
    private Long leaveDuration;
}

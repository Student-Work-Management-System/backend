package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "请假类型不能为空")
    private String leaveType;
    @NotBlank(message = "请假理由不能为空")
    private String leaveReason;
    @NotNull(message = "请假日期不能为空")
    private Date leaveDate;
    @NotNull(message = "请假时间不能为空")
    private Long leaveDuration;
}

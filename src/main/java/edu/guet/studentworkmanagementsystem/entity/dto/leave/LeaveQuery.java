package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveQuery implements Serializable {
    private String grade;
    private String majorId;
    private String leaveDate;
    private String auditState;
    private Integer pageNo;
    private Integer pageSize;
}

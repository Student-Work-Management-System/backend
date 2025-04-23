package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveQuery implements Serializable {
    private String search;
    private String gradeId;
    private String majorId;
    private String totalDay;
    private String counselorState;
    private String leaderState;
    private Integer pageNo;
    private Integer pageSize;
}

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
public class StudentLeaveQuery implements Serializable {
    private String type;
    private Boolean destroyed;
    private String state;
    private Boolean needLeader;
    private Integer pageNo;
    private Integer pageSize;
}

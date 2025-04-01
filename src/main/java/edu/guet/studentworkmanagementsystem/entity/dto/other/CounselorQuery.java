package edu.guet.studentworkmanagementsystem.entity.dto.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorQuery {
    private String search;
    private String gradeId;
    private String degreeId;
    private Integer pageNo;
    private Integer pageSize;
}

package edu.guet.studentworkmanagementsystem.entity.dto.precaution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrecautionQuery implements Serializable {
    private String search;
    private String detailSearch;
    private String gradeId;
    private String majorId;
    private String levelCode;
    private String startTerm;
    private String endTerm;
    private Integer pageNo;
    private Integer pageSize;
}

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
public class PrecautionStatQuery implements Serializable {
    private String gradeId;
    private String majorId;
    private String startTerm;
    private String endTerm;
}

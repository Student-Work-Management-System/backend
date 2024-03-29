package edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSchoolPrecautionVO implements Serializable {
    private Long studentSchoolPrecautionId;
    private String studentId;
    private String name;
    private String majorName;
    /**
     * 学业预警等级
     */
    private String schoolPrecautionLevel;
    /**
     * YYYY-YYYY_term
     */
    private String precautionTerm;
    /**
     * 原因
     */
    private String detailReason;
    /**
     * 备注
     */
    private String comment;
}

package edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution;

import edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution.StudentSchoolPrecaution;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
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
    private String majorIn;
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
    public StudentSchoolPrecautionVO(Student student, StudentSchoolPrecaution studentSchoolPrecaution) {
        this.studentSchoolPrecautionId = studentSchoolPrecaution.getStudentSchoolPrecautionId();
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.majorIn = student.getMajorIn();
        this.precautionTerm = studentSchoolPrecaution.getPrecautionTerm();
        this.detailReason = studentSchoolPrecaution.getDetailReason();
        this.comment = studentSchoolPrecaution.getComment();
    }
}

package edu.guet.studentworkmanagementsystem.entity.vo.cet;

import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CetVO implements Serializable {
    private Long score;
    private String examDate;
    private String certificateNumber;
    private String examType;
    public CetVO(StudentCet studentCet) {
        this.score = studentCet.getScore();
        this.examDate = studentCet.getExamDate();
        this.certificateNumber = studentCet.getCertificateNumber();
        this.examType = studentCet.getExamType();
    }
}

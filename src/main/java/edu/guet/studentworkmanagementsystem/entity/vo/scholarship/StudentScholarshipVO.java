package edu.guet.studentworkmanagementsystem.entity.vo.scholarship;

import com.mybatisflex.annotation.Id;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScholarshipVO implements Serializable {
    private String studentId;
    private String name;
    private String scholarshipName;
    private String scholarshipLevel;
    private String awardYear;
}

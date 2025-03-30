package edu.guet.studentworkmanagementsystem.entity.vo.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentItem implements Serializable {
    private String enrollmentId;
    private String examineeId;
    private String id;
    private String name;
    private String origin;
    private String enrollMajor;
    private String enrollMajorId;
    private String firstMajor;
    private String score;
    private String enrollTime;
}

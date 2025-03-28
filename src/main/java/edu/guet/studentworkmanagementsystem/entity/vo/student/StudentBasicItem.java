package edu.guet.studentworkmanagementsystem.entity.vo.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentBasicItem implements Serializable {
    private String studentId;
    private String name;
    private String gender;
    private String majorName;
    private String gradeName;
    private String degreeName;
}

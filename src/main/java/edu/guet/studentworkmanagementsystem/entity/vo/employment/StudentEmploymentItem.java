package edu.guet.studentworkmanagementsystem.entity.vo.employment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentItem implements Serializable {
    private String studentEmploymentId;
    private String studentId;
    private String idNumber;
    private String name;
    private String gender;
    private String nation;
    private String phone;
    private String email;
    private String gradeName;
    private String majorName;
    private String degreeName;
    private String politicStatus;
    private String studentFrom;
    private String enrollmentTime;
    private String classNo;
    private String location;
    private String graduationYear;
    private String householdType;
    private String whereabouts;
    private String organizationName;
    private String code;
    private String category;
    private String jobNature;
    private String jobIndustry;
    private String jobLocation;
    private String salary;
    private String contactPerson;
    private String contactPhone;
    private String state;
    private String comment;
}

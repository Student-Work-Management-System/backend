package edu.guet.studentworkmanagementsystem.entity.vo.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 展示表格条目
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentTableItem {
    private String studentId;
    private String idNumber;
    private String name;
    private String gender;
    private String nativePlace;
    private String postalCode;
    private String phone;
    private String email;
    private String degreeId;
    private String degreeName;
    private String nation;
    private String majorId;
    private String majorName;
    private String gradeId;
    private String gradeName;
    private String classNo;
    private String headTeacherUsername;
    private String headTeacherName;
    private String headTeacherPhone;
    private String politicId;
    private String politicStatus;
    private LocalDate joiningTime;
    private Boolean isStudentLoans;
    private String height;
    private String weight;
    private String religiousBeliefs;
    private String location;
    private String familyPopulation;
    private String familyMembers;
    private Boolean isOnlyChild;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String householdRegistration;
    private String householdType;
    private String address;
    private String fatherName;
    private String fatherPhone;
    private String fatherOccupation;
    private String motherName;
    private String motherPhone;
    private String motherOccupation;
    private String guardian;
    private String guardianPhone;
    private String highSchool;
    private String examId;
    private String admissionBatch;
    private String totalExamScore;
    private String foreignLanguage;
    private String foreignScore;
    private String hobbies;
    private String dormitory;
    private Boolean disability;
    private String otherNotes;
    private Boolean enabled;
}

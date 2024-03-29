package edu.guet.studentworkmanagementsystem.entity.vo.employment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentVO implements Serializable {
    private Long studentEmploymentId;
    private String studentId;
    private String name;
    private String majorId;
    private String graduationState;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduationYear;
    private String whereabouts;
    private String jobNature;
    private String jobIndustry;
    private String jobLocation;
    private String category;
    private String salary;
    public StudentEmploymentVO(Student student, StudentEmployment studentEmployment) {
        this.studentEmploymentId = studentEmployment.getStudentEmploymentId();
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.majorId = student.getMajorId();
        this.graduationState = studentEmployment.getGraduationState();
        this.graduationYear = studentEmployment.getGraduationYear();
        this.whereabouts = studentEmployment.getWhereabouts();
        this.jobNature = studentEmployment.getJobNature();
        this.jobIndustry = studentEmployment.getJobIndustry();
        this.jobLocation = studentEmployment.getJobLocation();
        this.category = studentEmployment.getCategory();
        this.salary = studentEmployment.getSalary();
    }
}

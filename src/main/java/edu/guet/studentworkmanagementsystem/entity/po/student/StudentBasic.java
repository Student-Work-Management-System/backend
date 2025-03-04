package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "student_basic")
public class StudentBasic implements Serializable {
    @Id
    private String  studentId;
    private String idNumber;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private Boolean enabled;
}

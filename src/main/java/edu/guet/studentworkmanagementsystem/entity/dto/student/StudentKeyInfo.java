package edu.guet.studentworkmanagementsystem.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentKeyInfo implements Serializable {
    private String studentId;
    private String name;
    private String idNumber;
}

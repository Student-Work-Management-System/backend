package edu.guet.studentworkmanagementsystem.entity.dto.student;

import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentList implements Serializable {
    @Valid
    private List<Student> students;
}

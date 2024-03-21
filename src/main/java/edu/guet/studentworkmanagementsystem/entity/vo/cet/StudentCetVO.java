package edu.guet.studentworkmanagementsystem.entity.vo.cet;

import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCetVO implements Serializable {
    private Student student;
    private List<StudentCet> cets;
}

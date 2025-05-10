package edu.guet.studentworkmanagementsystem.mapper.student;

import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatRow;

import java.util.List;

public interface StudentMapper {
    List<StudentStatRow> getStudentStat(StudentStatQuery query);
}

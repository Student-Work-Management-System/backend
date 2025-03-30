package edu.guet.studentworkmanagementsystem.mapper.student;

import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatItem;

import java.util.List;

public interface StudentMapper {
    List<StudentStatItem> getStudentStatusList(StudentStatQuery query);
}

package edu.guet.studentworkmanagementsystem.mapper.student;

import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatusItem;

import java.util.List;

public interface StudentMapper {
    List<StudentStatusItem> getStudentStatusList(StudentStatusQuery query);
}

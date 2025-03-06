package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentDetail;

import java.util.List;

public interface StudentDetailService extends IService<StudentDetail> {
    /**
     * 导入学生详细信息
     * @param studentDetails 学生详细信息列表
     */
    boolean importStudentDetail(List<StudentDetail> studentDetails);
    /**
     * 更新学生详细信息
     * @param studentDetail - 学生详细信息
     */
    boolean updateStudentDetail(StudentDetail studentDetail);
}

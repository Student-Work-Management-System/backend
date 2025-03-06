package edu.guet.studentworkmanagementsystem.service.student;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentBasic;

import java.util.List;

public interface StudentBasicService extends IService<StudentBasic> {
    /**
     * 导入学生基础信息
     * @param studentBasics 学生基础信息列表
     */
    boolean importStudentBasic(List<StudentBasic> studentBasics);
    /**
     * 更新学生基础信息
     * @param studentBasic - 学生基础信息
     */
    boolean updateStudentBasic(StudentBasic studentBasic);
    /**
     * 删除学生 - enabled控制
     * @param studentId 学号
     */
    boolean deleteStudentBasic(String studentId);
    /**
     * 恢复学生 - enabled控制
     * @param studentId 学号
     */
    boolean recoveryStudentBasic(String studentId);
}

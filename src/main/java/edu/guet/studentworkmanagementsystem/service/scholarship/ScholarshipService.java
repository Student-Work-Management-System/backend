package edu.guet.studentworkmanagementsystem.service.scholarship;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipItem;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatGroup;

import java.util.List;

public interface ScholarshipService extends IService<StudentScholarship> {
    /**
     * 对象添加奖学金记录
     */
    <T> BaseResponse<T> insertScholarship(Scholarship scholarship);
    /**
     * 修改奖学金记录
     * @param scholarship 奖学金记录
     */
    <T> BaseResponse<T> updateScholarship(Scholarship scholarship);
    /**
     * 获取全部奖学金
     */
    BaseResponse<List<Scholarship>> getScholarships();
    /**
     * 删除奖学金记录
     * @param scholarshipId 奖学金表的id
     */
    <T> BaseResponse<T> deleteScholarship(String scholarshipId);
    /**
     * 分页查询学生奖学金信息
     * <br/>
     * @param query 查询参数
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentScholarshipItem>> getStudentScholarship(ScholarshipQuery query);
    /**
     * 分配奖学金
     */
    <T> BaseResponse<T> insertStudentScholarship(ValidateList<StudentScholarship> studentScholarship);
    /**
     * 修改学生获得奖学金时间
     */
    <T> BaseResponse<T> updateStudentScholarship(StudentScholarship studentScholarship);
    /**
     * 删除学生获得奖学金记录
     * @param studentScholarshipId 学生奖学金记录id
     */
    <T> BaseResponse<T> deleteStudentScholarship(String studentScholarshipId);
    BaseResponse<List<StudentScholarshipStatGroup>> getStat(ScholarshipStatQuery query);
}

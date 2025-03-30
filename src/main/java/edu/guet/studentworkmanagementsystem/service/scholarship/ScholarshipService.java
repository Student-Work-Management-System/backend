package edu.guet.studentworkmanagementsystem.service.scholarship;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipList;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.StudentScholarshipRequest;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipItem;

import java.util.List;

public interface ScholarshipService extends IService<StudentScholarship> {
    /**
     * 批量导入奖学金信息记录
     * @param scholarshipList 奖学金信息列表
     * @return 存入数据库中的奖学金记录
     */
    <T> BaseResponse<T> importScholarship(ScholarshipList scholarshipList);
    /**
     * 对象添加奖学金记录
     * @param scholarship 奖学金记录对象
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
     * @param studentScholarshipRequest 学生获得奖学金记录
     */
    <T> BaseResponse<T> arrangeStudentScholarship(StudentScholarshipRequest studentScholarshipRequest);
    /**
     * 修改学生获得奖学金时间
     * @param studentScholarshipRequest 修改需要传递的对象, 某一为空则不修改该属性
     */
    <T> BaseResponse<T> updateStudentScholarship(StudentScholarshipRequest studentScholarshipRequest);
    /**
     * 删除学生获得奖学金记录
     * @param studentScholarshipId 学生奖学金记录id
     */
    <T> BaseResponse<T> deleteStudentScholarship(String studentScholarshipId);
}

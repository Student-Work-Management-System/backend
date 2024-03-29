package edu.guet.studentworkmanagementsystem.service.scholarship;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.StudentScholarshipDTO;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScholarshipService extends IService<StudentScholarship> {
    /**
     * 批量导入奖学金信息记录
     * @param scholarships 奖学金信息列表
     * @return 存入数据库中的奖学金记录
     */
    BaseResponse<List<Scholarship>> importScholarship(List<Scholarship> scholarships);
    /**
     * 对象添加奖学金记录
     * @param scholarship 奖学金记录对象
     */
    BaseResponse<Scholarship> insertScholarship(Scholarship scholarship);
    /**
     * 修改奖学金记录
     * @param scholarship 奖学金记录
     */
    <T> BaseResponse<T> updateScholarship(Scholarship scholarship);
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
    BaseResponse<Page<StudentScholarshipVO>> getStudentScholarship(ScholarshipQuery query);
    /**
     * 分配奖学金
     * @param studentScholarshipDTO 学生获得奖学金记录
     */
    <T> BaseResponse<T> arrangeStudentScholarship(StudentScholarshipDTO studentScholarshipDTO);
    /**
     * 修改学生获得奖学金时间(不能修改获得的奖学金类型,若需变动实现下方)
     * @param studentScholarshipDTO 修改需要传递的对象, 某一为空则不修改该属性
     */
    <T> BaseResponse<T> updateStudentScholarshipInfo(StudentScholarshipDTO studentScholarshipDTO);
    /**
     * 奖学金类型修正
     * @param studentId 学号
     * @param oldScholarshipId 原贫困信息记录id
     * @param newScholarshipId 新贫困信息记录id
     */
    <T> BaseResponse<T> updateStudentScholarship(String studentId, String oldScholarshipId, String newScholarshipId);
    /**
     * 删除学生获得奖学金记录
     * @param studentId 学号
     * @param scholarshipId 贫困信息记录id
     */
    <T> BaseResponse<T> deleteStudentPovertyAssistance(String studentId, String scholarshipId);
}

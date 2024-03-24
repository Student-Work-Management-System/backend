package edu.guet.studentworkmanagementsystem.service.punlishment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentVO;
import org.springframework.web.multipart.MultipartFile;

public interface PunishmentService extends IService<StudentPunishment> {
    /**
     * 使用文件导入学生处分信息
     * @param multipartFile 文件源
     */
    <T> BaseResponse<T> importStudentPunishment(MultipartFile multipartFile);
    /**
     * 添加学生处分信息
     * @param studentPunishment 学生处分信息记录对象
     */
    <T> BaseResponse<T> insertStudentPunishment(StudentPunishment studentPunishment);
    /**
     * 分页获取所有的学生处分记录
     * @param query 查询参数
     * @param pageNo 页号,默认1
     * @param pageSize 页大小, 默认50
     * @return 所有的学生处分记录
     */
    BaseResponse<Page<StudentPunishmentVO>> getAllStudentPunishment(PunishmentQuery query, int pageNo, int pageSize);
    /**
     * 删除处分信息
     * @param studentId 学号
     * @param studentPunishmentId 学生处分记录id
     */
    <T> BaseResponse<T> deleteStudentPunishment(String studentId, String studentPunishmentId);
    /**
     * 更新处分信息
     * @param studentId 学号
     * @param studentPunishmentId 学生处分记录id
     */
    <T> BaseResponse<T> updateStudentPunishment(String studentId, String studentPunishmentId);
}

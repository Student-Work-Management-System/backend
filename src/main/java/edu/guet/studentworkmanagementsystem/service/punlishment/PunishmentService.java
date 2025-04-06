package edu.guet.studentworkmanagementsystem.service.punlishment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentItem;


public interface PunishmentService extends IService<StudentPunishment> {
    /**
     * 批量导入学生处分信息
     */
    <T> BaseResponse<T> importStudentPunishment(ValidateList<StudentPunishment> studentPunishments);
    /**
     * 分页获取所有的学生处分记录
     * @param query 查询参数
     * @return 所有的学生处分记录
     */
    BaseResponse<Page<StudentPunishmentItem>> getStudentPunishments(StudentPunishmentQuery query);
    /**
     * 删除处分信息
     * @param studentPunishmentId 学生处分记录id
     */
    <T> BaseResponse<T> deleteStudentPunishment(String studentPunishmentId);
    /**
     * 更新处分信息
     */
    <T> BaseResponse<T> updateStudentPunishment(StudentPunishment studentPunishment);
}

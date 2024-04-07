package edu.guet.studentworkmanagementsystem.service.punlishment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentDTO;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentVO;

import java.util.List;

public interface PunishmentService extends IService<StudentPunishment> {
    /**
     * 批量导入学生处分信息
     * @param punishmentList 学生处分信息列表
     */
    <T> BaseResponse<T> importStudentPunishment(PunishmentList punishmentList);
    /**
     * 添加学生处分信息
     * @param studentPunishment 学生处分信息记录对象
     */
    <T> BaseResponse<T> insertStudentPunishment(StudentPunishment studentPunishment);
    /**
     * 分页获取所有的学生处分记录
     * @param query 查询参数
     * @return 所有的学生处分记录
     */
    BaseResponse<Page<StudentPunishmentVO>> getAllStudentPunishment(PunishmentQuery query);
    /**
     * 删除处分信息
     * @param studentPunishmentId 学生处分记录id
     */
    <T> BaseResponse<T> deleteStudentPunishment(String studentPunishmentId);
    /**
     * 更新处分信息
     * @param studentPunishmentDTO 处分信息对象
     */
    <T> BaseResponse<T> updateStudentPunishment(StudentPunishmentDTO studentPunishmentDTO);
}

package edu.guet.studentworkmanagementsystem.service.povertyAssistance;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.*;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.PovertyAssistanceStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.StudentPovertyAssistanceItem;

import java.util.List;

public interface PovertyAssistanceService extends IService<StudentPovertyAssistance> {
    BaseResponse<List<PovertyAssistance>> getAllPovertyAssistance();
    /**
     * 批量导入贫困信息记录
     * @param povertyAssistanceList 贫困信息列表
     * @return 存入数据库中的贫困补助记录
     */
    <T> BaseResponse<T> importPovertyAssistance(ValidateList<PovertyAssistance> povertyAssistanceList);
    /**
     * 对象添加贫困信息记录
     * @param povertyAssistance 贫困补助记录对象
     */
    <T> BaseResponse<T> insertPovertyAssistance(PovertyAssistance povertyAssistance);
    /**
     * 修改贫困信息记录
     * @param povertyAssistance 贫困信息记录
     */
    <T> BaseResponse<T> updatePovertyAssistance(PovertyAssistance povertyAssistance);
    /**
     * 删除贫困信息记录
     * @param povertyAssistanceId 贫困补助表的id
     */
    <T> BaseResponse<T> deletePovertyAssistance(String povertyAssistanceId);
    /**
     * 完成学生贫困认证记录
     */
    <T> BaseResponse<T> addStudentPovertyAssistance(StudentPovertyAssistance studentPovertyAssistance);
    /**
     * 批量插入学生贫困任职记录
     */
    <T> BaseResponse<T> importStudentPovertyAssistance(ValidateList<StudentPovertyAssistance> studentPovertyAssistanceList);
    /**
     * 分页查询学生贫困信息
     * <br/>
     * @param query 查询参数
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentPovertyAssistanceItem>> getStudentPovertyAssistance(PovertyAssistanceQuery query);
    /**
     * 修改学生贫困认定信息(不能修改贫困信息记录,若需变动实现下方)
     */
    <T> BaseResponse<T> updateStudentPovertyAssistance(StudentPovertyAssistance studentPovertyAssistance);
    /**
     * 删除学生贫困认定记录
     * @param studentPovertyAssistanceId 学生贫困信息记录id
     */
    <T> BaseResponse<T> deleteStudentPovertyAssistance(String studentPovertyAssistanceId);
    BaseResponse<List<PovertyAssistanceStatGroup>> getStudentPovertyAssistanceStatus(PovertyAssistanceStatQuery query);
}

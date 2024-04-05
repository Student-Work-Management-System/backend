package edu.guet.studentworkmanagementsystem.service.povertyAssistance;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.InsertStudentPovertyAssistanceDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.PovertyAssistanceQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.StudentStudentPovertyAssistanceVO;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.UpdateStudentPovertyAssistanceDTO;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;

import java.util.List;

public interface PovertyAssistanceService extends IService<StudentPovertyAssistance> {
    BaseResponse<List<PovertyAssistance>> getAllPovertyAssistance();
    /**
     * 批量导入贫困信息记录
     * @param povertyAssistanceList 贫困信息列表
     * @return 存入数据库中的贫困补助记录
     */
    <T> BaseResponse<T> importPovertyAssistance(List<PovertyAssistance> povertyAssistanceList);
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
     * @param insertStudentPovertyAssistanceDTO 学生贫困认证记录对象
     */
    <T> BaseResponse<T> arrangeStudentPovertyAssistance(InsertStudentPovertyAssistanceDTO insertStudentPovertyAssistanceDTO);
    /**
     * 分页查询学生贫困信息
     * <br/>
     * @param query 查询参数
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentStudentPovertyAssistanceVO>> getStudentPovertyAssistance(PovertyAssistanceQuery query);
    /**
     * 修改学生贫困认定信息(不能修改贫困信息记录,若需变动实现下方)
     * @param updateStudentPovertyAssistanceDTO 修改需要传递的对象, 某一为空则不修改该属性
     */
    <T> BaseResponse<T> updateStudentPovertyAssistance(UpdateStudentPovertyAssistanceDTO updateStudentPovertyAssistanceDTO);
    /**
     * 删除学生贫困认定记录
     * @param studentPovertyAssistanceId 学生贫困信息记录id
     */
    <T> BaseResponse<T> deleteStudentPovertyAssistance(String studentPovertyAssistanceId);
}

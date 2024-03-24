package edu.guet.studentworkmanagementsystem.service.povertyAssistance;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.PovertyAssistanceQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.StudentPovertyAssistanceDTO;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PovertyAssistanceService extends IService<StudentPovertyAssistance> {
    /**
     * 使用文件导入贫困信息记录
     * @param multipartFile 文件源
     * @return 存入数据库中的贫困补助记录
     */
    BaseResponse<List<PovertyAssistance>> importPovertyAssistance(MultipartFile multipartFile);
    /**
     * 对象添加贫困信息记录
     * @param povertyAssistance 贫困补助记录对象
     */
    BaseResponse<PovertyAssistance> insertPovertyAssistance(PovertyAssistance povertyAssistance);
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
     * @param studentPovertyAssistanceDTO 学生贫困认证记录对象
     */
    <T> BaseResponse<T> arrangeStudentPovertyAssistance(StudentPovertyAssistanceDTO studentPovertyAssistanceDTO);
    /**
     * 分页查询学生贫困信息
     * <br/>
     * @param query 查询参数
     * @param pageNo 页号, 默认1
     * @param pageSize 页大小, 默认50
     * @return 学生就业信息
     */
    BaseResponse<Page<StudentEmploymentVO>> getStudentPovertyAssistance(PovertyAssistanceQuery query, int pageNo, int pageSize);
    /**
     * 修改学生贫困认定信息(不能修改贫困信息记录,若需变动实现下方)
     * @param studentPovertyAssistanceDTO 修改需要传递的对象, 某一为空则不修改该属性
     */
    <T> BaseResponse<T> updateStudentPovertyAssistanceInfo(StudentPovertyAssistanceDTO studentPovertyAssistanceDTO);
    /**
     * 贫困认定信息修正
     * @param studentId 学号
     * @param oldPovertyAssistanceId 原贫困信息记录id
     * @param newPovertyAssistanceId 新贫困信息记录id
     */
    <T> BaseResponse<T> updateStudentPovertyAssistance(String studentId, String oldPovertyAssistanceId, String newPovertyAssistanceId);
    /**
     * 删除学生贫困认定记录
     * @param studentId 学号
     * @param povertyAssistanceId 贫困信息记录id
     */
    <T> BaseResponse<T> deleteStudentPovertyAssistance(String studentId, String povertyAssistanceId);
}

package edu.guet.studentworkmanagementsystem.service.schoolPrecaution;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.PrecautionList;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.StudentSchoolPrecautionRequest;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution.StudentSchoolPrecautionItem;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;


public interface PrecautionService extends IService<StudentPrecaution> {
    /**
     * 批量导入学业预警信息记录
     * @param schoolPrecautionList 学业预警信息列表
     */
    <T> BaseResponse<T> importSchoolPrecaution(PrecautionList schoolPrecautionList);
    /**
     * 添加学业预警记录
     * @param schoolPrecaution 学业预警记录
     */
    <T> BaseResponse<T> insertSchoolPrecaution(StudentPrecaution schoolPrecaution);
    /**
     * 修改学业预警记录
     * @param schoolPrecautionDTO 待修改的学业预警记录
     */
    <T> BaseResponse<T> updateSchoolPrecaution(StudentSchoolPrecautionRequest schoolPrecautionDTO);
    /**
     * 删除学业预警记录
     * @param studentSchoolPrecautionId 学业预警表id
     */
    <T> BaseResponse<T> deleteSchoolPrecaution(String studentSchoolPrecautionId);
    /**
     * 分页查询学生奖学金信息
     * <br/>
     * @param query 查询参数
     * @return 学业预警信息
     */
    BaseResponse<Page<StudentSchoolPrecautionItem>> getAllRecords(PrecautionQuery query);
    BaseResponse<HashMap<String, Object>> stat(PrecautionStatQuery query);
    void download(PrecautionStatQuery query, HttpServletResponse response);
}

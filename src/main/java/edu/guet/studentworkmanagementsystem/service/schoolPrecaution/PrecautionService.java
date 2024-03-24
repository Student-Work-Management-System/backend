package edu.guet.studentworkmanagementsystem.service.schoolPrecaution;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution.StudentSchoolPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution.StudentSchoolPrecautionVO;
import org.springframework.web.multipart.MultipartFile;


public interface PrecautionService extends IService<StudentSchoolPrecaution> {
    /**
     * 使用文件导入学业预警信息记录
     * @param multipartFile 文件源
     * @return 存入数据库中的奖学金记录
     */
    <T> BaseResponse<T> importSchoolPrecaution(MultipartFile multipartFile);
    /**
     * 添加学业预警记录
     * @param schoolPrecaution 学业预警记录
     */
    BaseResponse<Scholarship> insertSchoolPrecaution(StudentSchoolPrecaution schoolPrecaution);
    /**
     * 修改学业预警记录
     * @param schoolPrecaution 待修改的学业预警记录
     */
    <T> BaseResponse<T> updateSchoolPrecaution(StudentSchoolPrecaution schoolPrecaution);
    /**
     * 删除学业预警记录
     * @param studentSchoolPrecautionId 学业预警表id
     */
    <T> BaseResponse<T> deleteSchoolPrecaution(String studentSchoolPrecautionId);
    /**
     * 分页查询学生奖学金信息
     * <br/>
     * @param query 查询参数
     * @param pageNo 页号, 默认1
     * @param pageSize 页大小, 默认50
     * @return 学业预警信息
     */
    BaseResponse<Page<StudentSchoolPrecautionVO>> getAllRecords(PrecautionQuery query, int pageNo, int pageSize);
}

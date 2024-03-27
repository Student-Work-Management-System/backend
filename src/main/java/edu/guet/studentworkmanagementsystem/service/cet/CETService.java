package edu.guet.studentworkmanagementsystem.service.cet;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.CETQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.StudentCetDTO;
import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.StudentCetVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CETService extends IService<StudentCet> {
    /**
     * 批量导入CET成绩
     * @param studentCets 学生CET成绩
     */
    <T> BaseResponse<T> importCETScore(List<StudentCet> studentCets);
    /**
     * 单个插入CET成绩
     * @param studentCet 学生CET成绩
     */
    <T> BaseResponse<T> insertStudentCet(StudentCet studentCet);
    /**
     * 分页获取未通过CET4的所有学生以及考试记录
     * @param query 查询参数
     * @param pageNo 页号,默认: 1
     * @param pageSize 每页大小, 默认: 50
     */
    BaseResponse<Page<StudentCetVO>> getNotPassCET4(CETQuery query, int pageNo, int pageSize);
    /**
     * 获取可选的考试学期
     * @return 学期列表
     */
    BaseResponse<List<String>> getOptionalExamDate();
    /**
     * 修改CET考试记录信息
     * @param studentCetDTO 修改后和CET成绩记录
     */
    <T> BaseResponse<T> updateStudentCET(StudentCetDTO studentCetDTO);
    /**
     * 删除成绩记录
     * @param studentId 学号
     * @param studentCETId 考试成绩记录
     */
    <T> BaseResponse<T> deleteStudentCET(String studentId, String studentCETId);
    // todo: 统计、分析CET成绩
}

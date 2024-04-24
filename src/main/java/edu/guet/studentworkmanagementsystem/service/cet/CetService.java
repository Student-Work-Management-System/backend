package edu.guet.studentworkmanagementsystem.service.cet;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.*;
import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.CetStatistics;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.StudentCetVO;

import java.util.HashMap;
import java.util.List;

public interface CetService extends IService<StudentCet> {
    /**
     * 批量导入CET成绩
     * @param insertCetDTOList 学生CET成绩
     */
    <T> BaseResponse<T> importCETScore(InsertCetDTOList insertCetDTOList);
    /**
     * 单个插入CET成绩
     * @param insertStudentCetDTO 学生CET成绩
     */
    <T> BaseResponse<T> insertStudentCet(InsertStudentCetDTO insertStudentCetDTO);
    /**
     * 分页获取未通过CET4的所有学生以及考试记录
     * @param query 查询参数
     */
    BaseResponse<Page<StudentCetVO>> getAllRecord(CETQuery query);
    /**
     * 获取可选的考试学期
     * @return 学期列表
     */
    BaseResponse<List<String>> getOptionalExamDate();
    /**
     * 修改CET考试记录信息
     * @param updateStudentCetDTO 修改后和CET成绩记录
     */
    <T> BaseResponse<T> updateStudentCET(UpdateStudentCetDTO updateStudentCetDTO);
    /**
     * 删除成绩记录
     * @param studentCetId 考试成绩记录
     */
    <T> BaseResponse<T> deleteStudentCET(String studentCetId);
    /**
     * cet成绩统计
     */
    BaseResponse<HashMap<String, CetStatistics>> getCetStatistics(CetStatQuery query);
}

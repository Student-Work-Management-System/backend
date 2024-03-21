package edu.guet.studentworkmanagementsystem.service.cet;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.StudentCetDTO;
import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.StudentCetVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface StudentCetService extends IService<StudentCet> {
    /**
     * 使用文件导入学生CET成绩, 返回处理结果
     * @param multipartFile 文件源
     */
    <T> BaseResponse<T> importCetScore(MultipartFile multipartFile);
    /**
     * 分页获取未通过CET4的所有学生以及考试记录
     * @param pageNo 页号,默认: 1
     * @param pageSize 每页大小, 默认: 50
     */
    BaseResponse<Page<StudentCetVO>> getNotPassCET4(int pageNo, int pageSize);
    /**
     * 获取学期报考人数和通过率
     * @param examDate 考试学期.
     * @param examType 考试类型。
     * @return 一个储存指定考试学期报考人数
     * 具体见类描述{@link edu.guet.studentworkmanagementsystem.entity.po.student.Student}
     */
    BaseResponse<HashMap<String, String>> getNumberWithPassRate(String examDate, String examType);
    /**
     * 获取可选的考试学期
     * @return 学期列表
     */
    BaseResponse<List<String>> getOptionalExamDate();
    /**
     * 修改CET考试记录信息
     * @param studentCetDTO 前端传输的CET考试对象, 存在属性为空.(使用学号+主键id定位, 这两属性不允许修改)
     */
    <T> BaseResponse<T> updateStudentCet(StudentCetDTO studentCetDTO);
}

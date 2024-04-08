package edu.guet.studentworkmanagementsystem.service.academicWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkList;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Authors;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentAcademicWork;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkVO;

import java.util.List;


public interface AcademicWorkService extends IService<StudentAcademicWork> {
     /**
     * 批量导入学生学术作品
      * @param studentAcademicWorkList 学生学术作品列表
     */
    <T> BaseResponse<T> importStudentAcademicWork(StudentAcademicWorkList studentAcademicWorkList);
    /**
     * 单个上传学生学术著作,其中存在{@link AcademicWork 学术著作类型}
     * <br/>
     * 在插入时同时根据字段{@link StudentAcademicWork 学生学术作品类}的academicWorkType属性生成不同的实例, 具体见数据库
     * @param studentAcademicWorkDTO 学生学术著作上报对象
     */
    <T> BaseResponse<T> insertStudentAcademicWork(StudentAcademicWorkDTO studentAcademicWorkDTO);
    /**
     * 使用于上方插入
     * @param academicWork 学术作品详细信息
     * @return 插入成功后对应表的id
     */
    Long insertAcademicWork(AcademicWork academicWork, String typeId);
    /**
     * 删除学术作品(需要考虑外检约束: 类型->学术作品id->学术作品记录、认领表记录)
     * @param studentAcademicWorkId 学术作品记录id
     */
    <T> BaseResponse<T> deleteStudentAcademicWork(String studentAcademicWorkId);
    /**
     * 学生查询自己上报的学术作品记录
     * @param studentId 查询学生学号
     * @return 学生上报的学术著作记录
     */
    BaseResponse<List<StudentAcademicWorkVO>> getOwnStudentAcademicWork(String studentId);
    /**
     * 审核学生学术作品认证结果
     * <br/>
     * 通过 学生学术作品id 和 上报人学号 定位数据, 修改审核结果。<br/>
     * 拒绝 -> 修改状态 并 填入拒绝理由 <br/>
     * 通过 -> 修改状态 并 存入认领表
     * @param academicWorkAuditDTO 学术作品审核
     */
    <T> BaseResponse<T> auditStudentAcademicWork(AcademicWorkAuditDTO academicWorkAuditDTO) throws JsonProcessingException;
    /**
     * 审核通过后调用插入学术认证结果认领表(包括上报者一齐存入认领表)<br/>
     * 应在插入前筛选出合法的学号(本系统仅关注 本校 的 学生 )<br/>
     * 教师不在本系统关注范围内
     * @param authors 作者
     * @param studentAcademicWorkId 学生学术作品id
     * @return 表中修改的行数(判断是否全部插入)
     */
    <T> BaseResponse<T> insertStudentAcademicWorkAudit(Authors authors, String studentAcademicWorkId);
    /**
     * (审核人用)分页查询学生上报记录, 默认只查询状态为 待审核 的上报记录
     * <br/>
     * 审核人在前端不展示, 拒绝理由只有状态未 未通过 时才展示
     * @param query 查询参数
     * @return 上报结果
     */
    BaseResponse<Page<StudentAcademicWorkVO>> getAllStudentAcademicWork(AcademicWorkQuery query);
}

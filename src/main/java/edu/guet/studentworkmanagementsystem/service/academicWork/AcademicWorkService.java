package edu.guet.studentworkmanagementsystem.service.academicWork;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentAcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.StudentAcademicWorkAudit;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkUser;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkItem;

import java.util.List;


public interface AcademicWorkService extends IService<StudentAcademicWork> {
    <T> BaseResponse<T> insertStudentAcademicWork(AcademicWorkRequest academicWorkRequest);
    <T> BaseResponse<T> deleteStudentAcademicWork(String studentAcademicWorkId);
    BaseResponse<List<StudentAcademicWorkItem>> getOwnStudentAcademicWork(String uid);
    <T> BaseResponse<T> updateStudentAcademicWorkAudit(List<StudentAcademicWorkAudit> audits);
    BaseResponse<Page<StudentAcademicWorkItem>> getAllStudentAcademicWork(AcademicWorkQuery query);
    BaseResponse<List<AcademicWorkUser>> getOptionalUserByUsername(String username);
    BaseResponse<AcademicWorkStatGroup> getStat();
}

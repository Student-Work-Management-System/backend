package edu.guet.studentworkmanagementsystem.service.academicWork;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWorkAudit;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkUser;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkItem;

import java.util.List;


public interface AcademicWorkService extends IService<AcademicWork> {
    <T> BaseResponse<T> insertAcademicWork(AcademicWorkRequest academicWorkRequest);
    <T> BaseResponse<T> deleteAcademicWork(String studentAcademicWorkId);
    BaseResponse<List<AcademicWorkItem>> getOwnAcademicWork(String username);
    <T> BaseResponse<T> updateAcademicWorkAudit(List<AcademicWorkAudit> audits);
    BaseResponse<Page<AcademicWorkItem>> getAllAcademicWork(AcademicWorkQuery query);
    BaseResponse<List<AcademicWorkUser>> getOptionalUserByUsername(String username);
    BaseResponse<AcademicWorkStatGroup> getStat();
}

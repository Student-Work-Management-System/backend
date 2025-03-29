package edu.guet.studentworkmanagementsystem.service.competition;


import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetitionAudit;

public interface CompetitionAuditService extends IService<StudentCompetitionAudit> {
    boolean insertCompetitionAudit(StudentCompetitionAudit studentCompetitionAudit);
    boolean deleteCompetitionAudit(String studentCompetitionId);
    <T> BaseResponse<T> updateCompetitionAudit(ValidateList<StudentCompetitionAudit> studentCompetitionAudits);
}

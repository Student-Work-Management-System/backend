package edu.guet.studentworkmanagementsystem.service.student;

import edu.guet.studentworkmanagementsystem.entity.vo.student.archive.*;

import java.util.List;

public interface ArchiveService {
    EnrollmentBase getEnrollmentBase(String studentId);
    List<StatusBase> getStatusBaseList(String studentId);
    List<ScholarshipBase> getScholarshipBaseList(String studentId);
    List<PunishmentBase> getPunishmentBaseList(String studentId);
    List<PovertyAssistanceBase> getPovertyAssistanceBaseList(String studentId);
    List<ForeignLanguageBase> getForeignLanguageBaseList(String studentId);
    List<PrecautionBase> getPrecautionBaseList(String studentId);
    List<AcademicWorkBase> getAcademicWorkBaseList(String studentId);
    List<CompetitionBase> getCompetitionBaseList(String studentId);
}

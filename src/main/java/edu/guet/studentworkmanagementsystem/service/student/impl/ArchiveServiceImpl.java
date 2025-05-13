package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.query.QueryChain;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.ForeignLanguage;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.archive.*;
import edu.guet.studentworkmanagementsystem.service.student.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.AcademicWorkAuditTableDef.ACADEMIC_WORK_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.AcademicWorkMemberTableDef.ACADEMIC_WORK_MEMBER;
import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.AcademicWorkTableDef.ACADEMIC_WORK;
import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentTableDef.ENROLLMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.table.ForeignLanguageTableDef.FOREIGN_LANGUAGE;
import static edu.guet.studentworkmanagementsystem.entity.po.foreignLanguage.table.LanguageTableDef.LANGUAGE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.PoliticTableDef.POLITIC;
import static edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.table.PovertyAssistanceTableDef.POVERTY_ASSISTANCE;
import static edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.table.StudentPovertyAssistanceTableDef.STUDENT_POVERTY_ASSISTANCE;
import static edu.guet.studentworkmanagementsystem.entity.po.precaution.table.StudentPrecautionTableDef.STUDENT_PRECAUTION;
import static edu.guet.studentworkmanagementsystem.entity.po.punishment.table.PunishmentTableDef.PUNISHMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.punishment.table.StudentPunishmentTableDef.STUDENT_PUNISHMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.scholarship.table.ScholarshipTableDef.SCHOLARSHIP;
import static edu.guet.studentworkmanagementsystem.entity.po.scholarship.table.StudentScholarshipTableDef.STUDENT_SCHOLARSHIP;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StatusTableDef.STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StudentStatusTableDef.STUDENT_STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    @Autowired
    @Qualifier("readThreadPool")
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    public EnrollmentBase getEnrollmentBase(String studentId) {
        return QueryChain.of(Enrollment.class)
                .select(
                        ENROLLMENT.ALL_COLUMNS,
                        MAJOR.ALL_COLUMNS,
                        POLITIC.ALL_COLUMNS,
                        DEGREE.ALL_COLUMNS,
                        GRADE.ALL_COLUMNS,
                        STATUS.ALL_COLUMNS,
                        USER.USERNAME.as("headerTeacherUsername"),
                        USER.REAL_NAME.as("headerTeacherRealName"),
                        USER.PHONE.as("headerTeacherPhone")
                )
                .from(ENROLLMENT)
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(ENROLLMENT.MAJOR_ID))
                .innerJoin(POLITIC).on(POLITIC.POLITIC_ID.eq(ENROLLMENT.POLITIC_ID))
                .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(ENROLLMENT.DEGREE_ID))
                .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(ENROLLMENT.GRADE_ID))
                .innerJoin(STUDENT_STATUS).on(ENROLLMENT.STUDENT_ID.eq(STUDENT_STATUS.STUDENT_ID).and(STUDENT_STATUS.STATUS_ENABLED.eq(true)))
                .innerJoin(STATUS).on(STATUS.STATUS_ID.eq(STUDENT_STATUS.STATUS_ID))
                .innerJoin(USER).on(ENROLLMENT.HEADER_TEACHER_USERNAME.eq(USER.USERNAME))
                .where(ENROLLMENT.STUDENT_ID.eq(studentId))
                .oneAs(EnrollmentBase.class);
    }

    @Override
    public List<StatusBase> getStatusBaseList(String studentId) {
        return QueryChain.of(StudentStatus.class)
                .select(
                        STUDENT_STATUS.ALL_COLUMNS,
                        STATUS.ALL_COLUMNS
                )
                .from(STUDENT_STATUS)
                .innerJoin(STATUS).on(STATUS.STATUS_ID.eq(STUDENT_STATUS.STATUS_ID))
                .where(STUDENT_STATUS.STUDENT_ID.eq(studentId))
                .orderBy(STUDENT_STATUS.STUDENT_STATUS_ID.desc())
                .listAs(StatusBase.class);
    }

    @Override
    public List<ScholarshipBase> getScholarshipBaseList(String studentId) {
        return QueryChain.of(StudentScholarship.class)
                .select(
                        SCHOLARSHIP.ALL_COLUMNS,
                        STUDENT_SCHOLARSHIP.ALL_COLUMNS
                )
                .from(STUDENT_SCHOLARSHIP)
                .innerJoin(SCHOLARSHIP).on(SCHOLARSHIP.SCHOLARSHIP_ID.eq(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID))
                .where(STUDENT_SCHOLARSHIP.STUDENT_ID.eq(studentId))
                .listAs(ScholarshipBase.class);
    }

    @Override
    public List<PunishmentBase> getPunishmentBaseList(String studentId) {
        return QueryChain.of(StudentPunishment.class)
                .select(
                        STUDENT_PUNISHMENT.ALL_COLUMNS,
                        PUNISHMENT.ALL_COLUMNS
                )
                .from(STUDENT_PUNISHMENT)
                .innerJoin(PUNISHMENT).on(PUNISHMENT.PUNISHMENT_ID.eq(STUDENT_PUNISHMENT.PUNISHMENT_ID))
                .where(STUDENT_PUNISHMENT.STUDENT_ID.eq(studentId))
                .listAs(PunishmentBase.class);
    }

    @Override
    public List<PovertyAssistanceBase> getPovertyAssistanceBaseList(String studentId) {
        return QueryChain.of(StudentPovertyAssistance.class)
                .select(
                        POVERTY_ASSISTANCE.ALL_COLUMNS,
                        STUDENT_POVERTY_ASSISTANCE.ALL_COLUMNS
                )
                .from(STUDENT_POVERTY_ASSISTANCE)
                .innerJoin(POVERTY_ASSISTANCE).on(POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_ID.eq(STUDENT_POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_ID))
                .where(STUDENT_POVERTY_ASSISTANCE.STUDENT_ID.eq(studentId))
                .listAs(PovertyAssistanceBase.class);
    }

    @Override
    public List<ForeignLanguageBase> getForeignLanguageBaseList(String studentId) {
        return QueryChain.of(ForeignLanguage.class)
                .select(
                        FOREIGN_LANGUAGE.ALL_COLUMNS,
                        LANGUAGE.ALL_COLUMNS
                )
                .from(FOREIGN_LANGUAGE)
                .innerJoin(LANGUAGE).on(LANGUAGE.LANGUAGE_ID.eq(FOREIGN_LANGUAGE.LANGUAGE_ID))
                .where(FOREIGN_LANGUAGE.STUDENT_ID.eq(studentId))
                .listAs(ForeignLanguageBase.class);
    }

    @Override
    public List<PrecautionBase> getPrecautionBaseList(String studentId) {
        return QueryChain.of(StudentPrecaution.class)
                .select(STUDENT_PRECAUTION.ALL_COLUMNS)
                .from(STUDENT_PRECAUTION)
                .where(STUDENT_PRECAUTION.STUDENT_ID.eq(studentId))
                .listAs(PrecautionBase.class);
    }

    @Override
    public List<AcademicWorkBase> getAcademicWorkBaseList(String studentId) {
        List<AcademicWorkBase> academicWorkBases = QueryChain.of(AcademicWork.class)
                .select(
                        USER.REAL_NAME,
                        ACADEMIC_WORK.ALL_COLUMNS
                )
                .from(ACADEMIC_WORK)
                .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                .innerJoin(ACADEMIC_WORK_AUDIT)
                .on(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID)
                        .and(ACADEMIC_WORK_AUDIT.STATE.eq(Common.PASS.getValue())))
                .innerJoin(ACADEMIC_WORK_MEMBER)
                .on(ACADEMIC_WORK_MEMBER.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID))
                .where(USER.USERNAME.eq(studentId).or(ACADEMIC_WORK_MEMBER.USERNAME.eq(studentId)))
                .listAs(AcademicWorkBase.class);

        List<StudentAcademicWorkItem> records = QueryChain.of(AcademicWork.class)
                .select(
                        USER.ALL_COLUMNS,
                        ACADEMIC_WORK.ALL_COLUMNS,
                        ACADEMIC_WORK_AUDIT.ALL_COLUMNS
                )
                .from(ACADEMIC_WORK)
                .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                .innerJoin(ACADEMIC_WORK_AUDIT).on(
                        ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID)
                )
                .where(ACADEMIC_WORK.ACADEMIC_WORK_ID.in())
                .listAs(StudentAcademicWorkItem.class);
        academicWorkBases.forEach(item -> {

        });
        return List.of();
    }

    @Override
    public List<CompetitionBase> getCompetitionBaseList(String studentId) {
        return List.of();
    }
}

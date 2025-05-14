package edu.guet.studentworkmanagementsystem.entity.vo.student;

import edu.guet.studentworkmanagementsystem.entity.vo.student.archive.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentArchive {
    /**
     * 学籍信息
     */
    private EnrollmentBase enrollment;
    private List<StatusBase> statuses;
    /**
     * 奖惩信息
     */
    private List<ScholarshipBase> scholarships;
    private List<PunishmentBase> punishments;
    /**
     * 贫困补助信息
     */
    private List<PovertyAssistanceBase> povertyAssistances;
    /**
     * 学业信息
     */
    private List<ForeignLanguageBase> foreignLanguages;
    private List<PrecautionBase> precautions;
    /**
     * 学术成果与竞赛获奖
     */
    private List<AcademicWorkBase> academicWorks;
    private List<CompetitionBase> competitions;
}

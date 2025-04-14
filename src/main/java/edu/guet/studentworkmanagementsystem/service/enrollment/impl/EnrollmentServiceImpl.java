package edu.guet.studentworkmanagementsystem.service.enrollment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.enrollment.EnrollmentMapper;
import edu.guet.studentworkmanagementsystem.network.EnrollmentFeign;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.common.Majors.majorName2MajorId;
import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentTableDef.ENROLLMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.PoliticTableDef.POLITIC;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {
    @Autowired
    private EnrollmentFeign enrollmentFeign;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    @Transactional
    public <T> BaseResponse<T> importEnrollment(ValidateList<Enrollment> enrollments) {
        int i = mapper.insertBatch(enrollments);
        int size = enrollments.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateEnrollment(Enrollment enrollment) {
        boolean update = UpdateChain.of(Enrollment.class)
                // 个人信息
                .set(ENROLLMENT.NAME, enrollment.getName(), StringUtils::hasLength)
                .set(ENROLLMENT.GENDER, enrollment.getGender(), StringUtils::hasLength)
                .set(ENROLLMENT.BIRTHDATE, enrollment.getBirthdate())
                .set(ENROLLMENT.HOBBIES, enrollment.getHobbies())
                .set(ENROLLMENT.NATION, enrollment.getNation())
                .set(ENROLLMENT.HEIGHT, enrollment.getHeight())
                .set(ENROLLMENT.WEIGHT, enrollment.getWeight())
                .set(ENROLLMENT.POLITIC_ID, enrollment.getPoliticId(), StringUtils::hasLength)
                .set(ENROLLMENT.PHONE, enrollment.getPhone())
                // 在校信息
                .set(ENROLLMENT.DORMITORY, enrollment.getDormitory())
                .set(ENROLLMENT.CLASS_NO, enrollment.getAddress())
                .set(ENROLLMENT.MAJOR_ID, enrollment.getMajorId(), StringUtils::hasLength)
                .set(ENROLLMENT.DEGREE_ID, enrollment.getDegreeId(), StringUtils::hasLength)
                // 高考信息
                .set(ENROLLMENT.STUDENT_TYPE, enrollment.getStudentType())
                .set(ENROLLMENT.ADMISSION_BATCH, enrollment.getAdmissionBatch())
                .set(ENROLLMENT.SUBJECT_CATEGORY, enrollment.getSubjectCategory())
                .set(ENROLLMENT.PROVINCE_NAME, enrollment.getProvinceName())
                .set(ENROLLMENT.EXAM_ID, enrollment.getExamId())
                .set(ENROLLMENT.ADMITTED_MAJOR, enrollment.getAdmittedMajor())
                .set(ENROLLMENT.VOLUNTEER_MAJOR, enrollment.getVolunteerMajor())
                .set(ENROLLMENT.VOLUNTEER_COLLEGE, enrollment.getVolunteerCollege())
                .set(ENROLLMENT.TOTAL_EXAM_SCORE, enrollment.getTotalExamScore())
                .set(ENROLLMENT.CONVERTED_SCORE, enrollment.getConvertedScore())
                .set(ENROLLMENT.SPECIAL_SCORE, enrollment.getSpecialScore())
                .set(ENROLLMENT.FEATURE, enrollment.getFeature())
                .set(ENROLLMENT.VOLUNTEER1, enrollment.getVolunteer1())
                .set(ENROLLMENT.VOLUNTEER2, enrollment.getVolunteer2())
                .set(ENROLLMENT.VOLUNTEER3, enrollment.getVolunteer3())
                .set(ENROLLMENT.VOLUNTEER4, enrollment.getVolunteer4())
                .set(ENROLLMENT.VOLUNTEER5, enrollment.getVolunteer5())
                .set(ENROLLMENT.VOLUNTEER6, enrollment.getVolunteer6())
                .set(ENROLLMENT.STUDENT_FROM, enrollment.getStudentFrom())
                .set(ENROLLMENT.IS_ADJUSTED, enrollment.getIsAdjusted(), !Objects.isNull(enrollment.getIsAdjusted()))
                // 收件人信息
                .set(ENROLLMENT.NATIVE_PLACE, enrollment.getNativePlace())
                .set(ENROLLMENT.RECEIVER, enrollment.getReceiver())
                .set(ENROLLMENT.RECEIVER_PHONE, enrollment.getReceiverPhone())
                .set(ENROLLMENT.POSTAL_CODE, enrollment.getPostalCode())
                // 户口信息
                .set(ENROLLMENT.HOUSEHOLD_REGISTRATION, enrollment.getHouseholdRegistration())
                .set(ENROLLMENT.HOUSEHOLD_TYPE, enrollment.getHouseholdType())
                .set(ENROLLMENT.ADDRESS, enrollment.getAddress())
                .set(ENROLLMENT.FATHER_NAME, enrollment.getFatherName())
                .set(ENROLLMENT.FATHER_PHONE, enrollment.getFatherPhone())
                .set(ENROLLMENT.FATHER_OCCUPATION, enrollment.getFatherOccupation())
                .set(ENROLLMENT.MOTHER_NAME, enrollment.getMotherName())
                .set(ENROLLMENT.MOTHER_PHONE, enrollment.getMotherPhone())
                .set(ENROLLMENT.MOTHER_OCCUPATION, enrollment.getMotherOccupation())
                .set(ENROLLMENT.GUARDIAN, enrollment.getGuardian())
                .set(ENROLLMENT.GUARDIAN_PHONE, enrollment.getGuardianPhone())
                .set(ENROLLMENT.FAMILY_POPULATION, enrollment.getFamilyPopulation())
                .set(ENROLLMENT.FAMILY_MEMBERS, enrollment.getFamilyMembers())
                .set(ENROLLMENT.FAMILY_LOCATION, enrollment.getFamilyLocation())
                .set(ENROLLMENT.IS_ONLY_CHILD, enrollment.getIsOnlyChild(), !Objects.isNull(enrollment.getIsOnlyChild()))
                // 中学信息
                .set(ENROLLMENT.HIGH_SCHOOL_CODE, enrollment.getHighSchoolCode())
                .set(ENROLLMENT.HIGH_SCHOOL_NAME, enrollment.getHighSchoolName())
                .set(ENROLLMENT.CANDIDATE_CATEGORY_CLASSIFICATION, enrollment.getCandidateCategoryClassification())
                .set(ENROLLMENT.GRADUATION_CATEGORY_CLASSIFICATION, enrollment.getGraduationCategoryClassification())
                .set(ENROLLMENT.GRADUATION_CATEGORY, enrollment.getGraduationCategory())
                .set(ENROLLMENT.CANDIDATE_CATEGORY, enrollment.getCandidateCategory())
                .set(ENROLLMENT.FOREIGN_LANGUAGE_ID, enrollment.getForeignLanguageId())
                .set(ENROLLMENT.SCORE_CHINESE, enrollment.getScoreChinese())
                .set(ENROLLMENT.SCORE_MATH, enrollment.getScoreMath())
                .set(ENROLLMENT.SCORE_FOREIGN_LANGUAGE, enrollment.getScoreForeignLanguage())
                .set(ENROLLMENT.SCORE_COMPREHENSIVE, enrollment.getScoreComprehensive())
                .set(ENROLLMENT.SCORE_PHYSICS, enrollment.getScorePhysics())
                .set(ENROLLMENT.SCORE_CHEMISTRY, enrollment.getScoreChemistry())
                .set(ENROLLMENT.SCORE_BIOLOGY, enrollment.getScoreBiology())
                .set(ENROLLMENT.SCORE_POLITICS, enrollment.getScorePolitics())
                .set(ENROLLMENT.SCORE_HISTORY, enrollment.getScoreHistory())
                .set(ENROLLMENT.SCORE_GEOGRAPHY, enrollment.getScoreGeography())
                .set(ENROLLMENT.SCORE_TECHNOLOGY, enrollment.getScoreTechnology())
                .set(ENROLLMENT.SELECTED_SUBJECTS, enrollment.getSelectedSubjects())
                // 其他
                .set(ENROLLMENT.STUDENT_LOANS, enrollment.getStudentLoans(), !Objects.isNull(enrollment.getStudentLoans()))
                .set(ENROLLMENT.DISABILITY, enrollment.getDisability(), !Objects.isNull(enrollment.getDisability()))
                // 备注
                .set(ENROLLMENT.OTHER_NOTES, enrollment.getOtherNotes())
                .where(ENROLLMENT.ENROLLMENT_ID.eq(enrollment.getEnrollmentId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteEnrollment(String enrollmentId) {
        QueryWrapper deleteWrapper =
                QueryWrapper.create().where(ENROLLMENT.ENROLLMENT_ID.eq(enrollmentId));
        int i = mapper.deleteByQuery(deleteWrapper);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<EnrollmentItem>> getAllRecords(EnrollmentQuery query) {
        CompletableFuture<Page<EnrollmentItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(Enrollment.class)
                    .select(
                            ENROLLMENT.ALL_COLUMNS,
                            MAJOR.ALL_COLUMNS,
                            POLITIC.ALL_COLUMNS,
                            DEGREE.ALL_COLUMNS
                            )
                    .from(ENROLLMENT)
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(ENROLLMENT.MAJOR_ID))
                    .innerJoin(POLITIC).on(POLITIC.POLITIC_ID.eq(ENROLLMENT.POLITIC_ID))
                    .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(ENROLLMENT.DEGREE_ID))
                    // todo: 补充where查询逻辑
                    .pageAs(Page.of(pageNo, pageSize), EnrollmentItem.class);
        }, readThreadPool);
        Page<EnrollmentItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public void download(EnrollmentStatQuery query, HttpServletResponse response) {
        try {
            List<String> majorIds = query.getMajorIds();
            if (majorIds.isEmpty()) {
                int key = 1;
                while (key <= 6) {
                    majorIds.add(String.valueOf(key));
                    key++;
                }
            }
            query.setMajorIds(majorIds);
            byte[] excelBytes = enrollmentFeign.exportWithStat(query);
            String fileName = "招生信息统计.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.getOutputStream().write(excelBytes);
        } catch (IOException exception) {
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }

    @Override
    public BaseResponse<HashMap<String, EnrollmentStatItem>> statistics(EnrollmentStatQuery query) {
        Set<String> keys = new HashSet<>();
        List<String> majorIds = query.getMajorIds();
        if (majorIds.isEmpty()) {
            int key = 1;
            while (key <= 6) {
                String s = String.valueOf(key);
                majorIds.add(s);
                key++;
            }
            keys = majorName2MajorId.keySet();
        } else {
            for (String majorId : majorIds) {
                for (String key : majorName2MajorId.keySet()) {
                    if (majorName2MajorId.get(key).equals(majorId)) {
                        keys.add(key);
                    }
                }
            }
        }
        query.setMajorIds(majorIds);
        HashMap<String, Object> map = enrollmentFeign.exportOnlyStat(query);
        if (map.isEmpty())
            return ResponseUtil.success();
        HashMap<String, HashMap<String, Object>> originMap = (HashMap<String, HashMap<String, Object>>) map.get("生源地情况");
        HashMap<String, HashMap<String, Object>> enrollmentStateMap = (HashMap<String, HashMap<String, Object>>) map.get("录取情况");
        HashMap<String, Object> reginScoreMap = (HashMap<String, Object>) map.get("各生源地高考分数统计");
        HashMap<String, EnrollmentStatItem> ret = new HashMap<>();
        keys.forEach(key -> {
            EnrollmentStatItem enrollmentStatItem = new EnrollmentStatItem();
            HashMap<String, HashMap<String, Object>> reginScore = new HashMap<>();
            if (originMap.containsKey(key)) {
                enrollmentStatItem.setOrigin(originMap.get(key));
            }
            if (enrollmentStateMap.containsKey(key)) {
                enrollmentStatItem.setEnrollmentState(enrollmentStateMap.get(key));
            }
            Set<String> reginScoreKeys = reginScoreMap.keySet();
            reginScoreKeys.forEach(it -> {
                HashMap<String, HashMap<String, Object>> hashMap = (HashMap<String, HashMap<String, Object>>) reginScoreMap.get(it);
                if (hashMap.containsKey(key)) {
                    reginScore.put(it, hashMap.get(key));
                }
            });
            enrollmentStatItem.setRegionScores(reginScore);
            ret.put(key, enrollmentStatItem);
        });
        return ResponseUtil.success(ret);
    }
}

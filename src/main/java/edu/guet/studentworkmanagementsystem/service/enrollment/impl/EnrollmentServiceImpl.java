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
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserRequest;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentBasic;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.enrollment.EnrollmentMapper;
import edu.guet.studentworkmanagementsystem.network.EnrollmentFeign;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import edu.guet.studentworkmanagementsystem.service.status.StatusService;
import edu.guet.studentworkmanagementsystem.service.student.StudentBasicService;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
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
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.common.Majors.majorName2MajorId;
import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentTableDef.ENROLLMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.PoliticTableDef.POLITIC;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StatusTableDef.STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StudentStatusTableDef.STUDENT_STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;
import static edu.guet.studentworkmanagementsystem.utils.UserUtil.createPassword;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {
    @Autowired
    private EnrollmentFeign enrollmentFeign;
    @Autowired
    private StudentBasicService studentBasicService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private UserService userService;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    @Transactional
    public <T> BaseResponse<T> importEnrollment(ValidateList<Enrollment> enrollments) {
        checkStudentIdOrIdNumberExisted(enrollments);
        insertEnrollment(enrollments);
        insertStudentBasic(enrollments);
        insertStudentEnrollmentStatus(enrollments);
        insertStudentUser(enrollments);
        return ResponseUtil.success();
    }
    /**
     * 检查是否出现重复的身份证号或学号
     * @param enrollments 学籍档案
     */
    @Transactional
    public void checkStudentIdOrIdNumberExisted(List<Enrollment> enrollments) {
        Set<String> idNumberSet = new HashSet<>();
        Set<String> studentIdSet = new HashSet<>();
        for (Enrollment e : enrollments) {
            if (!idNumberSet.add(e.getIdNumber()) || !studentIdSet.add(e.getStudentId())) {
                throw new ServiceException(ServiceExceptionEnum.STUDENT_ID_OR_ID_NUMBER_REPEAT);
            }
        }
        List<Enrollment> existing = QueryChain.of(Enrollment.class)
                .where(ENROLLMENT.ID_NUMBER.in(idNumberSet)
                        .or(ENROLLMENT.STUDENT_ID.in(studentIdSet)))
                .list();
        if (!existing.isEmpty())
            throw new ServiceException(ServiceExceptionEnum.STUDENT_ID_OR_ID_NUMBER_EXISTED);
    }
    /**
     * 添加学籍档案
     * @param enrollments 学籍档案
     */
    @Transactional
    public void insertEnrollment(List<Enrollment> enrollments) {
        int i = mapper.insertBatch(enrollments);
        int size = enrollments.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    /**
     * 添加学生基本信息(该表仅用于关联)
     * @param enrollments 学籍档案
     */
    @Transactional
    public void insertStudentBasic(List<Enrollment> enrollments) {
        ArrayList<StudentBasic> studentBasics = new ArrayList<>();
        enrollments.forEach(it -> {
            StudentBasic studentBasic = createStudentBasic(it);
            studentBasics.add(studentBasic);
        });
        boolean success = studentBasicService.importStudentBasic(studentBasics);
        if (!success)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    /**
     * 添加学籍记录(14, 录入档案)
     * @param enrollments 学籍档案
     */
    @Transactional
    public void insertStudentEnrollmentStatus(ValidateList<Enrollment> enrollments) {
        Set<String> studentIds = enrollments.stream().map(Enrollment::getStudentId).collect(Collectors.toSet());
        boolean success = statusService.enrollmentStudent(studentIds);
        if (!success)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    /**
     * 创建学生用户
     * @param enrollments 学籍档案
     */
    @Transactional
    public void insertStudentUser(ValidateList<Enrollment> enrollments) {
        ArrayList<RegisterUserRequest> registerUsers = new ArrayList<>();
        enrollments.forEach(it -> {
            RegisterUserRequest registerUser = createRegisterUser(it);
            registerUsers.add(registerUser);
        });
        ValidateList<RegisterUserRequest> users = new ValidateList<>(registerUsers);
        userService.addUsers(users);
    }
    /**
     * 辅助方法
     * @param enrollment 学籍档案
     * @return 注册用户格式
     */
    public RegisterUserRequest createRegisterUser(Enrollment enrollment) {
        return RegisterUserRequest.builder()
                .username(enrollment.getStudentId())
                .password(createPassword(enrollment.getIdNumber()))
                .realName(enrollment.getName())
                .email(enrollment.getEmail())
                .phone(enrollment.getPhone())
                .roles(Set.of("5"))
                .build();
    }
    /**
     * 辅助方法
     * @param enrollment 学籍档案
     * @return 学生基本信息
     */
    public StudentBasic createStudentBasic(Enrollment enrollment) {
        return StudentBasic.builder()
                .studentId(enrollment.getStudentId())
                .name(enrollment.getName())
                .gender(enrollment.getGender())
                .idNumber(enrollment.getIdNumber())
                .gradeId(enrollment.getGradeId())
                .degreeId(enrollment.getDegreeId())
                .majorId(enrollment.getMajorId())
                .politicId(enrollment.getPoliticId())
                .phone(enrollment.getPhone())
                .email(enrollment.getEmail())
                .enabled(true)
                .build();
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
                .set(ENROLLMENT.EMAIL, enrollment.getEmail())
                // 在校信息
                .set(ENROLLMENT.HEADER_TEACHER_USERNAME, enrollment.getHeaderTeacherUsername(), StringUtils::hasLength)
                .set(ENROLLMENT.DORMITORY, enrollment.getDormitory())
                .set(ENROLLMENT.CLASS_NO, enrollment.getAddress())
                .set(ENROLLMENT.MAJOR_ID, enrollment.getMajorId(), StringUtils::hasLength)
                .set(ENROLLMENT.GRADE_ID, enrollment.getGradeId(), StringUtils::hasLength)
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
                .set(ENROLLMENT.FOREIGN_LANGUAGE, enrollment.getForeignLanguage())
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
                .set(ENROLLMENT.RELIGIOUS_BELIEFS, enrollment.getReligiousBeliefs())
                // 备注
                .set(ENROLLMENT.OTHER_NOTES, enrollment.getOtherNotes())
                .where(ENROLLMENT.ENROLLMENT_ID.eq(enrollment.getEnrollmentId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        StudentBasic studentBasic = createStudentBasic(enrollment);
        boolean success = studentBasicService.updateStudentBasic(studentBasic);
        if (!success)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteEnrollment(String studentId) {
        updateEnabled(studentId, false);
        boolean success = studentBasicService.deleteStudentBasic(studentId);
        if (!success)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return afterUpdateEnabled(studentId, false);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> recoveryEnrollment(String studentId) {
        updateEnabled(studentId, true);
        boolean success = studentBasicService.recoveryStudentBasic(studentId);
        if (!success)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return afterUpdateEnabled(studentId, true);
    }

    /**
     * 获取学籍档案(等同于获取学生)
     * @param query 查询参数
     * @return 分页好的学生信息
     */
    @Override
    public BaseResponse<Page<EnrollmentItem>> getAllRecords(EnrollmentQuery query) {
        CompletableFuture<Page<EnrollmentItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
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
                    .where(ENROLLMENT.ENABLED.eq(query.getEnabled()))
                    // 个人信息搜索框
                    .and(
                            ENROLLMENT.STUDENT_ID.likeLeft(query.getSearch())
                                    .or(ENROLLMENT.NAME.likeLeft(query.getSearch()))
                                    .or(ENROLLMENT.ID_NUMBER.likeLeft(query.getSearch()))
                                    .or(ENROLLMENT.EMAIL.likeLeft(query.getSearch()))
                                    .or(ENROLLMENT.PHONE.likeLeft(query.getSearch()))
                    )
                    // 家庭信息搜索框
                    .and(
                            ENROLLMENT.FATHER_NAME.likeLeft(query.getFamilySearch())
                                    .or(ENROLLMENT.FATHER_PHONE.likeLeft(query.getFamilySearch()))
                                    .or(ENROLLMENT.FATHER_OCCUPATION.likeLeft(query.getFamilySearch()))
                                    .or(ENROLLMENT.MOTHER_NAME.likeLeft(query.getFamilySearch()))
                                    .or(ENROLLMENT.MOTHER_PHONE.likeLeft(query.getFamilySearch()))
                                    .or(ENROLLMENT.MOTHER_OCCUPATION.likeLeft(query.getFamilySearch()))
                                    .or(ENROLLMENT.GUARDIAN.likeLeft(query.getFamilySearch()))
                                    .or(ENROLLMENT.GUARDIAN_PHONE.likeLeft(query.getFamilySearch()))
                    )
                    .and(ENROLLMENT.HOUSEHOLD_REGISTRATION.likeLeft(query.getHouseholdRegistration()))
                    .and(ENROLLMENT.HOUSEHOLD_TYPE.eq(query.getHouseholdType()))
                    .and(ENROLLMENT.IS_ONLY_CHILD.eq(query.getIsOnlyChild()))
                    // 在校信息搜索框
                    .and(
                            ENROLLMENT.DORMITORY.likeLeft(query.getSchoolSearch())
                                    .or(ENROLLMENT.CLASS_NO.likeLeft(query.getSchoolSearch()))
                    )
                    // 班主任信息搜索框
                    .and(
                            USER.USERNAME.likeLeft(query.getHeaderTeacherSearch())
                                    .or(USER.REAL_NAME.likeLeft(query.getHeaderTeacherSearch()))
                                    .or(USER.PHONE.likeLeft(query.getHeaderTeacherSearch()))
                    )
                    // 中学信息搜索
                    .and(
                            ENROLLMENT.HIGH_SCHOOL_NAME.likeLeft(query.getHighSchoolSearch())
                                    .or(ENROLLMENT.HIGH_SCHOOL_CODE.likeLeft(query.getHighSchoolSearch()))
                    )
                    .and(ENROLLMENT.CANDIDATE_CATEGORY.likeLeft(query.getCandidateCategory()))
                    .and(ENROLLMENT.STUDENT_TYPE.likeLeft(query.getStudentType()))
                    .and(ENROLLMENT.ADMISSION_BATCH.likeLeft(query.getAdmissionBatch()))
                    // 专业
                    .and(ENROLLMENT.MAJOR_ID.eq(query.getMajorId()))
                    // 年级
                    .and(ENROLLMENT.GRADE_ID.eq(query.getGradeId()))
                    // 培养层次
                    .and(ENROLLMENT.DEGREE_ID.eq(query.getDegreeId()))
                    // 政治面貌
                    .and(ENROLLMENT.POLITIC_ID.eq(query.getPoliticId()))
                    // 学籍状态
                    .and(STUDENT_STATUS.STATUS_ID.eq(query.getStatusId()))
                    // 是否残疾
                    .and(ENROLLMENT.DISABILITY.eq(query.getDisability()))
                    // 助学贷款
                    .and(ENROLLMENT.STUDENT_LOANS.eq(query.getStudentLoans()))
                    // 宗教信仰
                    .and(ENROLLMENT.RELIGIOUS_BELIEFS.eq(query.getReligiousBeliefs()))
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

    @Transactional
    public <T> BaseResponse<T> afterUpdateEnabled(String studentId, boolean enabled) {
        User user = QueryChain.of(User.class)
                .where(USER.USERNAME.eq(studentId))
                .one();
        if (Objects.isNull(user))
            return ResponseUtil.success();
        return enabled ? userService.recoveryUser(user.getUid()) : userService.deleteUser(user.getUid());
    }

    @Transactional
    public void updateEnabled(String studentId, boolean enabled) {
        boolean update = UpdateChain.of(Enrollment.class)
                .set(ENROLLMENT.ENABLED, enabled)
                .where(ENROLLMENT.STUDENT_ID.eq(studentId))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}

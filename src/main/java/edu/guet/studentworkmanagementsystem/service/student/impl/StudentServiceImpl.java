package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUser;
import edu.guet.studentworkmanagementsystem.entity.po.student.*;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatusItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentTableItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.other.DegreeMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.GradeMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.PoliticMapper;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentMapper;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import edu.guet.studentworkmanagementsystem.service.student.StudentBasicService;
import edu.guet.studentworkmanagementsystem.service.student.StudentDetailService;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.PoliticTableDef.POLITIC;
import static edu.guet.studentworkmanagementsystem.entity.po.status.table.StatusTableDef.STATUS;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentDetailTableDef.STUDENT_DETAIL;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RoleTableDef.ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserRoleTableDef.USER_ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Qualifier("readThreadPool")
    private ThreadPoolTaskExecutor readThreadPool;
    @Autowired
    private StudentBasicService studentBasicService;
    @Autowired
    private StudentDetailService studentDetailService;
    @Autowired
    private OtherService otherService;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private DegreeMapper degreeMapper;
    @Autowired
    private PoliticMapper politicMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudent(ValidateList<Student> students) {
        students.forEach(it -> it.setEnabled(true));
        checkStudentIdOrIdNumberExisted(students);
        insertStudentBasicBatch(students);
        insertStudentDetailBatch(students);
        insertUserBatch(students);
        return ResponseUtil.success();
    }
    /**
     * 添加学生
     */
    @Override
    @Transactional
    public <T> BaseResponse<T> addStudent(Student student) {
        ValidateList<Student> students = new ValidateList<>(student);
        return importStudent(students);
    }
    /**
     * 检查学号或身份证号是否重复
     */
    @Transactional
    public void checkStudentIdOrIdNumberExisted(List<Student> students) {
        int size = students.size();
        Set<String> idNumberSet = students.stream()
                .map(Student::getIdNumber)
                .collect(Collectors.toSet());
        Set<String> studentIdSet = students.stream()
                .map(Student::getStudentId)
                .collect(Collectors.toSet());
        if ((idNumberSet.size() != size) || (studentIdSet.size() != size))
            throw new ServiceException(ServiceExceptionEnum.STUDENT_ID_OR_ID_NUMBER_REPEAT);
        Set<String> dbIdNumberSet = QueryChain.of(StudentBasic.class)
                .where(STUDENT_BASIC.ID_NUMBER.in(idNumberSet))
                .list()
                .stream()
                .map(StudentBasic::getIdNumber)
                .collect(Collectors.toSet());
        Set<String> dbStudentIdSet = QueryChain.of(StudentBasic.class)
                .where(STUDENT_BASIC.STUDENT_ID.in(studentIdSet))
                .list()
                .stream()
                .map(StudentBasic::getStudentId)
                .collect(Collectors.toSet());
        if (!dbIdNumberSet.isEmpty() || !dbStudentIdSet.isEmpty())
            throw new ServiceException(ServiceExceptionEnum.STUDENT_ID_OR_ID_NUMBER_EXISTED);
    }
    /**
     *  插入学生基础信息
     */
    public StudentBasic createStudentBasic(Student student) {
        return StudentBasic.builder()
                .studentId(student.getStudentId())
                .idNumber(student.getIdNumber())
                .name(student.getName())
                .gender(student.getGender())
                .phone(student.getPhone())
                .email(student.getEmail())
                .degreeId(student.getDegreeId())
                .enabled(student.getEnabled())
                .build();
    }
    public List<StudentBasic> createStudentBasics(List<Student> students) {
        ArrayList<StudentBasic> studentBasics = new ArrayList<>();
        for (Student student : students) {
            studentBasics.add(createStudentBasic(student));
        }
        return studentBasics;
    }
    public void insertStudentBasicBatch(ValidateList<Student> students) {
        List<StudentBasic> studentBasics = createStudentBasics(students);
        boolean studentBasicInsertSuccess = studentBasicService.importStudentBasic(studentBasics);
        if (!studentBasicInsertSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    /**
     * 插入学生详细信息
     */
    public StudentDetail createStudentDetail(Student student) {
        return StudentDetail.builder()
                .studentId(student.getStudentId())
                .headerTeacherUsername(student.getHeaderTeacherUsername())
                .majorId(student.getMajorId())
                .nativePlace(student.getNativePlace())
                .postalCode(student.getPostalCode())
                .nation(student.getNation())
                .politicId(student.getPoliticId())
                .gradeId(student.getGradeId())
                .classNo(student.getClassNo())
                .dormitory(student.getDormitory())
                .birthdate(student.getBirthdate())
                .householdRegistration(student.getHouseholdRegistration())
                .householdType(student.getHouseholdType())
                .address(student.getAddress())
                .fatherName(student.getFatherName())
                .fatherPhone(student.getFatherPhone())
                .fatherOccupation(student.getFatherOccupation())
                .motherName(student.getMotherName())
                .motherPhone(student.getMotherPhone())
                .motherOccupation(student.getMotherOccupation())
                .guardian(student.getGuardian())
                .guardianPhone(student.getGuardianPhone())
                .highSchool(student.getHighSchool())
                .examId(student.getExamId())
                .admissionBatch(student.getAdmissionBatch())
                .totalExamScore(student.getTotalExamScore())
                .foreignLanguage(student.getForeignLanguage())
                .foreignScore(student.getForeignScore())
                .hobbies(student.getHobbies())
                .joiningTime(student.getJoiningTime())
                .isStudentLoans(student.getIsStudentLoans())
                .height(student.getHeight())
                .weight(student.getWeight())
                .religiousBeliefs(student.getReligiousBeliefs())
                .familyPopulation(student.getFamilyPopulation())
                .isOnlyChild(student.getIsOnlyChild())
                .location(student.getLocation())
                .disability(student.getDisability())
                .statusId(student.getStatusId())
                .politicId(student.getPoliticId())
                .otherNotes(student.getOtherNotes())
                .build();
    }
    public List<StudentDetail> createStudentDetails(List<Student> students) {
        ArrayList<StudentDetail> studentDetails = new ArrayList<>();
        for (Student student : students) {
            studentDetails.add(createStudentDetail(student));
        }
        return studentDetails;
    }
    public void insertStudentDetailBatch(ValidateList<Student> students) {
        List<StudentDetail> studentDetails = createStudentDetails(students);
        boolean studentDetailInsertSuccess = studentDetailService.importStudentDetail(studentDetails);
        if (!studentDetailInsertSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    /**
     * 注册学生身份用户
     */
    public RegisterUser createRegisterUser(Student student) {
        return RegisterUser.builder()
                .username(student.getStudentId())
                .password(createPassword(student.getIdNumber()))
                .realName(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .roles(Set.of("5"))
                .build();
    }
    public List<RegisterUser> createRegisterUsers(List<Student> students) {
        ArrayList<RegisterUser> registerUsers = new ArrayList<>();
        students.forEach(student -> {
            RegisterUser user = createRegisterUser(student);
            registerUsers.add(user);
        });
        return registerUsers;
    }
    public void insertUserBatch(ValidateList<Student> students) {
        List<RegisterUser> registerUsers = createRegisterUsers(students);
        ValidateList<RegisterUser> validateRegisterUsers = new ValidateList<>(registerUsers);
        userService.addUsers(validateRegisterUsers);
    }

    @Override
    public BaseResponse<Page<StudentTableItem>> getStudents(StudentQuery query) {
        CompletableFuture<BaseResponse<Page<StudentTableItem>>> future =
                CompletableFuture.supplyAsync(() -> ResponseUtil.success(getStudentTableItems(query)), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }
    public Page<StudentTableItem> getStudentTableItems(StudentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        return QueryChain.of(StudentBasic.class)
                .select(
                        STUDENT_BASIC.ALL_COLUMNS,
                        STUDENT_DETAIL.ALL_COLUMNS,
                        MAJOR.ALL_COLUMNS,
                        GRADE.ALL_COLUMNS,
                        DEGREE.ALL_COLUMNS,
                        STATUS.ALL_COLUMNS,
                        POLITIC.ALL_COLUMNS,
                        USER.USERNAME.as("headerTeacherUsername"),
                        USER.REAL_NAME.as("headerTeacherName"),
                        USER.PHONE.as("headerTeacherPhone")
                )
                .from(STUDENT_BASIC)
                .innerJoin(STUDENT_DETAIL).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_DETAIL.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT_DETAIL.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .innerJoin(USER).on(STUDENT_DETAIL.HEADER_TEACHER_USERNAME.eq(USER.USERNAME))
                .innerJoin(GRADE).on(STUDENT_DETAIL.GRADE_ID.eq(GRADE.GRADE_ID))
                .innerJoin(DEGREE).on(STUDENT_BASIC.DEGREE_ID.eq(DEGREE.DEGREE_ID))
                .innerJoin(STATUS).on(STUDENT_DETAIL.STATUS_ID.eq(STATUS.STATUS_ID))
                .innerJoin(POLITIC).on(STUDENT_DETAIL.POLITIC_ID.eq(POLITIC.POLITIC_ID))
                .where(STUDENT_BASIC.ENABLED.eq(query.getEnabled()))
                .and(STUDENT_BASIC.DEGREE_ID.eq(query.getDegreeId()))
                .and(STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch())
                        .or(STUDENT_BASIC.NAME.like(query.getSearch()))
                        .or(STUDENT_BASIC.ID_NUMBER.like(query.getSearch()))
                        .or(STUDENT_BASIC.EMAIL.like(query.getSearch()))
                        .or(STUDENT_BASIC.PHONE.like(query.getSearch()))
                        .or(STUDENT_DETAIL.FATHER_NAME.like(query.getSearch()))
                        .or(STUDENT_DETAIL.FATHER_PHONE.like(query.getSearch()))
                        .or(STUDENT_DETAIL.MOTHER_NAME.like(query.getSearch()))
                        .or(STUDENT_DETAIL.MOTHER_PHONE.like(query.getSearch()))
                        .or(STUDENT_DETAIL.GUARDIAN.like(query.getSearch()))
                        .or(STUDENT_DETAIL.GUARDIAN_PHONE.like(query.getSearch()))
                )
                .and(STUDENT_DETAIL.GRADE_ID.eq(query.getGradeId()))
                .and(STUDENT_BASIC.GENDER.eq(query.getGender()))
                .and(STUDENT_DETAIL.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT_DETAIL.STATUS_ID.eq(query.getStatusId()))
                .and(STUDENT_DETAIL.NATIVE_PLACE.like(query.getNativePlace()))
                .and(STUDENT_DETAIL.NATION.like(query.getNation()))
                .and(STUDENT_DETAIL.POLITIC_ID.eq(query.getPoliticId()))
                .and(STUDENT_DETAIL.CLASS_NO.eq(query.getClassNo()))
                .and(STUDENT_DETAIL.DORMITORY.eq(query.getDormitory()))
                .and(STUDENT_DETAIL.HOUSEHOLD_REGISTRATION.likeLeft(query.getHouseholdRegistration()))
                .and(STUDENT_DETAIL.HOUSEHOLD_TYPE.eq(query.getHouseholdType()))
                .and(STUDENT_DETAIL.ADDRESS.likeLeft(query.getAddress()))
                .and(STUDENT_DETAIL.EXAM_ID.eq(query.getExamId()))
                .and(STUDENT_DETAIL.HIGH_SCHOOL.like(query.getHighSchool()))
                .and(STUDENT_DETAIL.ADMISSION_BATCH.like(query.getAdmissionBatch()))
                .and(STUDENT_DETAIL.TOTAL_EXAM_SCORE.eq(query.getTotalExamScore()))
                .and(STUDENT_DETAIL.FOREIGN_LANGUAGE.like(query.getForeignLanguage()))
                .and(STUDENT_DETAIL.FOREIGN_SCORE.eq(query.getForeignScore()))
                .and(STUDENT_DETAIL.HOBBIES.like(query.getHobbies()))
                .and(STUDENT_DETAIL.OTHER_NOTES.like(query.getOtherNotes()))
                .and(STUDENT_DETAIL.IS_STUDENT_LOANS.eq(query.getIsStudentLoans()))
                .and(STUDENT_DETAIL.RELIGIOUS_BELIEFS.like(query.getReligiousBeliefs()))
                .and(STUDENT_DETAIL.FAMILY_POPULATION.eq(query.getFamilyPopulation()))
                .and(STUDENT_DETAIL.IS_ONLY_CHILD.eq(query.getIsOnlyChild()))
                .and(STUDENT_DETAIL.LOCATION.like(query.getLocation()))
                .and(STUDENT_DETAIL.DISABILITY.eq(query.getDisability()))
                .pageAs(Page.of(pageNo, pageSize), StudentTableItem.class);
    }

    @Override
    public BaseResponse<StudentArchive> getStudentArchive(String studentId) {
        // todo: 总学生档案, 包含系统中所有关于学生的信息
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudent(Student student) {
        updateStudentBasic(student);
        updateStudentDetail(student);
        return ResponseUtil.success();
    }
    /**
     * 删除 恢复学生
     */
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudent(String studentId) {
        boolean isSuccess = studentBasicService.deleteStudentBasic(studentId);
        if (!isSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return afterUpdateStudentEnabled(studentId, false);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> recoveryStudent(String studentId) {
        boolean isSuccess = studentBasicService.recoveryStudentBasic(studentId);
        if (!isSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return afterUpdateStudentEnabled(studentId, true);
    }

    @Override
    public BaseResponse<List<StudentStatusItem>> getStudentStatus(StudentStatusQuery query) {
        CompletableFuture<List<StudentStatusItem>> future = CompletableFuture.supplyAsync(()-> getAllStudent(query), readThreadPool);
        List<StudentStatusItem> list = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(list);
    }

    @Override
    public BaseResponse<List<HeaderTeacher>> getHeaderTeachers() {
        CompletableFuture<List<HeaderTeacher>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(User.class)
                .select(
                        USER.USERNAME.as("headerTeacherUsername"),
                        USER.REAL_NAME.as("headerTeacherRealName"),
                        USER.PHONE.as("headerTeacherPhone")
                )
                .innerJoin(USER_ROLE).on(USER.UID.eq(USER_ROLE.UID))
                .innerJoin(ROLE).on(ROLE.RID.eq(USER_ROLE.RID))
                .where(ROLE.ROLE_NAME.like(Common.HEADER_TEACHER.getValue()))
                .listAs(HeaderTeacher.class), readThreadPool);
        List<HeaderTeacher> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public List<StudentStatusItem> getAllStudent(StudentStatusQuery query) {
        return studentMapper.getStudentStatusList(query);
    }

    /**
     * 删除 恢复学生后的的必要操作
     */
    public User findUser(String username) {
        return QueryChain.of(User.class)
                .where(USER.USERNAME.eq(username))
                .one();
    }
    @Transactional
    public <T> BaseResponse<T> afterUpdateStudentEnabled(String studentId, boolean enabled) {
        User user = findUser(studentId);
        if (Objects.isNull(user))
            return ResponseUtil.success();
        return enabled ? userService.recoveryUser(user.getUid()) : userService.deleteUser(user.getUid());
    }
    /**
     * 创建用户密码，默认为身份证后六位
     */
    public String createPassword(String idNumber) {
        return idNumber.substring(idNumber.length() - 6);
    }
    /**
     * 检查是否有StudentBasic的属性
     */
    @Transactional
    public void updateStudentBasic(Student student) {
        StudentBasic studentBasic = createStudentBasic(student);
        boolean flag = studentBasicService.updateStudentBasic(studentBasic);
        if (!flag)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    /**
     * 检查是否有StudentDetail的属性
     */
    @Transactional
    public void updateStudentDetail(Student student) {
        StudentDetail studentDetail = createStudentDetail(student);
        boolean flag = studentDetailService.updateStudentDetail(studentDetail);
        if (!flag)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}

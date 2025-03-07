package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUser;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentBasic;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentDetail;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentTableItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentMapper;
import edu.guet.studentworkmanagementsystem.service.student.StudentBasicService;
import edu.guet.studentworkmanagementsystem.service.student.StudentDetailService;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentDetailTableDef.STUDENT_DETAIL;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;
    @Autowired
    private StudentBasicService studentBasicService;
    @Autowired
    private StudentDetailService studentDetailService;

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
        Set<String> dbIdNumberSet = QueryChain.of(Student.class)
                .where(STUDENT.ID_NUMBER.in(idNumberSet))
                .list()
                .stream()
                .map(Student::getIdNumber)
                .collect(Collectors.toSet());
        Set<String> dbStudentIdSet = QueryChain.of(Student.class)
                .where(STUDENT.STUDENT_ID.in(studentIdSet))
                .list()
                .stream()
                .map(Student::getStudentId)
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
                .headTeacherUsername(student.getHeadTeacherUsername())
                .majorId(student.getMajorId())
                .nativePlace(student.getNativePlace())
                .postalCode(student.getPostalCode())
                .nation(student.getNation())
                .politicsStatus(student.getPoliticsStatus())
                .grade(student.getGrade())
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
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception exception) {
            Throwable cause = exception.getCause();
            switch (cause) {
                case ServiceException serviceException ->
                        throw serviceException;
                case TimeoutException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_TIMEOUT);
                case InterruptedException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_INTERRUPTED);
                default ->
                        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        }
    }
    private Page<StudentTableItem> getStudentTableItems(StudentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        return QueryChain.of(StudentBasic.class)
                .select(STUDENT_BASIC.ALL_COLUMNS, STUDENT_DETAIL.ALL_COLUMNS, MAJOR.ALL_COLUMNS, USER.ALL_COLUMNS)
                .from(STUDENT_BASIC)
                .innerJoin(STUDENT_DETAIL).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_DETAIL.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT_DETAIL.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .innerJoin(USER).on(STUDENT_DETAIL.HEAD_TEACHER_USERNAME.eq(USER.USERNAME))
                .where(STUDENT_BASIC.ENABLED.eq(query.getEnabled()))
                .and(STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch())
                        .or(STUDENT_BASIC.NAME.likeLeft(query.getSearch()))
                        .or(STUDENT_BASIC.ID_NUMBER.likeLeft(query.getSearch()))
                        .or(STUDENT_BASIC.EMAIL.likeLeft(query.getSearch()))
                        .or(STUDENT_DETAIL.FATHER_NAME.likeLeft(query.getSearch()))
                        .or(STUDENT_DETAIL.FATHER_PHONE.likeLeft(query.getSearch()))
                        .or(STUDENT_DETAIL.MOTHER_NAME.likeLeft(query.getSearch()))
                        .or(STUDENT_DETAIL.MOTHER_PHONE.likeLeft(query.getSearch()))
                        .or(STUDENT_DETAIL.GUARDIAN.likeLeft(query.getSearch()))
                        .or(STUDENT_DETAIL.GUARDIAN_PHONE.likeLeft(query.getSearch()))
                )
                .and(STUDENT_BASIC.GENDER.eq(query.getGender()))
                .and(STUDENT_DETAIL.NATIVE_PLACE.like(query.getNativePlace()))
                .and(STUDENT_DETAIL.GRADE.eq(query.getGrade()))
                .and(STUDENT_DETAIL.NATION.like(query.getNation()))
                .and(STUDENT_DETAIL.POLITICS_STATUS.like(query.getPoliticsStatus()))
                .and(STUDENT_DETAIL.POSTAL_CODE.like(query.getPostalCode()))
                .and(STUDENT_DETAIL.CLASS_NO.like(query.getClassNo()))
                .and(STUDENT_DETAIL.DORMITORY.like(query.getDormitory()))
                .and(STUDENT_DETAIL.BIRTHDATE.eq(query.getBirthdate()))
                .and(STUDENT_DETAIL.HOUSEHOLD_REGISTRATION.like(query.getHouseholdRegistration()))
                .and(STUDENT_DETAIL.HOUSEHOLD_TYPE.like(query.getHouseholdType()))
                .and(STUDENT_DETAIL.ADDRESS.like(query.getAddress()))
                .and(STUDENT_DETAIL.EXAM_ID.eq(query.getExamId()))
                .and(STUDENT_DETAIL.HIGH_SCHOOL.like(query.getHighSchool()))
                .and(STUDENT_DETAIL.ADMISSION_BATCH.like(query.getAdmissionBatch()))
                .and(STUDENT_DETAIL.TOTAL_EXAM_SCORE.eq(query.getTotalExamScore()))
                .and(STUDENT_DETAIL.FOREIGN_LANGUAGE.like(query.getForeignLanguage()))
                .and(STUDENT_DETAIL.FOREIGN_SCORE.eq(query.getForeignScore()))
                .and(STUDENT_DETAIL.HOBBIES.like(query.getHobbies()))
                .and(STUDENT_DETAIL.OTHER_NOTES.like(query.getOtherNotes()))
                .pageAs(Page.of(pageNo, pageSize), StudentTableItem.class);
    }

    @Override
    public BaseResponse<StudentArchive> getStudent(String studentId) {
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
    /**
     * 用于添加学生前校验班主任是否存在
     */
    @Override
    public <T> BaseResponse<T> validateHeadTeacherExists(String headTeacherUsername) {
        User target = QueryChain.of(User.class)
                .where(USER.USERNAME.eq(headTeacherUsername))
                .one();
        if (Objects.isNull(target))
            throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
        return ResponseUtil.success();
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

package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUser;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentBasic;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentDetail;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentVO;
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
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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
        preImportStudent(students);
        List<StudentBasic> studentBasics = createStudentBasics(students);
        boolean studentBasicInsertSuccess = studentBasicService.importStudentBasic(studentBasics);
        if (!studentBasicInsertSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        List<StudentDetail> studentDetails = createStudentDetails(students);
        boolean studentDetailInsertSuccess = studentDetailService.importStudentDetail(studentDetails);
        if (!studentDetailInsertSuccess)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        List<RegisterUser> registerUsers = createRegisterUsers(students);
        ValidateList<RegisterUser> validateRegisterUsers = new ValidateList<>();
        validateRegisterUsers.setList(registerUsers);
        userService.addUsers(validateRegisterUsers);
        return ResponseUtil.success();
    }
    /**
     * 检查学号或身份证号是否重复
     * @param students 学生列表
     */
    @Transactional
    public void preImportStudent(List<Student> students) {
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
     * 将原始Student转化成StudentBasic
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
    /**
     * 将原始Student转化成StudentDetail
     */
    public StudentDetail createStudentDetail(Student student) {
        return StudentDetail.builder()
                .studentId(student.getStudentId())
                .headTeacherId(student.getHeadTeacherId())
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

    public List<RegisterUser> createRegisterUsers(List<Student> students) {
        ArrayList<RegisterUser> registerUsers = new ArrayList<>();
        students.forEach(student -> {
            RegisterUser user = createRegisterUser(student);
            registerUsers.add(user);
        });
        return registerUsers;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addStudent(Student student) {
        ValidateList<Student> students = new ValidateList<>();
        students.add(student);
        return importStudent(students);
    }

    private RegisterUser createRegisterUser(Student student) {
        return RegisterUser.builder()
                .username(student.getStudentId())
                .password(createPassword(student.getIdNumber()))
                .realName(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .roles(Set.of("5"))
                .build();
    }

    @Override
    public BaseResponse<Page<StudentVO>> getStudents(StudentQuery query) {
        CompletableFuture<BaseResponse<Page<StudentVO>>> future = CompletableFuture.supplyAsync(() -> {
            Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
            Page<StudentVO> studentPage = QueryChain.of(Student.class)
                    .select(STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                    .from(STUDENT).innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                    .and(STUDENT.STUDENT_ID.like(query.getSearch()).or(STUDENT.NAME.like(query.getSearch())))
                    .and(Student::getNativePlace).like(query.getNativePlace())
                    .and(Student::getNation).like(query.getNation())
                    .and(Student::getGender).eq(query.getGender())
                    .and(Student::getMajorId).eq(query.getMajorId())
                    .and(Student::getPoliticsStatus).eq(query.getPoliticsStatus())
                    .and(Student::getGrade).eq(query.getGrade())
                    .and(STUDENT.ENABLED.eq(query.getEnabled()))
                    .pageAs(Page.of(pageNo, pageSize), StudentVO.class);
            return ResponseUtil.success(studentPage);
        }, readThreadPool);
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

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudent(StudentDTO studentDTO) {
        boolean update = UpdateChain.of(Student.class)
                .set(Student::getName, studentDTO.getName(), StringUtils.hasLength(studentDTO.getName()))
                .set(Student::getIdNumber, studentDTO.getIdNumber(), StringUtils.hasLength(studentDTO.getIdNumber()))
                .set(Student::getGender, studentDTO.getGender(), StringUtils.hasLength(studentDTO.getGender()))
                .set(Student::getPostalCode, studentDTO.getPostalCode(), StringUtils.hasLength(studentDTO.getPostalCode()))
                .set(Student::getNativePlace, studentDTO.getNativePlace(), StringUtils.hasLength(studentDTO.getNativePlace()))
                .set(Student::getPhone, studentDTO.getPhone(), StringUtils.hasLength(studentDTO.getPhone()))
                .set(Student::getMajorId, studentDTO.getMajorId(), StringUtils.hasLength(studentDTO.getMajorId()))
                .set(Student::getGrade, studentDTO.getGrade(), StringUtils.hasLength(studentDTO.getGrade()))
                .set(Student::getClassNo, studentDTO.getClassNo(), StringUtils.hasLength(studentDTO.getClassNo()))
                .set(Student::getPoliticsStatus, studentDTO.getPoliticsStatus(), StringUtils.hasLength(studentDTO.getPoliticsStatus()))
                .where(Student::getStudentId).eq(studentDTO.getStudentId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudent(String studentId) {
        boolean i = UpdateChain.of(Student.class)
                .set(STUDENT.ENABLED, false)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .update();
        User one = findUser(studentId);
        if (!Objects.isNull(one)) {
            int code = userService.deleteUser(one.getUid()).getCode();
            if (code == 200)
                return ResponseUtil.success();
        }
        if (i)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public <T> BaseResponse<T> recoveryStudent(String studentId) {
        boolean update = UpdateChain.of(Student.class)
                .set(STUDENT.ENABLED, true)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .update();
        User one = findUser(studentId);
        if (!Objects.isNull(one)) {
            int code = userService.recoveryUser(one.getUid()).getCode();
            if (code == 200)
                return ResponseUtil.success();
        }
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public <T> BaseResponse<T> validateHeadTeacherExists(String headTeacherName, String headTeacherPhone) {
        User target = QueryChain.of(User.class)
                .where(USER.REAL_NAME.eq(headTeacherName))
                .and(USER.PHONE.eq(headTeacherPhone))
                .one();
        if (Objects.isNull(target))
            throw new ServiceException(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
        return ResponseUtil.success();
    }

    private User findUser(String studentId) {
        return QueryChain.of(User.class)
                .where(USER.USERNAME.eq(studentId))
                .one();
    }

    private String createPassword(String idNumber) {
        return idNumber.substring(idNumber.length() - 6);
    }
}

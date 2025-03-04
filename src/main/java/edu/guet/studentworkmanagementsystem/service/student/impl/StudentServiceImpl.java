package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTOList;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
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
        int i = mapper.insertBatch(students);
        if (i != students.size())
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        RegisterUserDTOList registerUserList = createRegisterUserList(students);
        return userService.addUsers(registerUserList);
    }

    /**
     * 检查学号或身份证号是否重复
     * @param students 学生列表
     */
    @Transactional
    public void preImportStudent(List<Student> students) {
        Set<String> idNumberSet = students.stream().map(Student::getIdNumber).collect(Collectors.toSet());
        Set<String> studentIdSet = students.stream().map(Student::getStudentId).collect(Collectors.toSet());
        if ((idNumberSet.size() != students.size()) || (studentIdSet.size() != students.size()))
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

    public RegisterUserDTOList createRegisterUserList(List<Student> students) {
        ArrayList<RegisterUserDTO> registerUserDTO = new ArrayList<>();
        RegisterUserDTOList registerUserDTOList = new RegisterUserDTOList();
        students.forEach(student -> {
            RegisterUserDTO user = createUser(student.getStudentId(), student.getName(), createPassword(student.getIdNumber()), student.getEmail(), student.getPhone());
            registerUserDTO.add(user);
        });
        registerUserDTOList.setRegisterUserDTOList(registerUserDTO);
        return registerUserDTOList;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addStudent(Student student) {
        String studentId = student.getStudentId();
        student.setEnabled(true);
        Student one = QueryChain.of(Student.class)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .or(STUDENT.ID_NUMBER.eq(student.getIdNumber()))
                .one();
        if (!Objects.isNull(one))
            throw new ServiceException(ServiceExceptionEnum.STUDENT_ID_OR_ID_NUMBER_EXISTED);
        return createStudentAndUser(student);
    }

    @Transactional
    public <T> BaseResponse<T> createStudentAndUser(Student student) {
        int i = mapper.insert(student);
        if (i > 0) {
            RegisterUserDTO user = createUser(
                    student.getStudentId(),
                    student.getName(),
                    createPassword(student.getIdNumber()),
                    student.getEmail(),
                    student.getPhone()
            );
            return userService.addUser(user);
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    private RegisterUserDTO createUser(String studentId, String name, String password, String email, String phone) {
        return new RegisterUserDTO(studentId, name, email, password, phone, List.of("5"));
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

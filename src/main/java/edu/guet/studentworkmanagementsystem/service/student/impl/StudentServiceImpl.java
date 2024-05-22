package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
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
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudent(StudentList studentList) {
        List<Student> students = studentList.getStudents();
        int i = mapper.insertBatch(students);
        if (i == students.size()) {
            ArrayList<RegisterUserDTO> registerUserDTOS = new ArrayList<>();
            students.forEach(item -> {
                RegisterUserDTO user = createUser(item.getStudentId(), item.getName(), createPassword(item.getIdNumber()));
                registerUserDTOS.add(user);
            });
            return userService.addUsers(new RegisterUserDTOList(registerUserDTOS));
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> addStudent(Student student) {
        String studentId = student.getStudentId();
        Student one = QueryChain.of(Student.class)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .one();
        if (!Objects.isNull(one))
            return enableAndUpdateStudentWithAccount(student);
        else
            return createStudentAndUser(student);
    }
    @Transactional
    public <T> BaseResponse<T> createStudentAndUser(Student student) {
        int i = mapper.insert(student);
        if (i > 0) {
            RegisterUserDTO user = createUser(student.getStudentId(), student.getName(), createPassword(student.getIdNumber()));
            return userService.addUser(user);
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    public <T> BaseResponse<T> enableAndUpdateStudentWithAccount(Student student) {
        boolean i = UpdateChain.of(Student.class)
                .set(Student::getName, student.getName(), StringUtils.hasLength(student.getName()))
                .set(Student::getIdNumber, student.getIdNumber(), StringUtils.hasLength(student.getIdNumber()))
                .set(Student::getGender, student.getGender(), StringUtils.hasLength(student.getGender()))
                .set(Student::getPostalCode, student.getPostalCode(), StringUtils.hasLength(student.getPostalCode()))
                .set(Student::getNativePlace, student.getNativePlace(), StringUtils.hasLength(student.getNativePlace()))
                .set(Student::getPhone, student.getPhone(), StringUtils.hasLength(student.getPhone()))
                .set(Student::getMajorId, student.getMajorId(), StringUtils.hasLength(student.getMajorId()))
                .set(Student::getGrade, student.getGrade(), StringUtils.hasLength(student.getGrade()))
                .set(Student::getClassNo, student.getClassNo(), StringUtils.hasLength(student.getClassNo()))
                .set(Student::getPoliticsStatus, student.getPoliticsStatus(), StringUtils.hasLength(student.getPoliticsStatus()))
                .set(STUDENT.ENABLED, true)
                .where(STUDENT.STUDENT_ID.eq(student.getStudentId()))
                .update();
        boolean j = UpdateChain.of(User.class)
                .set(User::getPassword, passwordEncoder.encode(createPassword(student.getIdNumber())), StringUtils::hasLength)
                .set(User::getRealName, student.getName(), StringUtils::hasLength)
                .set(User::getEmail, student.getStudentId(), StringUtils::hasLength)
                .set(User::isEnabled, true)
                .where(User::getUsername).eq(student.getStudentId())
                .update();
        if (i && j)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    private RegisterUserDTO createUser(String studentId, String name, String password) {
        return new RegisterUserDTO(studentId, name, studentId, password, List.of("5"));
    }
    @Override
    public BaseResponse<Page<StudentVO>> getStudents(StudentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentVO> studentPage = QueryChain.of(Student.class)
                .select(STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT).innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                .where(Student::getEnabled).eq(true)
                .and(STUDENT.STUDENT_ID.like(query.getSearch()).or(STUDENT.NAME.like(query.getSearch())))
                .and(Student::getNativePlace).like(query.getNativePlace())
                .and(Student::getNation).like(query.getNation())
                .and(Student::getGender).eq(query.getGender())
                .and(Student::getMajorId).eq(query.getMajorId())
                .and(Student::getPoliticsStatus).eq(query.getPoliticsStatus())
                .and(Student::getGrade).eq(query.getGrade())
                .pageAs(Page.of(pageNo, pageSize), StudentVO.class);
        return ResponseUtil.success(studentPage);
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
        User one = QueryChain.of(User.class)
                .where(USER.USERNAME.eq(studentId))
                .one();
        int code = userService.deleteUser(one.getUid()).getCode();
        if (code != 200)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        boolean i = UpdateChain.of(Student.class)
                .set(STUDENT.ENABLED, false)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .update();
        if (i)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    private String createPassword(String idNumber) {
        return idNumber.substring(idNumber.length() - 6);
    }
}

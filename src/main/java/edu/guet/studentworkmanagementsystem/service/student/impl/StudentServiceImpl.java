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
import java.util.Optional;

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
                String password = passwordEncoder.encode(createPassword(item.getIdNumber()));
                RegisterUserDTO registerUserDTO = new RegisterUserDTO(item.getStudentId(), item.getName(), item.getStudentId(), password, null);
                registerUserDTOS.add(registerUserDTO);
            });
            return userService.addUsers(new RegisterUserDTOList(registerUserDTOS));
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> addStudent(Student student) {
        int i = mapper.insert(student);
        if (i > 0) {
            String password = passwordEncoder.encode(createPassword(student.getIdNumber()));
            RegisterUserDTO registerUserDTO = new RegisterUserDTO(student.getStudentId(), student.getName(), student.getStudentId(), password, null);
            return userService.addUser(registerUserDTO);
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentVO>> getStudents(StudentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentVO> studentPage = QueryChain.of(Student.class)
                .select(STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT).innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                .where(Student::getName).like(query.getName())
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
        int code = userService.deleteUser(studentId).getCode();
        if (code != 200)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        int i = mapper.deleteById(studentId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    private String createPassword(String idNumber) {
        return idNumber.substring(idNumber.length() - 6);
    }
}

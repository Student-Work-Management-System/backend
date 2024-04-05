package edu.guet.studentworkmanagementsystem.service.employment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.StudentEmploymentDTO;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.employment.StudentEmploymentMapper;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.entity.po.employment.table.StudentEmploymentTableDef.STUDENT_EMPLOYMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class EmploymentServiceImpl extends  ServiceImpl<StudentEmploymentMapper, StudentEmployment> implements EmploymentService {

    @Autowired
    private StudentEmploymentMapper studentEmploymentMapper;
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentEmployment(List<StudentEmploymentDTO> studentEmploymentDTOList) {
        List<StudentEmployment> studentEmploymentList = studentEmploymentDTOList.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        System.out.println(studentEmploymentList);
        int i = mapper.insertBatch(studentEmploymentList);
        if (i == studentEmploymentDTOList.size())
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    private StudentEmployment convertToEntity(StudentEmploymentDTO dto) {
        StudentEmployment entity = new StudentEmployment();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getGraduationYear() != null) {
            entity.setGraduationYear(dto.getGraduationYear().toLocalDate());
        }
        return entity;
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentEmployment(StudentEmploymentDTO studentEmploymentDTO) {
        StudentEmployment studentEmployment = new StudentEmployment();
        BeanUtils.copyProperties(studentEmploymentDTO, studentEmployment);
        int i = mapper.insert(studentEmployment);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public BaseResponse<Page<StudentEmploymentVO>> getStudentEmployment(EmploymentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);

        Page<StudentEmploymentVO> page = QueryChain.of(StudentEmployment.class)
                .select(STUDENT_EMPLOYMENT.ALL_COLUMNS) // 假设存在一个常量包含所有列
                .from(STUDENT_EMPLOYMENT)
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_EMPLOYMENT.STUDENT_ID))
                .where(Student::getMajorId).eq(query.getMajorId()) // 假设要根据专业ID查询
                .and(Student::getGrade).eq(query.getGrade()) // 假设要根据年级查询
                .pageAs(Page.of(pageNo, pageSize), StudentEmploymentVO.class);
        return ResponseUtil.success(page);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentEmployment(StudentEmploymentDTO studentEmploymentDTO) {
        StudentEmployment studentEmployment = new StudentEmployment();
        BeanUtils.copyProperties(studentEmploymentDTO, studentEmployment);
        if (studentEmploymentDTO.getGraduationYear() != null) {
            studentEmployment.setGraduationYear(studentEmploymentDTO.getGraduationYear().toLocalDate());
        }

        boolean i = UpdateChain.of(StudentEmployment.class)
                .set(STUDENT_EMPLOYMENT.GRADUATION_STATE, studentEmployment.getGraduationState())
                .set(STUDENT_EMPLOYMENT.GRADUATION_YEAR, studentEmployment.getGraduationYear())
                .set(STUDENT_EMPLOYMENT.WHEREABOUTS, studentEmployment.getWhereabouts())
                .set(STUDENT_EMPLOYMENT.JOB_NATURE, studentEmployment.getJobNature())
                .set(STUDENT_EMPLOYMENT.JOB_INDUSTRY, studentEmployment.getJobIndustry())
                .set(STUDENT_EMPLOYMENT.JOB_LOCATION, studentEmployment.getJobLocation())
                .set(STUDENT_EMPLOYMENT.CATEGORY, studentEmployment.getCategory())
                .set(STUDENT_EMPLOYMENT.SALARY, studentEmployment.getSalary())
                .where(STUDENT_EMPLOYMENT.STUDENT_ID.eq(studentEmployment.getStudentId())
                        .and(STUDENT_EMPLOYMENT.STUDENT_EMPLOYMENT_ID.eq(studentEmployment.getStudentEmploymentId())))
                .update();
        if (i)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentEmployment(String studentId) {
        QueryWrapper wrapper = QueryWrapper.create().where(STUDENT_EMPLOYMENT.STUDENT_ID.eq(studentId));
        int i = studentEmploymentMapper.deleteByQuery(wrapper);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}

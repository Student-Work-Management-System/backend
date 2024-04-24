package edu.guet.studentworkmanagementsystem.service.employment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.EmploymentStatistics;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.employment.StudentEmploymentMapper;
import edu.guet.studentworkmanagementsystem.network.EmploymentFeign;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static edu.guet.studentworkmanagementsystem.common.Majors.majorName2MajorId;
import static edu.guet.studentworkmanagementsystem.entity.po.employment.table.StudentEmploymentTableDef.STUDENT_EMPLOYMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class EmploymentServiceImpl extends  ServiceImpl<StudentEmploymentMapper, StudentEmployment> implements EmploymentService {
    @Autowired
    private EmploymentFeign employmentFeign;
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentEmployment(InsertEmploymentDTOList insertEmploymentDTOList) {
        int size = insertEmploymentDTOList.getInsertStudentEmploymentDTOList().size();
        List<StudentEmployment> studentEmploymentList = insertEmploymentDTOList.getInsertStudentEmploymentDTOList()
                .stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        int i = mapper.insertBatch(studentEmploymentList);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    private StudentEmployment convertToEntity(InsertStudentEmploymentDTO dto) {
        StudentEmployment entity = new StudentEmployment();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentEmployment(InsertStudentEmploymentDTO insertStudentEmploymentDTO) {
        StudentEmployment studentEmployment = new StudentEmployment();
        BeanUtils.copyProperties(insertStudentEmploymentDTO, studentEmployment);
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

        QueryChain<StudentEmployment> queryChain = QueryChain.of(StudentEmployment.class)
                .select(STUDENT_EMPLOYMENT.ALL_COLUMNS, STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT_EMPLOYMENT)
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_EMPLOYMENT.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .where(Student::getMajorId).eq(query.getMajorId())
                .and(Student::getGrade).eq(query.getGrade())
                .and(STUDENT_EMPLOYMENT.GRADUATION_YEAR.eq(query.getGraduationYear()))
                .and(STUDENT.STUDENT_ID.like(query.getSearch()).or(STUDENT.NAME.like(query.getSearch())));

        Page<StudentEmploymentVO> page = queryChain.pageAs(Page.of(pageNo, pageSize), StudentEmploymentVO.class);
        return ResponseUtil.success(page);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentEmployment(UpdateStudentEmploymentDTO updateStudentEmploymentDTO) {
        boolean update = UpdateChain.of(StudentEmployment.class)
                .set(STUDENT_EMPLOYMENT.GRADUATION_STATE, updateStudentEmploymentDTO.getGraduationState(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.GRADUATION_YEAR, updateStudentEmploymentDTO.getGraduationYear(), Objects::nonNull)
                .set(STUDENT_EMPLOYMENT.WHEREABOUTS, updateStudentEmploymentDTO.getWhereabouts(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.JOB_NATURE, updateStudentEmploymentDTO.getJobNature(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.JOB_INDUSTRY, updateStudentEmploymentDTO.getJobIndustry(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.JOB_LOCATION, updateStudentEmploymentDTO.getJobLocation(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.CATEGORY, updateStudentEmploymentDTO.getCategory(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.SALARY, updateStudentEmploymentDTO.getSalary(), StringUtils::hasLength)
                .where(StudentEmployment::getStudentEmploymentId).eq(updateStudentEmploymentDTO.getStudentEmploymentId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentEmployment(String studentEmploymentId) {
        int i = mapper.deleteById(studentEmploymentId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public void download(EmploymentStatQuery query, HttpServletResponse response) {
        try {
            List<String> majorIds = query.getMajorIds();
            if (majorIds.isEmpty()) {
                int key = 1;
                while (key <= 6) {
                    majorIds.add(String.valueOf(key));
                    key++;
                }
            }
            byte[] excelBytes = employmentFeign.exportWithStat(query);
            String fileName = "学生就业信息统计.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.getOutputStream().write(excelBytes);
        } catch (IOException exception) {
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }

    @Override
    public BaseResponse<HashMap<String, EmploymentStatistics>> statistics(EmploymentStatQuery query) {
        List<String> majorIds = query.getMajorIds();
        if (majorIds.isEmpty()) {
            int key = 1;
            while (key <= 6) {
                majorIds.add(String.valueOf(key));
                key++;
            }
        }
        Map<String, Object> map = employmentFeign.exportOnlyStat(query);
        Set<String> keys = majorName2MajorId.keySet();
        HashMap<String, HashMap<String, Integer>> graduationStatus = (HashMap<String, HashMap<String, Integer>>) map.get("graduation_status");
        HashMap<String, HashMap<String, Integer>> jobLocation = (HashMap<String, HashMap<String, Integer>>) map.get("job_location");
        HashMap<String, HashMap<String, Integer>> jobIndustry = (HashMap<String, HashMap<String, Integer>>) map.get("job_industry");
        HashMap<String, Double> salaryMap = (HashMap<String, Double>) map.get("salary");
        HashMap<String, EmploymentStatistics> statisticsHashMap = new HashMap<>();
        keys.forEach(key -> {
            EmploymentStatistics tmp = new EmploymentStatistics();
            if (graduationStatus.containsKey(key)) {
                tmp.setGraduationStatus(graduationStatus.get(key));
            }
            if (jobLocation.containsKey(key)) {
                tmp.setJobLocation(jobLocation.get(key));
            }
            if (jobIndustry.containsKey(key)) {
                tmp.setJobIndustry(jobIndustry.get(key));
            }
            if (salaryMap.containsKey(key)) {
                tmp.setSalary(String.valueOf(salaryMap.get(key)));
            }
            if (graduationStatus.containsKey(key) || jobLocation.containsKey(key) || jobIndustry.containsKey(key) || salaryMap.containsKey(key)) {
                statisticsHashMap.put(key, tmp);
            }
        });
        return ResponseUtil.success(statisticsHashMap);
    }
}

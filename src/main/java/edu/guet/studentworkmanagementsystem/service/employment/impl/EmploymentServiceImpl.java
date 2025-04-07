package edu.guet.studentworkmanagementsystem.service.employment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.employment.StudentEmploymentMapper;
import edu.guet.studentworkmanagementsystem.network.EmploymentFeign;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
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
import static edu.guet.studentworkmanagementsystem.entity.po.employment.table.StudentEmploymentTableDef.STUDENT_EMPLOYMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.PoliticTableDef.POLITIC;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentDetailTableDef.STUDENT_DETAIL;

@Service
public class EmploymentServiceImpl extends  ServiceImpl<StudentEmploymentMapper, StudentEmployment> implements EmploymentService {
    @Autowired
    private EmploymentFeign employmentFeign;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentEmployment(ValidateList<StudentEmployment> studentEmployments) {
        int i = mapper.insertBatch(studentEmployments);
        int size = studentEmployments.size();
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public BaseResponse<Page<StudentEmploymentItem>> getStudentEmployment(EmploymentQuery query) {
        CompletableFuture<Page<StudentEmploymentItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(StudentEmployment.class)
                    .select(
                            STUDENT_BASIC.ALL_COLUMNS,
                            STUDENT_DETAIL.ALL_COLUMNS,
                            STUDENT_EMPLOYMENT.ALL_COLUMNS,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME,
                            DEGREE.DEGREE_NAME,
                            POLITIC.POLITIC_STATUS
                    )
                    .from(STUDENT_EMPLOYMENT)
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_EMPLOYMENT.STUDENT_ID))
                    .innerJoin(STUDENT_DETAIL).on(STUDENT_DETAIL.STUDENT_ID.eq(STUDENT_BASIC.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                    .innerJoin(POLITIC).on(POLITIC.POLITIC_ID.eq(STUDENT_BASIC.POLITIC_ID))
                    .where(
                            STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.likeLeft(query.getSearch()))
                                    .or(STUDENT_BASIC.EMAIL.likeLeft(query.getSearch()))
                                    .or(STUDENT_BASIC.PHONE.likeLeft(query.getSearch()))
                    )
                    .and(MAJOR.MAJOR_ID.eq(query.getMajorId()))
                    .and(GRADE.GRADE_ID.eq(query.getGradeId()))
                    .and(DEGREE.DEGREE_ID.eq(query.getDegreeId()))
                    .and(POLITIC.POLITIC_ID.eq(query.getPoliticId()))
                    .and(STUDENT_BASIC.GENDER.eq(query.getGender()))
                    .and(STUDENT_EMPLOYMENT.GRADUATION_YEAR.eq(query.getGraduationYear()))
                    .pageAs(Page.of(pageNo, pageSize), StudentEmploymentItem.class);
        }, readThreadPool);
        Page<StudentEmploymentItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentEmployment(StudentEmployment studentEmployment) {
        boolean update = UpdateChain.of(StudentEmployment.class)
                .set(STUDENT_EMPLOYMENT.GRADUATION_YEAR, studentEmployment.getGraduationYear(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.WHEREABOUTS, studentEmployment.getWhereabouts(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.CODE, studentEmployment.getCode(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.ORGANIZATION_NAME, studentEmployment.getOrganizationName(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.JOB_NATURE, studentEmployment.getJobNature(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.JOB_INDUSTRY, studentEmployment.getJobIndustry(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.JOB_LOCATION, studentEmployment.getJobLocation(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.CATEGORY, studentEmployment.getCategory(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.CERTIFICATE_TIME, studentEmployment.getCertificateTime(), !Objects.isNull(studentEmployment.getCertificateTime()))
                .where(STUDENT_EMPLOYMENT.STUDENT_EMPLOYMENT_ID.eq(studentEmployment.getStudentEmploymentId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
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
            query.setMajorIds(majorIds);
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
    public BaseResponse<HashMap<String, StudentEmploymentStatItem>> statistics(EmploymentStatQuery query) {
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
        HashMap<String, Object> map = employmentFeign.exportOnlyStat(query);
        if (map.isEmpty())
            return ResponseUtil.success();
        HashMap<String, HashMap<String, Object>> graduationStatus = (HashMap<String, HashMap<String, Object>>) map.get("毕业后状态");
        HashMap<String, HashMap<String, Object>> jobLocation = (HashMap<String, HashMap<String, Object>>) map.get("单位所在地");
        HashMap<String, HashMap<String, Object>> jobIndustry = (HashMap<String, HashMap<String, Object>>) map.get("单位所处行业");
        HashMap<String, Double> salaryMap = (HashMap<String, Double>) map.get("平均薪资");
        HashMap<String, StudentEmploymentStatItem> statisticsHashMap = new HashMap<>();
        keys.forEach(key -> {
            StudentEmploymentStatItem tmp = new StudentEmploymentStatItem();
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

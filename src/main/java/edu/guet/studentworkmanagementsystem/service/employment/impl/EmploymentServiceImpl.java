package edu.guet.studentworkmanagementsystem.service.employment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.*;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentStatRow;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.employment.StudentEmploymentMapper;
import edu.guet.studentworkmanagementsystem.network.EmploymentFeign;
import edu.guet.studentworkmanagementsystem.service.employment.EmploymentService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.employment.table.StudentEmploymentTableDef.STUDENT_EMPLOYMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentTableDef.ENROLLMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.PoliticTableDef.POLITIC;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

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
                            ENROLLMENT.ALL_COLUMNS,
                            STUDENT_EMPLOYMENT.ALL_COLUMNS,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME,
                            DEGREE.DEGREE_NAME,
                            POLITIC.POLITIC_STATUS
                    )
                    .from(STUDENT_EMPLOYMENT)
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_EMPLOYMENT.STUDENT_ID))
                    .innerJoin(ENROLLMENT).on(ENROLLMENT.STUDENT_ID.eq(STUDENT_BASIC.STUDENT_ID))
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
                .set(STUDENT_EMPLOYMENT.WHEREABOUTS, studentEmployment.getWhereabouts(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.STATE, studentEmployment.getState(), StringUtils::hasLength)
                .set(STUDENT_EMPLOYMENT.GRADUATION_YEAR, studentEmployment.getGraduationYear())
                .set(STUDENT_EMPLOYMENT.CODE, studentEmployment.getCode())
                .set(STUDENT_EMPLOYMENT.ORGANIZATION_NAME, studentEmployment.getOrganizationName())
                .set(STUDENT_EMPLOYMENT.JOB_NATURE, studentEmployment.getJobNature())
                .set(STUDENT_EMPLOYMENT.JOB_INDUSTRY, studentEmployment.getJobIndustry())
                .set(STUDENT_EMPLOYMENT.JOB_LOCATION, studentEmployment.getJobLocation())
                .set(STUDENT_EMPLOYMENT.CATEGORY, studentEmployment.getCategory())
                .set(STUDENT_EMPLOYMENT.CERTIFICATE_TIME, studentEmployment.getCertificateTime())
                .set(STUDENT_EMPLOYMENT.SALARY, studentEmployment.getSalary())
                .set(STUDENT_EMPLOYMENT.CONTACT_PERSON, studentEmployment.getContactPerson())
                .set(STUDENT_EMPLOYMENT.CONTACT_PHONE, studentEmployment.getContactPhone())
                .set(STUDENT_EMPLOYMENT.COMMENT, studentEmployment.getComment(), StringUtils::hasLength)
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
    public BaseResponse<List<StudentEmploymentStatGroup>> getStat(EmploymentStatQuery query) {
        CompletableFuture<List<StudentEmploymentStatGroup>> future = CompletableFuture.supplyAsync(() -> {
            List<StudentEmploymentStatRow> rows = mapper.getStat(query);
            return convertToStatGroup(rows);
        }, readThreadPool);
        List<StudentEmploymentStatGroup> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public List<StudentEmploymentStatGroup> convertToStatGroup(List<StudentEmploymentStatRow> rows) {
        Map<String, StudentEmploymentStatGroup> gradeMap = new LinkedHashMap<>();

        for (StudentEmploymentStatRow row : rows) {
            // 获取或创建年级对象
            StudentEmploymentStatGroup gradeGroup = gradeMap.computeIfAbsent(row.getGradeName(), g ->
                    StudentEmploymentStatGroup.builder()
                            .gradeName(g)
                            .majors(new ArrayList<>())
                            .build()
            );

            // 查找或创建专业对象
            StudentEmploymentStatGroup.MajorGroup majorGroup = gradeGroup.getMajors().stream()
                    .filter(m -> m.getMajorName().equals(row.getMajorName()))
                    .findFirst()
                    .orElseGet(() -> {
                        StudentEmploymentStatGroup.MajorGroup newMajor = StudentEmploymentStatGroup.MajorGroup.builder()
                                .majorName(row.getMajorName())
                                .employments(new ArrayList<>())
                                .build();
                        gradeGroup.getMajors().add(newMajor);
                        return newMajor;
                    });

            // 添加就业信息
            majorGroup.getEmployments().add(
                    StudentEmploymentStatGroup.StudentEmploymentGroup.builder()
                            .whereabouts(row.getWhereabouts())
                            .number(row.getNumber())
                            .build()
            );
        }

        return new ArrayList<>(gradeMap.values());
    }
}

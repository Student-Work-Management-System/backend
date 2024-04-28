package edu.guet.studentworkmanagementsystem.service.enrollment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.enrollment.EnrollmentMapper;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentTableDef.ENROLLMENT;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {
    @Override
    @Transactional
    public <T> BaseResponse<T> importEnrollment(EnrollmentList enrollmentList) {
        int size = enrollmentList.getEnrollmentList().size();
        int i = mapper.insertBatch(enrollmentList.getEnrollmentList());
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public BaseResponse<Scholarship> insertEnrollment(Enrollment enrollment) {
        int i = mapper.insert(enrollment);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateEnrollment(Enrollment enrollment) {
        boolean update = UpdateChain.of(Enrollment.class)
                .set(ENROLLMENT.EXAMINEE_ID, enrollment.getExamineeId(), StringUtils::hasLength)
                .set(ENROLLMENT.NAME, enrollment.getName(), StringUtils::hasLength)
                .set(ENROLLMENT.ENROLL_MAJOR_ID, enrollment.getEnrollMajorId(), StringUtils::hasLength)
                .set(ENROLLMENT.FIRST_MAJOR, enrollment.getFirstMajor(), StringUtils::hasLength)
                .set(ENROLLMENT.ORIGIN, enrollment.getOrigin(), StringUtils::hasLength)
                .set(ENROLLMENT.SCORE, enrollment.getScore(), StringUtils::hasLength)
                .set(ENROLLMENT.ENROLL_TIME, enrollment.getEnrollTime(), StringUtils::hasLength)
                .where(ENROLLMENT.ENROLLMENT_ID.eq(enrollment.getEnrollmentId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteEnrollment(String enrollmentInfoId) {
        int i = mapper.deleteById(enrollmentInfoId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<Enrollment>> getAllRecords(EnrollmentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<Enrollment> enrollmentInfoPage = QueryChain.of(Enrollment.class)
                .select(ENROLLMENT.ALL_COLUMNS)
                .from(ENROLLMENT)
                .where(ENROLLMENT.NAME.like(query.getName())
                    .or(ENROLLMENT.EXAMINEE_ID.like(query.getExamineeId()))
                    .or(ENROLLMENT.ID.like(query.getId()))
                    .or(ENROLLMENT.ORIGIN.like(query.getOrigin())))
                .and(ENROLLMENT.ENROLL_MAJOR_ID.eq(query.getEnrollMajorId()))
                .and(ENROLLMENT.FIRST_MAJOR.eq(query.getFirstMajor()))
                .and(ENROLLMENT.ENROLL_TIME.eq(query.getEnrollTime()))
                .page(Page.of(pageNo, pageSize));
        return ResponseUtil.success(enrollmentInfoPage);
    }
}

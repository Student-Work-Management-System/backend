package edu.guet.studentworkmanagementsystem.service.enrollmentInfo.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentInfoList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollmentInfo.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.EnrollmentInfo;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.enrollmentInfo.EnrollmentInfoMapper;
import edu.guet.studentworkmanagementsystem.service.enrollmentInfo.EnrollmentInfoService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentInfoTableDef.ENROLLMENT_INFO;

@Service
public class EnrollmentInfoServiceImpl extends ServiceImpl<EnrollmentInfoMapper, EnrollmentInfo> implements EnrollmentInfoService {
    @Override
    @Transactional
    public <T> BaseResponse<T> importEnrollmentInfo(EnrollmentInfoList enrollmentInfos) {
        int size = enrollmentInfos.getEnrollmentInfoList().size();
        int i = mapper.insertBatch(enrollmentInfos.getEnrollmentInfoList());
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public BaseResponse<Scholarship> insertEnrollmentInfo(EnrollmentInfo enrollmentInfo) {
        int i = mapper.insert(enrollmentInfo);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateEnrollmentInfo(EnrollmentInfo enrollmentInfo) {
        boolean update = UpdateChain.of(EnrollmentInfo.class)
                .set(ENROLLMENT_INFO.EXAM_ID, enrollmentInfo.getExamId(), StringUtils::hasLength)
                .set(ENROLLMENT_INFO.ID_NUMBER, enrollmentInfo.getIdNumber(), StringUtils::hasLength)
                .set(ENROLLMENT_INFO.NAME, enrollmentInfo.getName(), StringUtils::hasLength)
                .set(ENROLLMENT_INFO.FIRST_MAJOR, enrollmentInfo.getFirstMajor(), StringUtils::hasLength)
                .set(ENROLLMENT_INFO.FROM, enrollmentInfo.getFrom(), StringUtils::hasLength)
                .set(ENROLLMENT_INFO.ENROLL_COLLEGE, enrollmentInfo.getEnrollCollege(), StringUtils::hasLength)
                .set(ENROLLMENT_INFO.SCORE, enrollmentInfo.getScore(), Objects::nonNull)
                .set(ENROLLMENT_INFO.ENROLL_MAJOR, enrollmentInfo.getEnrollMajor(), StringUtils::hasLength)
                .where(ENROLLMENT_INFO.ENROLLMENT_INFO_ID.eq(enrollmentInfo.getEnrollmentInfoId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteEnrollmentInfo(String enrollmentInfoId) {
        int i = mapper.deleteById(enrollmentInfoId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<EnrollmentInfo>> getAllRecords(EnrollmentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<EnrollmentInfo> enrollmentInfoPage = QueryChain.of(EnrollmentInfo.class)
                .select(ENROLLMENT_INFO.ALL_COLUMNS)
                .from(ENROLLMENT_INFO)
                .where(ENROLLMENT_INFO.FROM.eq(query.getFrom()))
                .and(ENROLLMENT_INFO.ENROLL_COLLEGE.eq(query.getEnrollCollege()))
                .and(ENROLLMENT_INFO.ENROLL_MAJOR.eq(query.getEnrollCollege()))
                .and(ENROLLMENT_INFO.FIRST_MAJOR.eq(query.getFirstMajor()))
                .page(Page.of(pageNo, pageSize));
        return ResponseUtil.success(enrollmentInfoPage);
    }
}

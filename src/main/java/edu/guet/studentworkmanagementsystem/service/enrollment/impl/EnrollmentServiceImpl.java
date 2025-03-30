package edu.guet.studentworkmanagementsystem.service.enrollment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.enrollment.EnrollmentMapper;
import edu.guet.studentworkmanagementsystem.network.EnrollmentFeign;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static edu.guet.studentworkmanagementsystem.common.Majors.majorName2MajorId;
import static edu.guet.studentworkmanagementsystem.entity.po.enrollment.table.EnrollmentTableDef.ENROLLMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;

@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements EnrollmentService {
    @Autowired
    private EnrollmentFeign enrollmentFeign;
    @Override
    @Transactional
    public <T> BaseResponse<T> importEnrollment(EnrollmentList enrollmentList) {
        int size = enrollmentList.getEnrollments().size();
        int i = mapper.insertBatch(enrollmentList.getEnrollments());
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
    public BaseResponse<Page<EnrollmentItem>> getAllRecords(EnrollmentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<EnrollmentItem> enrollmentInfoPage = QueryChain.of(Enrollment.class)
                .select(ENROLLMENT.ALL_COLUMNS, MAJOR.MAJOR_NAME.as("enrollMajor"))
                .from(ENROLLMENT)
                .innerJoin(MAJOR).on(ENROLLMENT.ENROLL_MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .where(ENROLLMENT.NAME.like(query.getName())
                    .or(ENROLLMENT.EXAMINEE_ID.like(query.getExamineeId()))
                    .or(ENROLLMENT.ID.like(query.getId()))
                    .or(ENROLLMENT.ORIGIN.like(query.getOrigin())))
                .and(ENROLLMENT.ENROLL_MAJOR_ID.eq(query.getEnrollMajorId()))
                .and(ENROLLMENT.FIRST_MAJOR.eq(query.getFirstMajor()))
                .and(ENROLLMENT.ENROLL_TIME.eq(query.getEnrollTime()))
                .pageAs(Page.of(pageNo, pageSize), EnrollmentItem.class);
        return ResponseUtil.success(enrollmentInfoPage);
    }

    @Override
    public void download(EnrollmentStatQuery query, HttpServletResponse response) {
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
            byte[] excelBytes = enrollmentFeign.exportWithStat(query);
            String fileName = "招生信息统计.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.getOutputStream().write(excelBytes);
        } catch (IOException exception) {
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }

    @Override
    public BaseResponse<HashMap<String, EnrollmentStatItem>> statistics(EnrollmentStatQuery query) {
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
        HashMap<String, Object> map = enrollmentFeign.exportOnlyStat(query);
        if (map.isEmpty())
            return ResponseUtil.success();
        HashMap<String, HashMap<String, Object>> originMap = (HashMap<String, HashMap<String, Object>>) map.get("生源地情况");
        HashMap<String, HashMap<String, Object>> enrollmentStateMap = (HashMap<String, HashMap<String, Object>>) map.get("录取情况");
        HashMap<String, Object> reginScoreMap = (HashMap<String, Object>) map.get("各生源地高考分数统计");
        HashMap<String, EnrollmentStatItem> ret = new HashMap<>();
        keys.forEach(key -> {
            EnrollmentStatItem enrollmentStatItem = new EnrollmentStatItem();
            HashMap<String, HashMap<String, Object>> reginScore = new HashMap<>();
            if (originMap.containsKey(key)) {
                enrollmentStatItem.setOrigin(originMap.get(key));
            }
            if (enrollmentStateMap.containsKey(key)) {
                enrollmentStatItem.setEnrollmentState(enrollmentStateMap.get(key));
            }
            Set<String> reginScoreKeys = reginScoreMap.keySet();
            reginScoreKeys.forEach(it -> {
                HashMap<String, HashMap<String, Object>> hashMap = (HashMap<String, HashMap<String, Object>>) reginScoreMap.get(it);
                if (hashMap.containsKey(key)) {
                    reginScore.put(it, hashMap.get(key));
                }
            });
            enrollmentStatItem.setRegionScores(reginScore);
            ret.put(key, enrollmentStatItem);
        });
        return ResponseUtil.success(ret);
    }
}

package edu.guet.studentworkmanagementsystem.service.schoolPrecaution.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.PrecautionList;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.StudentSchoolPrecautionRequest;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution.StudentSchoolPrecautionItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.schoolPrecaution.PrecautionMapper;
import edu.guet.studentworkmanagementsystem.network.PrecautionFeign;
import edu.guet.studentworkmanagementsystem.service.schoolPrecaution.PrecautionService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PrecautionServiceImpl extends ServiceImpl<PrecautionMapper, StudentPrecaution> implements PrecautionService {
    @Autowired
    private PrecautionFeign precautionFeign;
    @Override
    @Transactional
    public <T> BaseResponse<T> importSchoolPrecaution(PrecautionList schoolPrecautionList) {
        List<StudentPrecaution> schoolPrecautions = schoolPrecautionList.getPrecautions();
        int size = schoolPrecautions.size();
        int i = mapper.insertBatch(schoolPrecautions);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertSchoolPrecaution(StudentPrecaution schoolPrecaution) {
        int i = mapper.insert(schoolPrecaution);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateSchoolPrecaution(StudentSchoolPrecautionRequest studentSchoolPrecautionRequest) {
        boolean update = UpdateChain.of(StudentPrecaution.class)
                .set(StudentPrecaution::getSchoolPrecautionLevel, studentSchoolPrecautionRequest.getSchoolPrecautionLevel(), StringUtils.hasLength(studentSchoolPrecautionRequest.getSchoolPrecautionLevel()))
                .set(StudentPrecaution::getPrecautionTerm, studentSchoolPrecautionRequest.getPrecautionTerm(), StringUtils.hasLength(studentSchoolPrecautionRequest.getPrecautionTerm()))
                .set(StudentPrecaution::getComment, studentSchoolPrecautionRequest.getComment(), StringUtils.hasLength(studentSchoolPrecautionRequest.getComment()))
                .set(StudentPrecaution::getDetailReason, studentSchoolPrecautionRequest.getDetailReason(), StringUtils.hasLength(studentSchoolPrecautionRequest.getDetailReason()))
                .set(StudentPrecaution::getStudentId, studentSchoolPrecautionRequest.getStudentId(), StringUtils.hasLength(studentSchoolPrecautionRequest.getStudentId()))
                .where(StudentPrecaution::getPrecautionId).eq(studentSchoolPrecautionRequest.getStudentSchoolPrecautionId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteSchoolPrecaution(String studentSchoolPrecautionId) {
        int i = mapper.deleteById(studentSchoolPrecautionId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentSchoolPrecautionItem>> getAllRecords(PrecautionQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        // Page<StudentSchoolPrecautionItem> studentSchoolPrecautionVOPage = QueryChain.of(StudentPrecaution.class)
        //         .select(STUDENT_SCHOOL_PRECAUTION.ALL_COLUMNS, STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
        //         .from(STUDENT_SCHOOL_PRECAUTION)
        //         .innerJoin(STUDENT).on(STUDENT_SCHOOL_PRECAUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID))
        //         .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
        //         .where(STUDENT.GRADE_ID.eq(query.getGrade()))
        //         .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
        //         .and(STUDENT_SCHOOL_PRECAUTION.SCHOOL_PRECAUTION_LEVEL.eq(query.getSchoolPrecautionLevel()))
        //         .pageAs(Page.of(pageNo, pageSize), StudentSchoolPrecautionItem.class);
        // return ResponseUtil.success(studentSchoolPrecautionVOPage);
        return null;
    }

    @Override
    public BaseResponse<HashMap<String, Object>> stat(PrecautionStatQuery query) {
        List<String> majorIds = query.getMajorIds();
        if (majorIds.isEmpty()) {
            int key = 1;
            while (key <= 6) {
                majorIds.add(String.valueOf(key));
                key++;
            }
        }
        query.setMajorIds(majorIds);
        HashMap<String, Object> stringObjectHashMap = precautionFeign.exportOnlyStat(query);
        HashMap<String, Object> o = (HashMap<String, Object>) stringObjectHashMap.get("学业预警情况");
        return ResponseUtil.success(o);
    }

    @Override
    public void download(PrecautionStatQuery query, HttpServletResponse response) {
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
            byte[] excelBytes = precautionFeign.exportWithStat(query);
            String fileName = "学业预警统计.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.getOutputStream().write(excelBytes);
        } catch (IOException exception) {
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }
}

package edu.guet.studentworkmanagementsystem.service.schoolPrecaution.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.PrecautionList;
import edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution.StudentSchoolPrecautionDTO;
import edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution.StudentSchoolPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.schoolPrecaution.StudentSchoolPrecautionVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.schoolPrecaution.PrecautionMapper;
import edu.guet.studentworkmanagementsystem.service.schoolPrecaution.PrecautionService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.schoolPrecaution.table.StudentSchoolPrecautionTableDef.STUDENT_SCHOOL_PRECAUTION;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class PrecautionServiceImpl extends ServiceImpl<PrecautionMapper, StudentSchoolPrecaution> implements PrecautionService {
    @Override
    @Transactional
    public <T> BaseResponse<T> importSchoolPrecaution(PrecautionList schoolPrecautionList) {
        List<StudentSchoolPrecaution> schoolPrecautions = schoolPrecautionList.getPrecautions();
        int size = schoolPrecautions.size();
        int i = mapper.insertBatch(schoolPrecautions);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertSchoolPrecaution(StudentSchoolPrecaution schoolPrecaution) {
        int i = mapper.insert(schoolPrecaution);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateSchoolPrecaution(StudentSchoolPrecautionDTO studentSchoolPrecautionDTO) {
        boolean update = UpdateChain.of(StudentSchoolPrecaution.class)
                .set(StudentSchoolPrecaution::getSchoolPrecautionLevel, studentSchoolPrecautionDTO.getSchoolPrecautionLevel(), StringUtils.hasLength(studentSchoolPrecautionDTO.getSchoolPrecautionLevel()))
                .set(StudentSchoolPrecaution::getPrecautionTerm, studentSchoolPrecautionDTO.getPrecautionTerm(), StringUtils.hasLength(studentSchoolPrecautionDTO.getPrecautionTerm()))
                .set(StudentSchoolPrecaution::getComment, studentSchoolPrecautionDTO.getComment(), StringUtils.hasLength(studentSchoolPrecautionDTO.getComment()))
                .set(StudentSchoolPrecaution::getDetailReason, studentSchoolPrecautionDTO.getDetailReason(), StringUtils.hasLength(studentSchoolPrecautionDTO.getDetailReason()))
                .set(StudentSchoolPrecaution::getStudentId, studentSchoolPrecautionDTO.getStudentId(), StringUtils.hasLength(studentSchoolPrecautionDTO.getStudentId()))
                .where(StudentSchoolPrecaution::getStudentSchoolPrecautionId).eq(studentSchoolPrecautionDTO.getStudentSchoolPrecautionId())
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
    public BaseResponse<Page<StudentSchoolPrecautionVO>> getAllRecords(PrecautionQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentSchoolPrecautionVO> studentSchoolPrecautionVOPage = QueryChain.of(StudentSchoolPrecaution.class)
                .select(STUDENT_SCHOOL_PRECAUTION.ALL_COLUMNS, STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT_SCHOOL_PRECAUTION)
                .innerJoin(STUDENT).on(STUDENT_SCHOOL_PRECAUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .where(STUDENT.GRADE.eq(query.getGrade()))
                .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT_SCHOOL_PRECAUTION.SCHOOL_PRECAUTION_LEVEL.eq(query.getSchoolPrecautionLevel()))
                .pageAs(Page.of(pageNo, pageSize), StudentSchoolPrecautionVO.class);
        return ResponseUtil.success(studentSchoolPrecautionVOPage);
    }
}

package edu.guet.studentworkmanagementsystem.service.scholarship.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.StudentScholarshipDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.UpdateScholarshipOwner;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.UpdateStudentScholarshipType;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.scholarship.ScholarshipMapper;
import edu.guet.studentworkmanagementsystem.mapper.scholarship.StudentScholarshipMapper;
import edu.guet.studentworkmanagementsystem.service.scholarship.ScholarshipService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.scholarship.table.ScholarshipTableDef.SCHOLARSHIP;
import static edu.guet.studentworkmanagementsystem.entity.po.scholarship.table.StudentScholarshipTableDef.STUDENT_SCHOLARSHIP;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class ScholarshipServiceImpl extends ServiceImpl<StudentScholarshipMapper, StudentScholarship> implements ScholarshipService {
    @Autowired
    private ScholarshipMapper scholarshipMapper;
    @Override
    @Transactional
    public <T> BaseResponse<T> importScholarship(List<Scholarship> scholarships) {
        int i = scholarshipMapper.insertBatch(scholarships);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertScholarship(Scholarship scholarship) {
        int i = scholarshipMapper.insert(scholarship);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateScholarship(Scholarship scholarship) {
        if (Objects.isNull(scholarship.getScholarshipId()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        boolean update = UpdateChain.of(Scholarship.class)
                .set(Scholarship::getScholarshipName, scholarship.getScholarshipName(), StringUtils.hasLength(scholarship.getScholarshipName()))
                .set(Scholarship::getScholarshipLevel, scholarship.getScholarshipLevel(), StringUtils.hasLength(scholarship.getScholarshipLevel()))
                .where(Scholarship::getScholarshipId).eq(scholarship.getScholarshipId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<Scholarship>> getScholarships() {
        List<Scholarship> scholarships = QueryChain.of(Scholarship.class)
                .list();
        return ResponseUtil.success(scholarships);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteScholarship(String scholarshipId) {
        int i = scholarshipMapper.deleteById(scholarshipId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentScholarshipVO>> getStudentScholarship(ScholarshipQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentScholarshipVO> studentScholarshipVOPage = QueryChain.of(StudentScholarship.class)
                .select(STUDENT.ALL_COLUMNS, SCHOLARSHIP.ALL_COLUMNS, STUDENT_SCHOLARSHIP.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT_SCHOLARSHIP)
                .innerJoin(SCHOLARSHIP).on(SCHOLARSHIP.SCHOLARSHIP_ID.eq(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID))
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_SCHOLARSHIP.STUDENT_ID))
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                .where(STUDENT.GRADE.eq(query.getGrade()))
                .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT_SCHOLARSHIP.AWARD_YEAR.eq(query.getAwardYear()))
                .pageAs(Page.of(pageNo, pageSize), StudentScholarshipVO.class);
        return ResponseUtil.success(studentScholarshipVOPage);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> arrangeStudentScholarship(StudentScholarshipDTO studentScholarshipDTO) {
        StudentScholarship studentScholarship = new StudentScholarship(studentScholarshipDTO);
        int i = mapper.insert(studentScholarship);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentScholarshipInfo(StudentScholarshipDTO studentScholarshipDTO) {
        boolean update = UpdateChain.of(StudentScholarship.class)
                .set(StudentScholarship::getAwardYear, studentScholarshipDTO.getAwardYear(), StringUtils.hasLength(studentScholarshipDTO.getAwardYear()))
                .where(StudentScholarship::getStudentId).eq(studentScholarshipDTO.getStudentId())
                .and(StudentScholarship::getScholarshipId).eq(studentScholarshipDTO.getScholarshipId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentScholarship(UpdateStudentScholarshipType updateStudentScholarshipType) {
        boolean update = UpdateChain.of(StudentScholarship.class)
                .set(StudentScholarship::getScholarshipId, updateStudentScholarshipType.getNewScholarshipId())
                .where(STUDENT_SCHOLARSHIP.STUDENT_ID.eq(updateStudentScholarshipType.getStudentId()))
                .where(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID.eq(updateStudentScholarshipType.getOldScholarshipId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentScholarship(UpdateScholarshipOwner updateScholarshipOwner) {
        boolean update = UpdateChain.of(StudentScholarship.class)
                .set(StudentScholarship::getStudentId, updateScholarshipOwner.getNewStudentId())
                .where(STUDENT_SCHOLARSHIP.STUDENT_ID.eq(updateScholarshipOwner.getOldStudentId()))
                .where(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID.eq(updateScholarshipOwner.getScholarshipId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentScholarship(String studentId, String scholarshipId) {
        QueryWrapper wrapper = QueryWrapper.create().where(STUDENT_SCHOLARSHIP.STUDENT_ID.eq(scholarshipId)).and(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID.eq(scholarshipId));
        int i = mapper.deleteByQuery(wrapper);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}

package edu.guet.studentworkmanagementsystem.service.povertyAssistance.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.*;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.StudentPovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.povertyAssistance.PovertyAssistanceMapper;
import edu.guet.studentworkmanagementsystem.mapper.povertyAssistance.StudentPovertyAssistanceMapper;
import edu.guet.studentworkmanagementsystem.service.povertyAssistance.PovertyAssistanceService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.table.PovertyAssistanceTableDef.POVERTY_ASSISTANCE;
import static edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.table.StudentPovertyAssistanceTableDef.STUDENT_POVERTY_ASSISTANCE;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class PovertyAssistanceServiceImpl extends ServiceImpl<StudentPovertyAssistanceMapper, StudentPovertyAssistance> implements PovertyAssistanceService {
    @Autowired
    private PovertyAssistanceMapper povertyAssistanceMapper;
    @Override
    public BaseResponse<List<PovertyAssistance>> getAllPovertyAssistance() {
        return ResponseUtil.success(povertyAssistanceMapper.selectAll());
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> importPovertyAssistance(PovertyAssistanceList povertyAssistanceList) {
        int size = povertyAssistanceList.getPovertyAssistances().size();
        int i = povertyAssistanceMapper.insertBatch(povertyAssistanceList.getPovertyAssistances());
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertPovertyAssistance(PovertyAssistance povertyAssistance) {
        int i = povertyAssistanceMapper.insert(povertyAssistance);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updatePovertyAssistance(PovertyAssistance povertyAssistance) {
        if (!StringUtils.hasLength(povertyAssistance.getPovertyAssistanceId()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        boolean update = UpdateChain.of(PovertyAssistance.class)
                .set(PovertyAssistance::getPovertyType, povertyAssistance.getPovertyType(), StringUtils.hasLength(povertyAssistance.getPovertyType()))
                .set(PovertyAssistance::getPovertyAssistanceStandard, povertyAssistance.getPovertyAssistanceStandard(), StringUtils.hasLength(povertyAssistance.getPovertyAssistanceStandard()))
                .set(PovertyAssistance::getPovertyLevel, povertyAssistance.getPovertyLevel(), StringUtils.hasLength(povertyAssistance.getPovertyLevel()))
                .where(PovertyAssistance::getPovertyAssistanceId).eq(povertyAssistance.getPovertyAssistanceId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deletePovertyAssistance(String povertyAssistanceId) {
        int i = povertyAssistanceMapper.deleteById(povertyAssistanceId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> addStudentPovertyAssistance(InsertStudentPovertyAssistanceDTO insertStudentPovertyAssistanceDTO) {
        StudentPovertyAssistance studentPovertyAssistance = new StudentPovertyAssistance(insertStudentPovertyAssistanceDTO);
        int i = mapper.insert(studentPovertyAssistance);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentPovertyAssistance(InsertStudentPovertyAssistanceList insertStudentPovertyAssistanceList) {
        ArrayList<StudentPovertyAssistance> studentPovertyAssistances = new ArrayList<>();
        insertStudentPovertyAssistanceList.getStudentPovertyAssistances().forEach(it -> {
            StudentPovertyAssistance studentPovertyAssistance = new StudentPovertyAssistance(it);
            studentPovertyAssistances.add(studentPovertyAssistance);
        });
        int size = studentPovertyAssistances.size();
        int i = mapper.insertBatch(studentPovertyAssistances);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentStudentPovertyAssistanceVO>> getStudentPovertyAssistance(PovertyAssistanceQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentStudentPovertyAssistanceVO> studentStudentPovertyAssistanceVOPage = QueryChain.of(StudentPovertyAssistance.class)
                .select(STUDENT_POVERTY_ASSISTANCE.ALL_COLUMNS, STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS, POVERTY_ASSISTANCE.ALL_COLUMNS)
                .from(STUDENT_POVERTY_ASSISTANCE)
                .innerJoin(POVERTY_ASSISTANCE).on(STUDENT_POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_ID.eq(POVERTY_ASSISTANCE.POVERTY_ASSISTANCE_ID))
                .innerJoin(STUDENT).on(STUDENT_POVERTY_ASSISTANCE.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .where(Student::getGradeId).eq(query.getGrade())
                .and(Student::getMajorId).eq(query.getMajorId())
                .and(Student::getEnabled).eq(query.getEnabled())
                .or(PovertyAssistance::getPovertyType).like(query.getSearch())
                .or(PovertyAssistance::getPovertyAssistanceStandard).like(query.getSearch())
                .and(PovertyAssistance::getPovertyLevel).eq(query.getPovertyLevel())
                .and(StudentPovertyAssistance::getAssistanceYear).eq(query.getAssistanceYear())
                .pageAs(Page.of(pageNo, pageSize), StudentStudentPovertyAssistanceVO.class);
        return ResponseUtil.success(studentStudentPovertyAssistanceVOPage);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentPovertyAssistance(UpdateStudentPovertyAssistanceDTO updateStudentPovertyAssistanceDTO) {
        boolean update = UpdateChain.of(StudentPovertyAssistance.class)
                .set(StudentPovertyAssistance::getStudentId, updateStudentPovertyAssistanceDTO.getStudentId(), StringUtils.hasLength(updateStudentPovertyAssistanceDTO.getStudentId()))
                .set(StudentPovertyAssistance::getPovertyAssistanceId, updateStudentPovertyAssistanceDTO.getPovertyAssistanceId(), StringUtils.hasLength(updateStudentPovertyAssistanceDTO.getPovertyAssistanceId()))
                .set(StudentPovertyAssistance::getAssistanceYear, updateStudentPovertyAssistanceDTO.getAssistanceYear(), StringUtils.hasLength(updateStudentPovertyAssistanceDTO.getAssistanceYear()))
                .where(StudentPovertyAssistance::getStudentPovertyAssistanceId).eq(updateStudentPovertyAssistanceDTO.getStudentPovertyAssistanceId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentPovertyAssistance(String studentPovertyAssistanceId) {
        int i = mapper.deleteById(studentPovertyAssistanceId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}

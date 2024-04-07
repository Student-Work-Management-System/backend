package edu.guet.studentworkmanagementsystem.service.cet.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.CETQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.InsertDTOList;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.InsertStudentCetDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.UpdateStudentCetDTO;
import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.StudentCetVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.cet.StudentCetMapper;
import edu.guet.studentworkmanagementsystem.service.cet.CetService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.cet.table.StudentCetTableDef.STUDENT_CET;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class CetServiceImpl extends ServiceImpl<StudentCetMapper, StudentCet> implements CetService {
    @Override
    @Transactional
    public <T> BaseResponse<T> importCETScore(InsertDTOList insertDTOList) {
        int size = insertDTOList.getInsertStudentCetDTOList().size();
        List<StudentCet> collect = insertDTOList.getInsertStudentCetDTOList()
                .stream()
                .map(this::convertToEntity)
                .toList();
        int i = mapper.insertBatch(collect);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentCet(InsertStudentCetDTO insertStudentCetDTO) {
        StudentCet studentCet = convertToEntity(insertStudentCetDTO);
        int i = mapper.insert(studentCet);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentCetVO>> getAllRecord(CETQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentCetVO> studentCetVOPage = QueryChain.of(StudentCet.class)
                .select(STUDENT_CET.ALL_COLUMNS, STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT_CET)
                .innerJoin(STUDENT).on(STUDENT_CET.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .where(STUDENT_CET.SCORE.le(query.getScore()))
                .and(STUDENT_CET.EXAM_TYPE.eq(query.getExamType()))
                .and(STUDENT_CET.EXAM_DATE.eq(query.getExamDate()))
                .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT.GRADE.eq(query.getGrade()))
                .pageAs(Page.of(pageNo, pageSize), StudentCetVO.class);
        return ResponseUtil.success(studentCetVOPage);
    }
    @Override
    public BaseResponse<List<String>> getOptionalExamDate() {
        List<String> collect = QueryChain.of(StudentCet.class)
                .select(STUDENT_CET.EXAM_DATE)
                .from(STUDENT_CET)
                .list()
                .stream()
                .map(StudentCet::getExamDate)
                .toList();
        return ResponseUtil.success(collect);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentCET(UpdateStudentCetDTO updateStudentCetDTO) {
        boolean update = UpdateChain.of(StudentCet.class)
                .set(STUDENT_CET.STUDENT_ID, updateStudentCetDTO.getStudentId(), StringUtils::hasLength)
                .set(STUDENT_CET.CERTIFICATE_NUMBER, updateStudentCetDTO.getCertificateNumber(), StringUtils::hasLength)
                .set(STUDENT_CET.SCORE, updateStudentCetDTO.getScore(), Objects::nonNull)
                .set(STUDENT_CET.EXAM_DATE, updateStudentCetDTO.getExamDate(), StringUtils::hasLength)
                .set(STUDENT_CET.EXAM_TYPE, updateStudentCetDTO.getExamType(), StringUtils::hasLength)
                .where(StudentCet::getStudentCetId).eq(updateStudentCetDTO.getStudentCetId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCET(String studentCETId) {
        int i = mapper.deleteById(studentCETId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    private StudentCet convertToEntity(InsertStudentCetDTO insertStudentCetDTO) {
        StudentCet studentCet = new StudentCet();
        BeanUtils.copyProperties(insertStudentCetDTO, studentCet);
        return studentCet;
    }
}

package edu.guet.studentworkmanagementsystem.service.academicWork.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkList;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.*;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.academicWork.*;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import edu.guet.studentworkmanagementsystem.utils.JsonUtil;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.StudentAcademicWorkTableDef.STUDENT_ACADEMIC_WORK;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class AcademicWorkServiceImpl extends ServiceImpl<StudentAcademicWorkMapper, StudentAcademicWork> implements AcademicWorkService {
    @Autowired
    private StudentPaperMapper paperMapper;
    @Autowired
    private StudentPatentMapper patentMapper;
    @Autowired
    private StudentSoftMapper softMapper;
    @Autowired
    private StudentAcademicWorkClaimMapper claimMapper;

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentAcademicWork(StudentAcademicWorkList studentAcademicWorkList) {
        List<StudentAcademicWorkDTO> studentAcademicWorkDTOList = studentAcademicWorkList.getStudentAcademicWorkDTOList();
        int size = studentAcademicWorkDTOList.size();
        ArrayList<StudentAcademicWork> studentAcademicWorks = new ArrayList<>();
        studentAcademicWorkDTOList.forEach(item -> {
            Long id = insertAcademicWork(item.getAcademicWork(), item.getAcademicWorkType());
            item.setAdditionalInfoId(id);
            try {
                studentAcademicWorks.add(new StudentAcademicWork(item));
            } catch (JsonProcessingException exception) {
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        });
        int i = mapper.insertBatch(studentAcademicWorks);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.JSON_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentAcademicWork(StudentAcademicWorkDTO studentAcademicWorkDTO) {
        Long id = insertAcademicWork(studentAcademicWorkDTO.getAcademicWork(), studentAcademicWorkDTO.getAcademicWorkType());
        studentAcademicWorkDTO.setAdditionalInfoId(id);
        try {
            StudentAcademicWork studentAcademicWork = new StudentAcademicWork(studentAcademicWorkDTO);
            int i = mapper.insert(studentAcademicWork);
            if (i > 0)
                return ResponseUtil.success();
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        } catch (JsonProcessingException exception) {
            throw new ServiceException(ServiceExceptionEnum.JSON_ERROR);
        }
    }

    @Override
    @Transactional
    public Long insertAcademicWork(AcademicWork academicWork, String typeId) {
        Long id = null;
        switch (typeId) {
            case "1" -> {
                StudentPaper studentPaper = (StudentPaper) academicWork;
                int i = paperMapper.insert(studentPaper);
                if (i > 0)
                    id = studentPaper.getStudentPaperId();
            }
            case "2" -> {
                StudentPatent studentPatent = (StudentPatent) academicWork;
                int i = patentMapper.insert(studentPatent);
                if (i > 0)
                    id = studentPatent.getStudentPatentId();
            }
            case "3" -> {
                StudentSoft studentSoft = (StudentSoft) academicWork;
                int i = softMapper.insert(studentSoft);
                if (i > 0)
                    id = studentSoft.getStudentSoftId();
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
        if (!Objects.isNull(id))
            return id;
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentAcademicWork(String studentAcademicWorkId) {
        QueryWrapper wrapper = QueryWrapper.create().where(STUDENT_ACADEMIC_WORK.STUDENT_ACADEMIC_WORK_ID.eq(studentAcademicWorkId));
        long claimNumber = claimMapper.selectCountByQuery(wrapper);
        int i = claimMapper.deleteByQuery(wrapper);
        if (i == claimNumber) {
            StudentAcademicWork studentAcademicWork = mapper.selectOneById(studentAcademicWorkId);
            deleteAcademicWork(studentAcademicWork.getAdditionalInfoId(), studentAcademicWork.getAcademicWorkType());
            int j = mapper.deleteById(studentAcademicWorkId);
            if (j > 0)
                return ResponseUtil.success();
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<StudentAcademicWorkVO>> getOwnStudentAcademicWork(String studentId) {
        List<StudentAcademicWorkVO> studentAcademicWorks = QueryChain.of(StudentAcademicWork.class)
                .select(STUDENT_ACADEMIC_WORK.ALL_COLUMNS, STUDENT.ALL_COLUMNS)
                .where(STUDENT_ACADEMIC_WORK.STUDENT_ID.eq(studentId))
                .listAs(StudentAcademicWorkVO.class);
        studentAcademicWorks.forEach(item -> {
            AcademicWork academicWork = getAcademicWork(item.getAcademicWorkType(), item.getAdditionalInfoId());
            item.setAcademicWork(academicWork);
        });
        return ResponseUtil.success(studentAcademicWorks);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> auditStudentAcademicWork(AcademicWorkAuditDTO academicWorkAuditDTO) throws JsonProcessingException {
        String studentAcademicWorkId = academicWorkAuditDTO.getStudentAcademicWorkId();
        boolean update = UpdateChain.of(StudentAcademicWork.class)
                .set(STUDENT_ACADEMIC_WORK.AUDITOR_ID, academicWorkAuditDTO.getAuditorId(), StringUtils::hasLength)
                .set(STUDENT_ACADEMIC_WORK.AUDIT_STATE, academicWorkAuditDTO.getAuditState(), StringUtils::hasLength)
                .set(STUDENT_ACADEMIC_WORK.REASON, academicWorkAuditDTO.getReason(), StringUtils::hasLength)
                .where(STUDENT_ACADEMIC_WORK.STUDENT_ACADEMIC_WORK_ID.eq(studentAcademicWorkId))
                .update();
        Boolean flag = stateHandler(academicWorkAuditDTO.getAuditState());
        if (update) {
            if (!flag)
                return ResponseUtil.success();
            String authorsStr = mapper.selectOneById(studentAcademicWorkId).getAuthors();
            Authors authors = JsonUtil.mapper.readValue(authorsStr, Authors.class);
            return insertStudentAcademicWorkAudit(authors, studentAcademicWorkId);
        }
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentAcademicWorkAudit(Authors authors, String studentAcademicWorkId) {
        ArrayList<StudentAcademicWorkClaim> studentAcademicWorkClaims = new ArrayList<>();
        int size = authors.getAuthors().size();
        authors.getAuthors().forEach(item -> {
            StudentAcademicWorkClaim claim = new StudentAcademicWorkClaim(studentAcademicWorkId, item.getStudentId());
            studentAcademicWorkClaims.add(claim);
        });
        int i = claimMapper.insertBatch(studentAcademicWorkClaims);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentAcademicWorkVO>> getAllStudentAcademicWork(AcademicWorkQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentAcademicWorkVO> studentAcademicWorkVOPage = QueryChain.of(StudentAcademicWork.class)
                .select(STUDENT_ACADEMIC_WORK.ALL_COLUMNS, STUDENT.ALL_COLUMNS)
                .where(STUDENT_ACADEMIC_WORK.STUDENT_ID.eq(query.getStudentId()))
                .and(STUDENT.NAME.like(query.getName()))
                .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT_ACADEMIC_WORK.UPLOAD_TIME.eq(query.getUploadTime()))
                .pageAs(Page.of(pageNo, pageSize), StudentAcademicWorkVO.class);
        studentAcademicWorkVOPage.getRecords().forEach(item -> {
            AcademicWork academicWork = getAcademicWork(item.getAcademicWorkType(), item.getAdditionalInfoId());
            item.setAcademicWork(academicWork);
        });
        return ResponseUtil.success(studentAcademicWorkVOPage);
    }

    private AcademicWork getAcademicWork(String typeId, String additionalInfoId) {
        switch (typeId) {
            case "1" -> {
                return paperMapper.selectOneById(additionalInfoId);
            }
            case "2" -> {
                return patentMapper.selectOneById(additionalInfoId);
            }
            case "3" -> {
                return softMapper.selectOneById(additionalInfoId);
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
    }

    private void deleteAcademicWork(String additionalInfoId, String typeId) {
        int i;
        switch (typeId) {
            case "1" -> {
                i = paperMapper.deleteById(additionalInfoId);
            }
            case "2" -> {
                i = patentMapper.deleteById(additionalInfoId);
            }
            case "3" -> {
                i = softMapper.deleteById(additionalInfoId);
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
        if (i > 0)
            return;
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    private Boolean stateHandler(String state) {
        switch (state) {
            case "已通过" -> {
                return true;
            }
            case "已拒绝" -> {
                return false;
            }
            default -> throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        }
    }
}

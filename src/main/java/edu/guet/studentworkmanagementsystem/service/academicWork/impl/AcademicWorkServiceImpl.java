package edu.guet.studentworkmanagementsystem.service.academicWork.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.StudentAcademicWorkList;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.*;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkVO;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.academicWork.*;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentAcademicWork(StudentAcademicWorkDTO studentAcademicWorkDTO) {
        return null;
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
        return null;
    }

    @Override
    public BaseResponse<List<StudentAcademicWorkVO>> getOwnStudentAcademicWork(String studentId) {
        return null;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> auditStudentAcademicWork(AcademicWorkAuditDTO academicWorkAuditDTO) {
        return null;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentAcademicWorkAudit(Authors authors, String studentAcademicWorkId) {
        return null;
    }

    @Override
    public BaseResponse<Page<StudentCompetitionVO>> getAllStudentAcademicWork(AcademicWorkQuery query) {
        return null;
    }
}

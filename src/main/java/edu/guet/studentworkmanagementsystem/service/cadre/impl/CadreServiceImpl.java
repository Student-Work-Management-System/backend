package edu.guet.studentworkmanagementsystem.service.cadre.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.*;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.cadre.CadreMapper;
import edu.guet.studentworkmanagementsystem.mapper.cadre.StudentCadreMapper;
import edu.guet.studentworkmanagementsystem.service.cadre.CadreService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.cadre.table.CadreTableDef.CADRE;
import static edu.guet.studentworkmanagementsystem.entity.po.cadre.table.StudentCadreTableDef.STUDENT_CADRE;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class CadreServiceImpl extends ServiceImpl<StudentCadreMapper, StudentCadre>  implements CadreService{
    @Autowired
    private CadreMapper cadreMapper ;
    @Override
    @Transactional
    public <T> BaseResponse<T> importCadres(CadreList cadreList) {
        List<Cadre> cadres = cadreList.getCadres();
        int size = cadres.size();
        int i = cadreMapper.insertBatch(cadres);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public <T> BaseResponse<T> insertCadre(Cadre cadre) {
        int i = cadreMapper.insert(cadre);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public  <T> BaseResponse<T> updateCadre(CadreDTO cadreDTO) {
        if (Objects.isNull(cadreDTO.getCadreId()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        boolean update = UpdateChain.of(Cadre.class)
                .set(Cadre::getCadrePosition, cadreDTO.getCadrePosition(), StringUtils.hasLength(cadreDTO.getCadrePosition()))
                .set(Cadre::getCadreLevel, cadreDTO.getCadreLevel(), StringUtils.hasLength(cadreDTO.getCadreLevel()))
                .where(Cadre::getCadreId).eq(cadreDTO.getCadreId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<List<Cadre>> getAllCadres() {
        return ResponseUtil.success(cadreMapper.selectAll());
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteCadre(String cadreId) {
        int i = cadreMapper.deleteById(cadreId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public  BaseResponse<StudentCadre> arrangePosition(InsertStudentCadreDTO insertStudentCadreDTO) {
        StudentCadre studentCadre = new StudentCadre(insertStudentCadreDTO);
        int i = mapper.insert(studentCadre);
        if (i > 0)
            return ResponseUtil.success(studentCadre);
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Transactional
    @Override
    public BaseResponse<StudentCadre> arrangePositions(InsertStudentCadreList insertStudentCadreList) {
        ArrayList<StudentCadre> studentCadres = new ArrayList<>();  
        insertStudentCadreList.getInsertStudentCadreDTOList().forEach(item->{
            StudentCadre studentCadre = new StudentCadre(item);
            studentCadres.add(studentCadre);
        });
        int size = studentCadres.size();
        int i = mapper.insertBatch(studentCadres);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentCadre(UpdateStudentCadreDTO updateStudentCadreDTO) {
           boolean update = UpdateChain.of(StudentCadre.class)
                   .set(StudentCadre::getStudentId, updateStudentCadreDTO.getStudentId(), StringUtils.hasLength(updateStudentCadreDTO.getStudentId()))
                   .set(StudentCadre::getCadreId, updateStudentCadreDTO.getCadreId(), StringUtils.hasLength(updateStudentCadreDTO.getCadreId()))
                   .set(StudentCadre::getAppointmentStartTerm, updateStudentCadreDTO.getAppointmentStartTerm(), StringUtils.hasLength(updateStudentCadreDTO.getAppointmentStartTerm()))
                   .set(StudentCadre::getAppointmentEndTerm, updateStudentCadreDTO.getAppointmentEndTerm(), StringUtils.hasLength(updateStudentCadreDTO.getAppointmentEndTerm()))
                   .set(StudentCadre::getComment, updateStudentCadreDTO.getComment(), StringUtils.hasLength(updateStudentCadreDTO.getComment()))
                   .where(StudentCadre::getStudentCadreId).eq(updateStudentCadreDTO.getStudentCadreId())
                   .update();
           if (update)
               return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCadre(String studentCadreId) {
        int i = mapper.deleteById(studentCadreId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentCadreVO>> getAllStudentAcademicWork(CadreQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentCadreVO> studentCadreVOPage = QueryChain.of(StudentCadre.class)
                .select(STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS, CADRE.ALL_COLUMNS, STUDENT_CADRE.ALL_COLUMNS)
                .from(STUDENT_CADRE)
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_CADRE.STUDENT_ID))
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                .innerJoin(CADRE).on(CADRE.CADRE_ID.eq(STUDENT_CADRE.CADRE_ID))
                .where(Student::getStudentId).eq(query.getStudentId())
                .and(Student::getMajorId).eq(query.getMajorId())
                .and(Student::getGrade).eq(query.getGrade())
                .and(Student::getName).like(query.getName())
                .and(Cadre::getCadreLevel).like(query.getCadreLevel())
                .and(Cadre::getCadrePosition).like(query.getCadrePosition())
                .and(StudentCadre::getAppointmentStartTerm).like(query.getAppointmentStartTerm())
                .and(StudentCadre::getAppointmentEndTerm).like(query.getAppointmentEndTerm())
                .pageAs(Page.of(pageNo, pageSize), StudentCadreVO.class);
        return ResponseUtil.success(studentCadreVOPage);
    }
}

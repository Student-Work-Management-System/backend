package edu.guet.studentworkmanagementsystem.service.cadre.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.StudentCadreDTO;
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
    public BaseResponse<List<Cadre>> importCadres(List<Cadre> cadres) {
        int size = cadres.size();
        int[] res = Db.executeBatch(cadres, 1000, CadreMapper.class, (cadreMapper, cadre) -> {
            cadreMapper.insert(cadre);
        });
        if (size == res.length)
            return ResponseUtil.success(cadres);
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Cadre> insertCadre(Cadre cadre) {
        int i = cadreMapper.insert(cadre);
        if (i > 0)
            return ResponseUtil.success(cadre);
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public  <T> BaseResponse<T> updateCadre(CadreDTO cadreDTO) {
        if (Objects.isNull(cadreDTO.getCadreId()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        boolean update = UpdateChain.of(Cadre.class)
                .set(Cadre::getCadrePosition, cadreDTO.getCadrePosition(),StringUtils.hasLength(cadreDTO.getCadrePosition()))
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
    public  BaseResponse<StudentCadre> arrangePositions(StudentCadre studentCadre) {
        Cadre cadre = !Objects.isNull(studentCadre.getCadreId())? cadreMapper.selectOneById(studentCadre.getCadreId()) : null  ;
        if (cadre == null || !StringUtils.hasLength(studentCadre.getStudentId())) {
            throw new ServiceException(ServiceExceptionEnum.NOT_RESOURCE) ;
        }
        int i = mapper.insert(studentCadre);
        if (i > 0)
            return ResponseUtil.success(studentCadre);
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentCadreInfo(StudentCadreDTO studentCadreDTO) {

           boolean update = UpdateChain.of(StudentCadre.class)
                   .set(StudentCadre::getAppointmentStartTerm, studentCadreDTO.getAppointmentStartTerm(), StringUtils.hasLength(studentCadreDTO.getAppointmentStartTerm()))
                   .set(StudentCadre::getAppointmentEndTerm, studentCadreDTO.getAppointmentEndTerm(), StringUtils.hasLength(studentCadreDTO.getAppointmentEndTerm()))
                   .set(StudentCadre::getComment, studentCadreDTO.getComment(), StringUtils.hasLength(studentCadreDTO.getComment()))
                   .where(StudentCadre::getStudentCadreId).eq(studentCadreDTO.getStudentCadreId())
                   .update();
           if (update)
               return ResponseUtil.success();


        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentCadre( String studentCadreId, String newCadreId) {
        Cadre cadre =  StringUtils.hasLength(newCadreId)?cadreMapper.selectOneById(newCadreId) : null ;
        if (cadre == null) {
            throw new ServiceException(ServiceExceptionEnum.NOT_RESOURCE) ;
        }
        boolean update = UpdateChain.of(StudentCadre.class)
                .set(StudentCadre::getCadreId, newCadreId)
                .where(StudentCadre::getStudentCadreId).eq(studentCadreId)
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCadre(String studentCadreId) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.where(STUDENT_CADRE.STUDENT_CADRE_ID.eq(studentCadreId));
        int i = mapper.deleteByQuery(queryWrapper) ;
        if (i>0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentCadreVO>> getAllStudentAcademicWork(CadreQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        QueryChain<StudentCadre> queryChain = QueryChain.of(StudentCadre.class)
                .select(STUDENT.ALL_COLUMNS,
                        MAJOR.ALL_COLUMNS,
                        CADRE.ALL_COLUMNS,
                        STUDENT_CADRE.ALL_COLUMNS)
                .from(STUDENT_CADRE)
                .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_CADRE.STUDENT_ID))
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                .innerJoin(CADRE).on(CADRE.CADRE_ID.eq(STUDENT_CADRE.CADRE_ID))
                .where(Student::getIdNumber).like("%");

        Optional.ofNullable(query)
                .ifPresent(q -> {
                    Optional.ofNullable(q.getGrade()).filter(StringUtils::hasLength).ifPresent(grade -> queryChain.and(Student::getGrade).eq(grade));
                    Optional.ofNullable(q.getMajorId()).filter(StringUtils::hasLength).ifPresent(majorId -> queryChain.and(Student::getMajorId).eq(majorId));
                    Optional.ofNullable(q.getStudentId()).filter(StringUtils::hasLength).ifPresent(studentId -> queryChain.and(Student::getStudentId).eq(studentId));
                    Optional.ofNullable(q.getName()).filter(StringUtils::hasLength).ifPresent(name -> queryChain.and(Student::getName).like(name));
                    Optional.ofNullable(q.getCadrePosition()).filter(StringUtils::hasLength).ifPresent(cadrePosition -> queryChain.and(Cadre::getCadrePosition).eq(cadrePosition));
                    Optional.ofNullable(q.getAppointmentStartTerm()).filter(StringUtils::hasLength).ifPresent(startTerm -> queryChain.and(StudentCadre::getAppointmentStartTerm).eq(startTerm));
                    Optional.ofNullable(q.getAppointmentEndTerm()).filter(StringUtils::hasLength).ifPresent(endTerm -> queryChain.and(StudentCadre::getAppointmentEndTerm).eq(endTerm));
                });


        Page<StudentCadreVO> studentCadreVOPage = queryChain.pageAs(Page.of(pageNo, pageSize), StudentCadreVO.class);

        return ResponseUtil.success(studentCadreVOPage);

    }
}

package edu.guet.studentworkmanagementsystem.service.cadre.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static edu.guet.studentworkmanagementsystem.entity.po.cadre.table.CadreTableDef.CADRE;
import static edu.guet.studentworkmanagementsystem.entity.po.cadre.table.StudentCadreTableDef.STUDENT_CADRE;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class CadreServiceImpl extends ServiceImpl<StudentCadreMapper, StudentCadre>  implements CadreService{
    @Autowired
    private CadreMapper cadreMapper ;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

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
        CompletableFuture<BaseResponse<List<Cadre>>> future = CompletableFuture.supplyAsync(() -> ResponseUtil.success(cadreMapper.selectAll()), readThreadPool);
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception exception) {
            Throwable cause = exception.getCause();
            switch (cause) {
                case ServiceException serviceException ->
                        throw serviceException;
                case TimeoutException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_TIMEOUT);
                case InterruptedException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_INTERRUPTED);
                default ->
                        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteCadre(String cadreId) {
        QueryWrapper queryWrapper = QueryWrapper.create().where(STUDENT_CADRE.CADRE_ID.eq(cadreId));
        long size = count(queryWrapper);
        int rows = mapper.deleteByQuery(queryWrapper);
        if (rows != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
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
                   .set(StudentCadre::getStudentId, updateStudentCadreDTO.getStudentId(), StringUtils::hasLength)
                   .set(StudentCadre::getCadreId, updateStudentCadreDTO.getCadreId(), StringUtils::hasLength)
                   .set(StudentCadre::getAppointmentStartTerm, updateStudentCadreDTO.getAppointmentStartTerm(), StringUtils::hasLength)
                   .set(StudentCadre::getAppointmentEndTerm, updateStudentCadreDTO.getAppointmentEndTerm(), StringUtils::hasLength)
                   .set(StudentCadre::getComment, updateStudentCadreDTO.getComment(), StringUtils::hasLength)
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
        CompletableFuture<BaseResponse<Page<StudentCadreVO>>> future = CompletableFuture.supplyAsync(() -> {
            Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
            Page<StudentCadreVO> studentCadreVOPage = QueryChain.of(StudentCadre.class)
                    .select(STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS, CADRE.ALL_COLUMNS, STUDENT_CADRE.ALL_COLUMNS)
                    .from(STUDENT_CADRE)
                    .innerJoin(STUDENT).on(STUDENT.STUDENT_ID.eq(STUDENT_CADRE.STUDENT_ID).and(STUDENT.ENABLED.eq(true)))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT.MAJOR_ID))
                    .innerJoin(CADRE).on(CADRE.CADRE_ID.eq(STUDENT_CADRE.CADRE_ID))
                    .where(STUDENT.STUDENT_ID.like(query.getSearch())
                            .or(STUDENT.NAME.like(query.getSearch()))
                            .or(CADRE.CADRE_POSITION.like(query.getSearch())))
                    .and(Student::getMajorId).eq(query.getMajorId())
                    .and(Student::getGrade).eq(query.getGrade())
                    .and(Cadre::getCadreLevel).eq(query.getCadreLevel())
                    .and(StudentCadre::getAppointmentStartTerm).eq(query.getAppointmentStartTerm())
                    .and(StudentCadre::getAppointmentEndTerm).eq(query.getAppointmentEndTerm())
                    .pageAs(Page.of(pageNo, pageSize), StudentCadreVO.class);
            return ResponseUtil.success(studentCadreVOPage);
        }, readThreadPool);
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception exception) {
            Throwable cause = exception.getCause();
            switch (cause) {
                case ServiceException serviceException ->
                        throw serviceException;
                case TimeoutException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_TIMEOUT);
                case InterruptedException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_INTERRUPTED);
                default ->
                        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        }
    }
}

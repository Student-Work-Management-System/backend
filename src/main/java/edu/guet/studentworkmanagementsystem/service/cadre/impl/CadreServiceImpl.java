package edu.guet.studentworkmanagementsystem.service.cadre.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.*;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreItem;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.cadre.CadreMapper;
import edu.guet.studentworkmanagementsystem.mapper.cadre.StudentCadreMapper;
import edu.guet.studentworkmanagementsystem.service.cadre.CadreService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.cadre.table.CadreTableDef.CADRE;
import static edu.guet.studentworkmanagementsystem.entity.po.cadre.table.StudentCadreTableDef.STUDENT_CADRE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

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
    public  <T> BaseResponse<T> updateCadre(Cadre cadre) {
        boolean update = UpdateChain.of(Cadre.class)
                .set(Cadre::getCadrePosition, cadre.getCadrePosition(), StringUtils.hasLength(cadre.getCadrePosition()))
                .set(Cadre::getCadreLevel, cadre.getCadreLevel(), StringUtils.hasLength(cadre.getCadreLevel()))
                .set(Cadre::getCadreBelong, cadre.getCadreBelong(), StringUtils.hasLength(cadre.getCadreBelong()))
                .where(Cadre::getCadreId).eq(cadre.getCadreId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<Cadre>> getAllCadres() {
        CompletableFuture<BaseResponse<List<Cadre>>> future =
                CompletableFuture.supplyAsync(() -> ResponseUtil.success(cadreMapper.selectAll()), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
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
    public  BaseResponse<StudentCadre> arrangePosition(StudentCadre studentCadre) {
        int i = mapper.insert(studentCadre);
        if (i > 0)
            return ResponseUtil.success(studentCadre);
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Transactional
    @Override
    public BaseResponse<StudentCadre> arrangePositions(ValidateList<StudentCadre> studentCadreList) {
        int size = studentCadreList.size();
        int i = mapper.insertBatch(studentCadreList);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentCadre(StudentCadre studentCadre) {
           boolean update = UpdateChain.of(StudentCadre.class)
                   .set(StudentCadre::getStudentId, studentCadre.getStudentId(), StringUtils::hasLength)
                   .set(StudentCadre::getCadreId, studentCadre.getCadreId(), StringUtils::hasLength)
                   .set(StudentCadre::getAppointmentStartTerm, studentCadre.getAppointmentStartTerm(), StringUtils::hasLength)
                   .set(StudentCadre::getAppointmentEndTerm, studentCadre.getAppointmentEndTerm(), StringUtils::hasLength)
                   .set(StudentCadre::getComment, studentCadre.getComment(), StringUtils::hasLength)
                   .where(StudentCadre::getStudentCadreId).eq(studentCadre.getStudentCadreId())
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
    public BaseResponse<Page<StudentCadreItem>> getAllStudentCadre(CadreQuery query) {
        CompletableFuture<Page<StudentCadreItem>> future = CompletableFuture.supplyAsync(() -> {
            Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
            return QueryChain.of(StudentCadre.class)
                    .select(
                            STUDENT_BASIC.STUDENT_ID,
                            STUDENT_BASIC.NAME,
                            STUDENT_BASIC.GENDER,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME,
                            CADRE.ALL_COLUMNS,
                            STUDENT_CADRE.STUDENT_CADRE_ID,
                            STUDENT_CADRE.APPOINTMENT_START_TERM,
                            STUDENT_CADRE.APPOINTMENT_END_TERM,
                            STUDENT_CADRE.COMMENT
                            )
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_CADRE.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(CADRE).on(CADRE.CADRE_ID.eq(STUDENT_CADRE.CADRE_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .where(
                            STUDENT_BASIC.STUDENT_ID.like(query.getSearch())
                                    .or(STUDENT_BASIC.NAME.like(query.getSearch()))
                                    .or(CADRE.CADRE_POSITION.like(query.getSearch()))
                    )
                    .and(STUDENT_BASIC.MAJOR_ID.eq(query.getMajorId()))
                    .and(STUDENT_BASIC.GRADE_ID.eq(query.getGradeId()))
                    .and(STUDENT_BASIC.ENABLED.eq(query.getEnabled()))
                    .and(StudentCadre::getAppointmentStartTerm).eq(query.getAppointmentStartTerm())
                    .and(StudentCadre::getAppointmentEndTerm).eq(query.getAppointmentEndTerm())
                    .pageAs(Page.of(pageNo, pageSize), StudentCadreItem.class);
        }, readThreadPool);
        Page<StudentCadreItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<List<StudentCadreStatItem>> getCadreStatus(CadreStatQuery query) {
        // todo: 完成mapper内实现
        CompletableFuture<List<StudentCadreStatItem>> future =
                CompletableFuture.supplyAsync(() -> mapper.getCadreStatus(query), readThreadPool);
        List<StudentCadreStatItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}

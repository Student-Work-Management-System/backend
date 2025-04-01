package edu.guet.studentworkmanagementsystem.service.other.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.po.other.*;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.authority.UserRoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.*;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


import static com.mybatisflex.core.query.QueryMethods.distinct;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.CounselorTableDef.COUNSELOR;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserRoleTableDef.USER_ROLE;

@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private DegreeMapper degreeMapper;
    @Autowired
    private PoliticMapper politicMapper;
    @Autowired
    private CounselorMapper counselorMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    public BaseResponse<List<Grade>> getAllGrades() {
        return ResponseUtil.success(getGradeList());
    }

    @Override
    public List<Grade> getGradeList() {
        CompletableFuture<List<Grade>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(Grade.class)
                .orderBy(Grade::getGradeName)
                .asc()
                .list(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addGrade(Grade grade) {
        List<Grade> list = QueryChain.of(Grade.class)
                .where(GRADE.GRADE_NAME.eq(grade.getGradeName().trim()))
                .list();
        if (!list.isEmpty())
            throw new ServiceException(
                    ServiceExceptionEnum.METHOD_ARGUMENT_NOT_VALID.getCode(),
                    ServiceExceptionEnum.METHOD_ARGUMENT_NOT_VALID.getMsg() + "输入的内容已经存在"
            );
        int i = gradeMapper.insert(grade);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<Degree>> getAllDegrees() {
        return ResponseUtil.success(getDegreeList());
    }

    @Override
    public List<Degree> getDegreeList() {
        CompletableFuture<List<Degree>> future = CompletableFuture.supplyAsync(() -> degreeMapper.selectAll(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addDegree(Degree degree) {
        List<Degree> list = QueryChain.of(Degree.class)
                .where(DEGREE.DEGREE_NAME.eq(degree.getDegreeName().trim()))
                .list();
        if (!list.isEmpty())
            throw new ServiceException(
                    ServiceExceptionEnum.METHOD_ARGUMENT_NOT_VALID.getCode(),
                    ServiceExceptionEnum.METHOD_ARGUMENT_NOT_VALID.getMsg() + "输入的内容已经存在"
            );
        int i = degreeMapper.insert(degree);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<Politic>> getAllPolitics() {
        return ResponseUtil.success(getPoliticList());
    }

    @Override
    public List<Politic> getPoliticList() {
        CompletableFuture<List<Politic>> future = CompletableFuture.supplyAsync(() -> politicMapper.selectAll(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addMajor(Major major) {
        if (Objects.isNull(major.getMajorId()) || Objects.isNull(major.getMajorName()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        int i = majorMapper.insert(major);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateMajor(Major major) {
        int update = majorMapper.update(major);
        if (update > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<Major>> getMajors() {
        return ResponseUtil.success(getMajorList());
    }

    @Override
    public List<Major> getMajorList() {
        CompletableFuture<List<Major>> future = CompletableFuture.supplyAsync(() -> majorMapper.selectAll(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteMajor(String majorId) {
        int i = majorMapper.deleteById(majorId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<CounselorItem>> getAllCounselors(CounselorQuery query) {
        CompletableFuture<Page<CounselorItem>> future = CompletableFuture.supplyAsync(() -> getPageCounselors(query), readThreadPool);
        Page<CounselorItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public Page<CounselorItem> getPageCounselors(CounselorQuery query) {
        int pageNo = Optional.of(query.getPageNo()).orElse(1);
        int pageSize = Optional.of(query.getPageSize()).orElse(10);
        QueryWrapper countWrapper = QueryWrapper.create().select(distinct(COUNSELOR.UID));
        long totalRow = counselorMapper.selectCountByQuery(countWrapper);
        System.out.println("totalRow = " + totalRow);
        int offset = (pageNo - 1) * pageSize;
        List<CounselorItem> counselors = counselorMapper.getCounselors(query.getSearch(), query.getGradeId(), query.getDegreeId(), offset, pageSize);
        counselors.forEach(it -> {
            String uid = it.getUid();
            List<Degree> degreeList = QueryChain.of(Degree.class)
                    .select(DEGREE.DEGREE_NAME)
                    .innerJoin(COUNSELOR).on(COUNSELOR.DEGREE_ID.eq(DEGREE.DEGREE_ID))
                    .where(COUNSELOR.UID.eq(uid))
                    .list();
            it.setChargeDegree(degreeList.stream().map(Degree::getDegreeName).collect(Collectors.toSet()));
            List<Grade> gradeList = QueryChain.of(Grade.class)
                    .select(GRADE.GRADE_NAME)
                    .innerJoin(COUNSELOR).on(COUNSELOR.GRADE_ID.eq(GRADE.GRADE_ID))
                    .where(COUNSELOR.UID.eq(uid))
                    .list();
            it.setChargeGrade(gradeList.stream().map(Grade::getGradeName).collect(Collectors.toSet()));
        });
        return new Page<>(counselors, pageNo, pageSize, totalRow);
    }

    @Override
    public <T> BaseResponse<T> deleteCounselor(String uid) {
        QueryWrapper deleteCounselorWrapper = QueryWrapper.create().where(COUNSELOR.UID.eq(uid));
        int i = counselorMapper.deleteByQuery(deleteCounselorWrapper);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        QueryWrapper deleteUserRoleWrapper = QueryWrapper.create().where(USER_ROLE.UID.eq(uid)).and(USER_ROLE.RID.eq(3));
        int j = userRoleMapper.deleteByQuery(deleteUserRoleWrapper);
        if  (j <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }
}

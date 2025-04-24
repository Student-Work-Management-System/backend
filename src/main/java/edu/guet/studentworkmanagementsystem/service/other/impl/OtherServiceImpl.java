package edu.guet.studentworkmanagementsystem.service.other.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorRequest;
import edu.guet.studentworkmanagementsystem.entity.po.other.*;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import edu.guet.studentworkmanagementsystem.entity.vo.other.UserWithCounselorRole;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.authority.UserRoleMapper;
import edu.guet.studentworkmanagementsystem.mapper.other.*;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import edu.guet.studentworkmanagementsystem.utils.SetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


import static com.mybatisflex.core.query.QueryMethods.distinct;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;
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

    @Override
    public BaseResponse<List<UserWithCounselorRole>> getOptionalCounselors(String gradeId) {
        CompletableFuture<List<UserWithCounselorRole>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(User.class)
                .select(USER.ALL_COLUMNS)
                .from(USER)
                .innerJoin(USER_ROLE).on(USER_ROLE.UID.eq(USER.UID).and(USER_ROLE.RID.eq(3)))
                .innerJoin(COUNSELOR).on(COUNSELOR.UID.eq(USER.UID).and(USER.ENABLED.eq(true)))
                .and(COUNSELOR.GRADE_ID.eq(gradeId))
                .listAs(UserWithCounselorRole.class), readThreadPool);
        List<UserWithCounselorRole> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateCounselor(CounselorRequest request) {
        String uid = request.getUid();
        List<Counselor> counselors = QueryChain.of(Counselor.class)
                .where(COUNSELOR.UID.eq(uid))
                .list();
        // 集合操作获取需要移除和新增的id
        Set<String> chargeGradeFromDB = counselors.stream().map(Counselor::getGradeId).collect(Collectors.toSet());
        Set<String> chargeGrade = request.getChargeGrade();
        Set<String> gradeIntersection = SetUtil.intersection(chargeGradeFromDB, chargeGrade);
        // 单独处理
        removeHandler(uid, chargeGradeFromDB, chargeGrade);
        addHandler(uid, chargeGrade, gradeIntersection);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<UserWithCounselorRole>> getOptionalCounselors() {
        CompletableFuture<List<UserWithCounselorRole>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(User.class)
                .select(USER.ALL_COLUMNS)
                .from(USER)
                .innerJoin(USER_ROLE).on(USER_ROLE.UID.eq(USER.UID).and(USER_ROLE.RID.eq(3)))
                .leftJoin(COUNSELOR).on(COUNSELOR.UID.eq(USER.UID))
                .where(COUNSELOR.UID.isNull())
                .listAs(UserWithCounselorRole.class), readThreadPool);
        List<UserWithCounselorRole> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addCounselors(CounselorRequest request) {
        ArrayList<Counselor> counselors = new ArrayList<>();
        String uid = request.getUid();
        request.getChargeGrade().forEach(it -> {
            Counselor counselor = new Counselor(uid, it);
            counselors.add(counselor);
        });
        int i = counselorMapper.insertBatch(counselors);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Transactional
    public void removeHandler(String uid, Set<String> db, Set<String> web) {
        Set<String> remove = SetUtil.difference(db, web);
        remove.forEach((it) -> {
            Counselor counselor = new Counselor(uid, it);
            int delete = counselorMapper.delete(counselor);
            if (delete <= 0)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        });
    }

    @Transactional
    public void addHandler(String uid, Set<String> web, Set<String> intersection) {
        Set<String> add = SetUtil.difference(web, intersection);
        add.forEach((it) -> {
            Counselor counselor = new Counselor(uid, it);
            int insert = counselorMapper.insert(counselor);
            if (insert <= 0)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        });
    }

    public Page<CounselorItem> getPageCounselors(CounselorQuery query) {
        int pageNo = Optional.of(query.getPageNo()).orElse(1);
        int pageSize = Optional.of(query.getPageSize()).orElse(10);
        if (Objects.isNull(query.getSearch())) query.setSearch("");
        QueryWrapper countWrapper = QueryWrapper.create()
                .select(distinct(COUNSELOR.UID))
                .leftJoin(USER).on(USER.UID.eq(COUNSELOR.UID).and(USER.ENABLED.eq(true)))
                .leftJoin(USER_ROLE).on(USER_ROLE.UID.eq(COUNSELOR.UID).and(USER_ROLE.RID.eq(3)))
                .where(USER.USERNAME.likeLeft(query.getSearch())
                        .or(USER.REAL_NAME.likeLeft(query.getSearch())));
        long totalRow = counselorMapper.selectCountByQuery(countWrapper);
        int offset = (pageNo - 1) * pageSize;
        List<CounselorItem> counselors = counselorMapper.getCounselors(query.getSearch(), offset, pageSize);
        counselors.forEach(it -> {
            String uid = it.getUid();
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
        return ResponseUtil.success();
    }
}

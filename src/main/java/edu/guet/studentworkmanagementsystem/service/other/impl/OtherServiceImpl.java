package edu.guet.studentworkmanagementsystem.service.other.impl;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.other.*;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
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
import java.util.concurrent.CompletableFuture;

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
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    public BaseResponse<List<Grade>> getAllGrades() {
        return ResponseUtil.success(getGradeList());
    }

    @Override
    public List<Grade> getGradeList() {
        CompletableFuture<List<Grade>> future = CompletableFuture.supplyAsync(() -> gradeMapper.selectAll(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
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
    public BaseResponse<List<Politic>> getAllPolitics() {
        return ResponseUtil.success(getPoliticList());
    }

    @Override
    public List<Politic> getPoliticList() {
        CompletableFuture<List<Politic>> future = CompletableFuture.supplyAsync(() -> politicMapper.selectAll(), readThreadPool);
        return FutureExceptionExecute.fromFuture(future).execute();
    }

    @Override
    public List<Counselor> getCounselorList() {
        CompletableFuture<List<Counselor>> future = CompletableFuture.supplyAsync(() -> counselorMapper.selectAll(), readThreadPool);
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
}

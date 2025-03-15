package edu.guet.studentworkmanagementsystem.service.major.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.other.Major;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.other.MajorMapper;
import edu.guet.studentworkmanagementsystem.service.major.MajorService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
    private final ThreadPoolTaskExecutor readThreadPool;

    public MajorServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addMajor(Major major) {
        if (Objects.isNull(major.getMajorId()) || Objects.isNull(major.getMajorName()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        int i = mapper.insert(major);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateMajor(Major major) {
        int update = mapper.update(major);
        if (update > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<Major>> getMajors() {
        CompletableFuture<BaseResponse<List<Major>>> future = CompletableFuture.supplyAsync(() -> ResponseUtil.success(mapper.selectAll()), readThreadPool);
        try {
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            Throwable cause = e.getCause();
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
    public <T> BaseResponse<T> deleteMajor(String majorId) {
        int i = mapper.deleteById(majorId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
}

package edu.guet.studentworkmanagementsystem.service.major.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.major.Major;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.major.MajorMapper;
import edu.guet.studentworkmanagementsystem.service.major.MajorService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
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
        if (Objects.isNull(major.getMajorId()))
            throw new ServiceException(ServiceExceptionEnum.KEY_ARGUMENT_NOT_INPUT);
        int update = mapper.update(major);
        if (update > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<List<Major>> getMajors() {
        return ResponseUtil.success(mapper.selectAll());
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

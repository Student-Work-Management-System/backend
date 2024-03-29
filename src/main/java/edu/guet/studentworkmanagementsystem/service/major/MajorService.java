package edu.guet.studentworkmanagementsystem.service.major;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.major.Major;

import java.util.List;

public interface MajorService extends IService<Major> {
    <T> BaseResponse<T> addMajor(Major major);
    <T> BaseResponse<T> updateMajor(Major major);
    BaseResponse<List<Major>> getMajors();
    <T> BaseResponse<T> deleteMajor(String majorId);
}

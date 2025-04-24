package edu.guet.studentworkmanagementsystem.service.other;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorRequest;
import edu.guet.studentworkmanagementsystem.entity.po.other.*;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import edu.guet.studentworkmanagementsystem.entity.vo.other.UserWithCounselorRole;

import java.util.List;

public interface OtherService {
    BaseResponse<List<Grade>> getAllGrades();
    List<Grade> getGradeList();
    <T> BaseResponse<T> addGrade(Grade grade);
    BaseResponse<List<Degree>> getAllDegrees();
    List<Degree> getDegreeList();
    <T> BaseResponse<T> addDegree(Degree degree);
    BaseResponse<List<Politic>> getAllPolitics();
    List<Politic> getPoliticList();
    <T> BaseResponse<T> addMajor(Major major);
    <T> BaseResponse<T> updateMajor(Major major);
    BaseResponse<List<Major>> getMajors();
    List<Major> getMajorList();
    <T> BaseResponse<T> deleteMajor(String majorId);
    BaseResponse<Page<CounselorItem>> getAllCounselors(CounselorQuery query);
    BaseResponse<List<UserWithCounselorRole>> getOptionalCounselors(String gradeId);
    <T> BaseResponse<T> deleteCounselor(String uid);
    <T> BaseResponse<T> updateCounselor(CounselorRequest counselor);
    BaseResponse<List<UserWithCounselorRole>> getOptionalCounselors();
    <T> BaseResponse<T> addCounselors(CounselorRequest request);
}

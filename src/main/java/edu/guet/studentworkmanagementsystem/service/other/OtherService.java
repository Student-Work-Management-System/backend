package edu.guet.studentworkmanagementsystem.service.other;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.other.*;

import java.util.List;

public interface OtherService {
    BaseResponse<List<Grade>> getAllGrades();
    List<Grade> getGradeList();
    BaseResponse<List<Degree>> getAllDegrees();
    List<Degree> getDegreeList();
    BaseResponse<List<Politic>> getAllPolitics();
    List<Politic> getPoliticList();
    <T> BaseResponse<T> addMajor(Major major);
    <T> BaseResponse<T> updateMajor(Major major);
    BaseResponse<List<Major>> getMajors();
    List<Major> getMajorList();
    <T> BaseResponse<T> deleteMajor(String majorId);
}

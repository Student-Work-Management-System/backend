package edu.guet.studentworkmanagementsystem.service.other;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.other.Counselor;
import edu.guet.studentworkmanagementsystem.entity.po.other.Degree;
import edu.guet.studentworkmanagementsystem.entity.po.other.Grade;
import edu.guet.studentworkmanagementsystem.entity.po.other.Politic;

import java.util.List;

public interface OtherService {
    BaseResponse<List<Grade>> getAllGrades();
    BaseResponse<List<Degree>> getAllDegrees();
    BaseResponse<List<Politic>> getAllPolitics();
    List<Counselor> getAllCounselors();
    List<Counselor> getCounselorByUid(String uid);
}

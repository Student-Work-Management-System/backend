package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentDetail;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentDetailMapper;
import edu.guet.studentworkmanagementsystem.service.student.StudentDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentDetailTableDef.STUDENT_DETAIL;

@Service
public class StudentDetailServiceImpl extends ServiceImpl<StudentDetailMapper, StudentDetail> implements StudentDetailService {
    @Transactional
    @Override
    public boolean importStudentDetail(List<StudentDetail> studentDetails) {
        int size = studentDetails.size();
        int i = mapper.insertBatch(studentDetails);
        return size == i;
    }
    @Transactional
    @Override
    public boolean updateStudentDetail(StudentDetail studentDetail) {
        return UpdateChain.of(StudentDetail.class)
                .set(STUDENT_DETAIL.HEAD_TEACHER_ID, studentDetail.getHeadTeacherId(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.MAJOR_ID, studentDetail.getMajorId(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.NATIVE_PLACE, studentDetail.getNativePlace(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.POSTAL_CODE, studentDetail.getPostalCode(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.NATION, studentDetail.getNation(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.POLITICS_STATUS, studentDetail.getPoliticsStatus(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.GRADE, studentDetail.getGrade(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.CLASS_NO, studentDetail.getClassNo(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.DORMITORY, studentDetail.getDormitory(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.BIRTHDATE, studentDetail.getBirthdate(), !Objects.isNull(studentDetail.getBirthdate()))
                .set(STUDENT_DETAIL.HOUSEHOLD_REGISTRATION, studentDetail.getHouseholdRegistration(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.HOUSEHOLD_TYPE, studentDetail.getHouseholdType(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.ADDRESS, studentDetail.getAddress(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.FATHER_NAME, studentDetail.getFatherName(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.FATHER_PHONE, studentDetail.getFatherPhone(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.FATHER_OCCUPATION, studentDetail.getFatherOccupation(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.MOTHER_NAME, studentDetail.getMotherName(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.MOTHER_PHONE, studentDetail.getMotherPhone(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.MOTHER_OCCUPATION, studentDetail.getMotherOccupation(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.GUARDIAN, studentDetail.getGuardian(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.GUARDIAN_PHONE, studentDetail.getGuardianPhone(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.HIGH_SCHOOL, studentDetail.getHighSchool(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.EXAM_ID, studentDetail.getExamId(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.ADMISSION_BATCH, studentDetail.getAdmissionBatch(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.TOTAL_EXAM_SCORE, studentDetail.getTotalExamScore(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.FOREIGN_LANGUAGE, studentDetail.getForeignLanguage(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.FOREIGN_SCORE, studentDetail.getForeignScore(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.HOBBIES, studentDetail.getHobbies(), StringUtils::hasLength)
                .set(STUDENT_DETAIL.OTHER_NOTES, studentDetail.getOtherNotes(), StringUtils::hasLength)
                .where(STUDENT_DETAIL.STUDENT_ID.eq(studentDetail.getStudentId()))
                .update();
    }
}

package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.entity.po.student.StudentBasic;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentBasicMapper;
import edu.guet.studentworkmanagementsystem.service.student.StudentBasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class StudentBasicServiceImpl extends ServiceImpl<StudentBasicMapper, StudentBasic> implements StudentBasicService {
    @Transactional
    @Override
    public boolean importStudentBasic(List<StudentBasic> studentBasics) {
        int size = studentBasics.size();
        int i = mapper.insertBatch(studentBasics);
        return size == i;
    }

    @Transactional
    @Override
    public boolean updateStudentBasic(StudentBasic studentBasic) {
        return UpdateChain.of(StudentBasic.class)
                .set(STUDENT_BASIC.NAME, studentBasic.getName(), StringUtils::hasLength)
                .set(STUDENT_BASIC.EMAIL, studentBasic.getEmail(), StringUtils::hasLength)
                .set(STUDENT_BASIC.PHONE, studentBasic.getPhone(), StringUtils::hasLength)
                .set(STUDENT_BASIC.ID_NUMBER, studentBasic.getIdNumber(), StringUtils::hasLength)
                .set(STUDENT_BASIC.DEGREE_ID, studentBasic.getDegreeId(), StringUtils::hasLength)
                .where(STUDENT_BASIC.STUDENT_ID.eq(studentBasic.getStudentId()))
                .update();
    }

    @Transactional
    @Override
    public boolean deleteStudentBasic(String studentId) {
        return UpdateChain.of(StudentBasic.class)
                .set(STUDENT_BASIC.ENABLED, false)
                .where(STUDENT_BASIC.STUDENT_ID.eq(studentId))
                .update();
    }

    @Transactional
    @Override
    public boolean recoveryStudentBasic(String studentId) {
        return UpdateChain.of(StudentBasic.class)
                .set(STUDENT_BASIC.ENABLED, true)
                .where(STUDENT_BASIC.STUDENT_ID.eq(studentId))
                .update();
    }
}

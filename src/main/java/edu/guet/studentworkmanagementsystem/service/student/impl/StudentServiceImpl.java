package edu.guet.studentworkmanagementsystem.service.student.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.enrollment.Enrollment;
import edu.guet.studentworkmanagementsystem.entity.po.student.*;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.enrollment.EnrollmentItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentArchive;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentBasicItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatItem;
import edu.guet.studentworkmanagementsystem.mapper.student.StudentMapper;
import edu.guet.studentworkmanagementsystem.service.enrollment.EnrollmentService;
import edu.guet.studentworkmanagementsystem.service.student.StudentBasicService;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import edu.guet.studentworkmanagementsystem.service.user.UserService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.RoleTableDef.ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserRoleTableDef.USER_ROLE;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    @Qualifier("readThreadPool")
    private ThreadPoolTaskExecutor readThreadPool;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentBasicService studentBasicService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private EnrollmentService enrollmentService;

    @Override
    @Transactional
    public <T> BaseResponse<T> importStudent(ValidateList<Enrollment> enrollments) {
        return enrollmentService.importEnrollment(enrollments);
    }

    @Override
    public BaseResponse<Page<EnrollmentItem>> getStudents(EnrollmentQuery query) {
        return enrollmentService.getAllRecords(query);
    }

    @Override
    public BaseResponse<EnrollmentItem> getOwnEnrollment(String studentId) {
        return enrollmentService.getOwnEnrollment(studentId);
    }


    @Override
    public BaseResponse<StudentArchive> getStudentArchive(String studentId) {
        return ResponseUtil.success();
    }

    /**
     * 更新学生档案信息
     * @param enrollment 学籍信息
     */
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudent(Enrollment enrollment) {
        return enrollmentService.updateEnrollment(enrollment);
    }

    /**
     * 根据学号(studentId)删除/恢复学生
     */
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudent(String studentId) {
        return enrollmentService.deleteEnrollment(studentId);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> recoveryStudent(String studentId) {
       return enrollmentService.recoveryEnrollment(studentId);
    }
    /**
     * 统计查询
     * @param query 查询类
     * @return 统计结果
     */
    @Override
    public BaseResponse<List<StudentStatItem>> getStudentStatus(StudentStatQuery query) {
        CompletableFuture<List<StudentStatItem>> future =
                CompletableFuture.supplyAsync(()-> studentMapper.getStudentStatusList(query), readThreadPool);
        List<StudentStatItem> list = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(list);
    }


    @Override
    public BaseResponse<List<HeaderTeacher>> getHeaderTeachers() {
        CompletableFuture<List<HeaderTeacher>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(User.class)
                .select(
                        USER.USERNAME.as("headerTeacherUsername"),
                        USER.REAL_NAME.as("headerTeacherRealName"),
                        USER.PHONE.as("headerTeacherPhone")
                )
                .innerJoin(USER_ROLE).on(USER.UID.eq(USER_ROLE.UID))
                .innerJoin(ROLE).on(ROLE.RID.eq(USER_ROLE.RID))
                .where(ROLE.ROLE_NAME.like(Common.HEADER_TEACHER.getValue()))
                .listAs(HeaderTeacher.class), readThreadPool);
        List<HeaderTeacher> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<List<StudentBasicItem>> getStudentBasic(String studentId) {
        CompletableFuture<List<StudentBasicItem>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(StudentBasic.class)
                .select(
                        STUDENT_BASIC.STUDENT_ID,
                        STUDENT_BASIC.NAME,
                        STUDENT_BASIC.GENDER,
                        MAJOR.MAJOR_NAME,
                        GRADE.GRADE_NAME,
                        DEGREE.DEGREE_NAME
                )
                .from(STUDENT_BASIC)
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                .where(STUDENT_BASIC.STUDENT_ID.likeLeft(studentId))
                .listAs(StudentBasicItem.class), readThreadPool);
        List<StudentBasicItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<List<StudentBasicItem>> getStudentCompetitionTeam(List<String> studentIds) {
        CompletableFuture<List<StudentBasicItem>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(StudentBasic.class)
                .select(
                        STUDENT_BASIC.STUDENT_ID,
                        STUDENT_BASIC.NAME,
                        STUDENT_BASIC.GENDER,
                        MAJOR.MAJOR_NAME,
                        GRADE.GRADE_NAME,
                        DEGREE.DEGREE_NAME
                )
                .from(STUDENT_BASIC)
                .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                .innerJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                .where(STUDENT_BASIC.STUDENT_ID.in(studentIds))
                .listAs(StudentBasicItem.class), readThreadPool);
        List<StudentBasicItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}

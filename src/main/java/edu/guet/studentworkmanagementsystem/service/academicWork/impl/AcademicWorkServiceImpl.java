package edu.guet.studentworkmanagementsystem.service.academicWork.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkMember;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.*;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkItem;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.StudentAcademicWorkMemberItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.academicWork.*;
import edu.guet.studentworkmanagementsystem.service.academicWork.AcademicWorkService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.StudentAcademicWorkAuditTableDef.STUDENT_ACADEMIC_WORK_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.StudentAcademicWorkMemberTableDef.STUDENT_ACADEMIC_WORK_MEMBER;
import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.StudentAcademicWorkTableDef.STUDENT_ACADEMIC_WORK;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

@Service
@Slf4j
public class AcademicWorkServiceImpl extends ServiceImpl<StudentAcademicWorkMapper, StudentAcademicWork> implements AcademicWorkService {
    @Autowired
    private StudentPaperMapper paperMapper;
    @Autowired
    private StudentPatentMapper patentMapper;
    @Autowired
    private StudentSoftMapper softMapper;
    @Autowired
    private StudentAcademicWorkAuditMapper auditMapper;
    @Autowired
    private StudentAcademicWorkMemberMapper memberMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;


    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentAcademicWork(AcademicWorkRequest request) {
        String referenceId = insertAcademicWork(request.getAcademicWork());
        StudentAcademicWork studentAcademicWork = createStudentAcademicWork(request, referenceId);
        int i = mapper.insert(studentAcademicWork);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        String studentAcademicWorkId = studentAcademicWork.getStudentAcademicWorkId();
        insertStudentAcademicWorkTeam(request.getTeam(), studentAcademicWorkId);
        return ResponseUtil.success();
    }

    @Transactional
    public String insertAcademicWork(AcademicWork academicWork) {
        int effect = -1;
        String id = "";
        if (academicWork instanceof StudentPaper studentPaper) {
            effect = paperMapper.insert(studentPaper);
            id = studentPaper.getStudentPaperId();
        } else if (academicWork instanceof StudentPatent studentPatent) {
            effect = patentMapper.insert(studentPatent);
            id = studentPatent.getStudentPatentId();
        } else if (academicWork instanceof StudentSoft studentSoft) {
            effect = softMapper.insert(studentSoft);
            id = studentSoft.getStudentSoftId();
        }
        if (effect <= 0 || !StringUtils.hasLength(id))
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return id;
    }

    public StudentAcademicWork createStudentAcademicWork(AcademicWorkRequest request, String referenceId) {
        return StudentAcademicWork.builder()
                .workName(request.getWorkName())
                .type(request.getType())
                .uid(request.getUid())
                .evidence(request.getEvidence())
                .referenceId(referenceId)
                .time(LocalDate.now())
                .build();
    }

    @Transactional
    public void insertStudentAcademicWorkTeam(List<AcademicWorkMember> members, String studentAcademicWorkId) {
        List<StudentAcademicWorkMember> studentAcademicWorkTeam = createStudentAcademicWorkTeam(members, studentAcademicWorkId);
        int i = memberMapper.insertBatch(studentAcademicWorkTeam);
        int size = studentAcademicWorkTeam.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    public List<StudentAcademicWorkMember> createStudentAcademicWorkTeam(List<AcademicWorkMember> members, String studentAcademicWorkId) {
        ArrayList<StudentAcademicWorkMember> studentAcademicWorkMembers = new ArrayList<>();
        members.forEach(member -> {
            StudentAcademicWorkMember build = StudentAcademicWorkMember.builder()
                    .studentAcademicWorkId(studentAcademicWorkId)
                    .uid(member.getUid())
                    .memberOrder(member.getMemberOrder())
                    .isStudent(member.getIsStudent())
                    .build();
            studentAcademicWorkMembers.add(build);
        });
        return studentAcademicWorkMembers;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentAcademicWork(String studentAcademicWorkId) {
        QueryWrapper deleteStudentAcademicWorkAuditWrapper =
                QueryWrapper.create()
                        .where(STUDENT_ACADEMIC_WORK_AUDIT.STUDENT_ACADEMIC_WORK_ID.eq(studentAcademicWorkId));
        int i = auditMapper.deleteByQuery(deleteStudentAcademicWorkAuditWrapper);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        QueryWrapper deleteStudentAcademicWorkMemberWrapper =
                QueryWrapper.create()
                        .where(STUDENT_ACADEMIC_WORK_MEMBER.STUDENT_ACADEMIC_WORK_ID.eq(studentAcademicWorkId));
        int j = memberMapper.deleteByQuery(deleteStudentAcademicWorkMemberWrapper);
        if (j <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        int k = mapper.deleteById(studentAcademicWorkId);
        if (k <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<StudentAcademicWorkItem>> getOwnStudentAcademicWork(String studentId) {
        CompletableFuture<List<StudentAcademicWorkItem>> future = CompletableFuture.supplyAsync(() -> {
            List<StudentAcademicWorkItem> items = QueryChain.of(StudentAcademicWork.class)
                    .select(
                            USER.ALL_COLUMNS,
                            STUDENT_ACADEMIC_WORK.ALL_COLUMNS,
                            STUDENT_ACADEMIC_WORK_AUDIT.ALL_COLUMNS
                    )
                    .from(STUDENT_ACADEMIC_WORK)
                    .innerJoin(USER).on(USER.UID.eq(STUDENT_ACADEMIC_WORK.UID))
                    .innerJoin(STUDENT_ACADEMIC_WORK_AUDIT).on(STUDENT_ACADEMIC_WORK_AUDIT.STUDENT_ACADEMIC_WORK_ID.eq(STUDENT_ACADEMIC_WORK.STUDENT_ACADEMIC_WORK_ID))
                    .where(USER.USERNAME.eq(studentId))
                    .listAs(StudentAcademicWorkItem.class);
            items.forEach(this::getStudentAcademicWorkTeam);
            return items;
        }, readThreadPool);
        List<StudentAcademicWorkItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public void getStudentAcademicWorkTeam(StudentAcademicWorkItem item) {
        String studentAcademicWorkId = item.getStudentAcademicWorkId();
        List<StudentAcademicWorkMemberItem> memberItems = QueryChain.of(StudentAcademicWorkMember.class)
                .select(
                        STUDENT_ACADEMIC_WORK_MEMBER.ALL_COLUMNS,
                        USER.USERNAME,
                        USER.REAL_NAME,
                        MAJOR.MAJOR_NAME,
                        GRADE.GRADE_NAME,
                        DEGREE.DEGREE_NAME
                )
                .from(STUDENT_ACADEMIC_WORK_MEMBER)
                .leftJoin(USER).on(USER.UID.eq(STUDENT_ACADEMIC_WORK_MEMBER.UID))
                .leftJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(USER.USERNAME))
                .leftJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                .leftJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                .leftJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                .where(STUDENT_ACADEMIC_WORK_MEMBER.STUDENT_ACADEMIC_WORK_ID.eq(studentAcademicWorkId))
                .listAs(StudentAcademicWorkMemberItem.class);
        item.setTeam(memberItems);
        String type = item.getType();
        String referenceId = item.getReferenceId();
        if (Common.PAPER.getValue().equals(type)) {
            StudentPaper studentPaper = paperMapper.selectOneById(referenceId);
            item.setAcademicWork(studentPaper);
        } else if (Common.SOFT.getValue().equals(type)) {
            StudentSoft studentSoft = softMapper.selectOneById(referenceId);
            item.setAcademicWork(studentSoft);
        } else if (Common.PATENT.getValue().equals(type)) {
            StudentPatent studentPatent = patentMapper.selectOneById(referenceId);
            item.setAcademicWork(studentPatent);
        }
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentAcademicWorkAudit(StudentAcademicWorkAudit audit)  {
        boolean update = UpdateChain.of(StudentAcademicWorkAudit.class)
                .set(STUDENT_ACADEMIC_WORK_AUDIT.STATE, audit.getState(), StringUtils::hasLength)
                .set(STUDENT_ACADEMIC_WORK_AUDIT.REJECT_REASON, audit.getRejectReason(), StringUtils::hasLength)
                .set(STUDENT_ACADEMIC_WORK_AUDIT.OPERATOR_ID, audit.getOperatorId(), StringUtils::hasLength)
                .set(STUDENT_ACADEMIC_WORK_AUDIT.OPERATOR_TIME, LocalDate.now())
                .where(STUDENT_ACADEMIC_WORK_AUDIT.STUDENT_ACADEMIC_WORK_ID.eq(audit.getStudentAcademicWorkId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<StudentAcademicWorkItem>> getAllStudentAcademicWork(AcademicWorkQuery query) {
        CompletableFuture<Page<StudentAcademicWorkItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            Page<StudentAcademicWorkItem> items = QueryChain.of(StudentAcademicWork.class)
                    .select(
                            USER.ALL_COLUMNS,
                            STUDENT_ACADEMIC_WORK.ALL_COLUMNS,
                            STUDENT_ACADEMIC_WORK_AUDIT.ALL_COLUMNS
                    )
                    .from(STUDENT_ACADEMIC_WORK)
                    .innerJoin(USER).on(USER.UID.eq(STUDENT_ACADEMIC_WORK.UID))
                    .innerJoin(STUDENT_ACADEMIC_WORK_AUDIT).on(STUDENT_ACADEMIC_WORK_AUDIT.STUDENT_ACADEMIC_WORK_ID.eq(STUDENT_ACADEMIC_WORK.STUDENT_ACADEMIC_WORK_ID))
                    .where(
                            USER.USERNAME.likeLeft(query.getSearch())
                                    .or(USER.REAL_NAME.likeLeft(query.getSearch()))
                                    .or(STUDENT_ACADEMIC_WORK.WORK_NAME.likeLeft(query.getSearch()))
                    )
                    .and(STUDENT_ACADEMIC_WORK_AUDIT.STATE.eq(query.getState()))
                    .and(STUDENT_ACADEMIC_WORK.TYPE.eq(query.getType()))
                    .pageAs(Page.of(pageNo, pageSize), StudentAcademicWorkItem.class);
            items.getRecords().forEach(this::getStudentAcademicWorkTeam);
            return items;
        }, readThreadPool);
        Page<StudentAcademicWorkItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}

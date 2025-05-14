package edu.guet.studentworkmanagementsystem.service.academicWork.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.Common;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkMemberRequest;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.academicWork.AcademicWorkRequest;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.*;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.*;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.AcademicWorkAuditTableDef.ACADEMIC_WORK_AUDIT;
import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.AcademicWorkMemberTableDef.ACADEMIC_WORK_MEMBER;
import static edu.guet.studentworkmanagementsystem.entity.po.academicWork.table.AcademicWorkTableDef.ACADEMIC_WORK;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.DegreeTableDef.DEGREE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;
import static edu.guet.studentworkmanagementsystem.entity.po.user.table.UserTableDef.USER;

@Service
@Slf4j
public class AcademicWorkServiceImpl extends ServiceImpl<AcademicWorkMapper, AcademicWork> implements AcademicWorkService {
    @Autowired
    private AcademicWorkPaperMapper academicWorkPaperMapper;
    @Autowired
    private AcademicWorkPatentMapper academicWorkPatentMapper;
    @Autowired
    private AcademicWorkSoftMapper academicWorkSoftMapper;
    @Autowired
    private AcademicWorkAuditMapper auditMapper;
    @Autowired
    private AcademicWorkMemberMapper memberMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;


    @Override
    @Transactional
    public <T> BaseResponse<T> insertAcademicWork(AcademicWorkRequest request) {
        String referenceId = insertAcademicWork(request.getAbstractAcademicWork());
        AcademicWork academicWork = createStudentAcademicWork(request, referenceId);
        int i = mapper.insert(academicWork);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        String academicWorkId = academicWork.getAcademicWorkId();
        insertStudentAcademicWorkTeam(request.getTeam(), academicWorkId);
        insertStudentAcademicWorkAudit(academicWorkId);
        return ResponseUtil.success();
    }

    @Transactional
    public String insertAcademicWork(AbstractAcademicWork abstractAcademicWork) {
        int effect = -1;
        String id = "";
        if (abstractAcademicWork instanceof AcademicWorkPaper academicWorkPaper) {
            effect = academicWorkPaperMapper.insert(academicWorkPaper);
            id = academicWorkPaper.getPaperId();
        } else if (abstractAcademicWork instanceof AcademiciWorkPatent academiciWorkPatent) {
            effect = academicWorkPatentMapper.insert(academiciWorkPatent);
            id = academiciWorkPatent.getPatentId();
        } else if (abstractAcademicWork instanceof AcademicWorkSoft academicWorkSoft) {
            effect = academicWorkSoftMapper.insert(academicWorkSoft);
            id = academicWorkSoft.getSoftId();
        }
        if (effect <= 0 || !StringUtils.hasLength(id))
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return id;
    }

    public AcademicWork createStudentAcademicWork(AcademicWorkRequest request, String referenceId) {
        return AcademicWork.builder()
                .workName(request.getWorkName())
                .type(request.getType())
                .username(request.getUsername())
                .evidence(request.getEvidence())
                .referenceId(referenceId)
                .time(LocalDate.now())
                .build();
    }

    @Transactional
    public void insertStudentAcademicWorkTeam(List<AcademicWorkMemberRequest> members, String academicWorkId) {
        List<AcademicWorkMember> studentAcademicWorkTeam = createStudentAcademicWorkTeam(members, academicWorkId);
        int i = memberMapper.insertBatch(studentAcademicWorkTeam);
        int size = studentAcademicWorkTeam.size();
        if (i != size)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    public List<AcademicWorkMember> createStudentAcademicWorkTeam(List<AcademicWorkMemberRequest> members, String academicWorkId) {
        ArrayList<AcademicWorkMember> academicWorkMembers = new ArrayList<>();
        members.forEach(member -> {
            AcademicWorkMember build = AcademicWorkMember.builder()
                    .academicWorkId(academicWorkId)
                    .username(member.getUsername())
                    .memberOrder(member.getMemberOrder())
                    .build();
            academicWorkMembers.add(build);
        });
        return academicWorkMembers;
    }

    @Transactional
    public void insertStudentAcademicWorkAudit(String academicWorkId) {
        AcademicWorkAudit build = AcademicWorkAudit.builder()
                .academicWorkId(academicWorkId)
                .state(Common.WAITING.getValue())
                .build();
        int i = auditMapper.insert(build);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteAcademicWork(String academicWorkId) {
        QueryWrapper deleteStudentAcademicWorkAuditWrapper =
                QueryWrapper.create()
                        .where(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(academicWorkId));
        int i = auditMapper.deleteByQuery(deleteStudentAcademicWorkAuditWrapper);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        QueryWrapper deleteStudentAcademicWorkMemberWrapper =
                QueryWrapper.create()
                        .where(ACADEMIC_WORK_MEMBER.ACADEMIC_WORK_ID.eq(academicWorkId));
        int j = memberMapper.deleteByQuery(deleteStudentAcademicWorkMemberWrapper);
        if (j <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        int k = mapper.deleteById(academicWorkId);
        if (k <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<List<StudentAcademicWorkItem>> getOwnAcademicWork(String username) {
        CompletableFuture<List<StudentAcademicWorkItem>> future = CompletableFuture.supplyAsync(() -> {
            List<StudentAcademicWorkItem> items = QueryChain.of(AcademicWork.class)
                    .select(
                            USER.ALL_COLUMNS,
                            ACADEMIC_WORK.ALL_COLUMNS,
                            ACADEMIC_WORK_AUDIT.ALL_COLUMNS
                    )
                    .from(ACADEMIC_WORK)
                    .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                    .innerJoin(ACADEMIC_WORK_AUDIT).on(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID))
                    .innerJoin(ACADEMIC_WORK_MEMBER).on(ACADEMIC_WORK_MEMBER.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID))
                    .where(USER.USERNAME.eq(username).or(ACADEMIC_WORK_MEMBER.USERNAME.eq(username)))
                    .listAs(StudentAcademicWorkItem.class);
            items.forEach(this::getStudentAcademicWorkTeamAndDetail);
            return items;
        }, readThreadPool);
        List<StudentAcademicWorkItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    public void getStudentAcademicWorkTeamAndDetail(StudentAcademicWorkItem item) {
        String academicWorkId = item.getStudentAcademicWorkId();
        List<AcademicWorkMemberItem> memberItems = QueryChain.of(AcademicWorkMember.class)
                .select(
                        ACADEMIC_WORK_MEMBER.ALL_COLUMNS,
                        USER.USERNAME,
                        USER.REAL_NAME,
                        MAJOR.MAJOR_NAME,
                        GRADE.GRADE_NAME,
                        DEGREE.DEGREE_NAME
                )
                .from(ACADEMIC_WORK_MEMBER)
                .leftJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK_MEMBER.USERNAME))
                .leftJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(USER.USERNAME))
                .leftJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                .leftJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                .leftJoin(DEGREE).on(DEGREE.DEGREE_ID.eq(STUDENT_BASIC.DEGREE_ID))
                .where(ACADEMIC_WORK_MEMBER.ACADEMIC_WORK_ID.eq(academicWorkId))
                .listAs(AcademicWorkMemberItem.class);
        item.setTeam(memberItems);
        String type = item.getType();
        String referenceId = item.getReferenceId();
        if (Common.PAPER.getValue().equals(type)) {
            AcademicWorkPaper academicWorkPaper = academicWorkPaperMapper.selectOneById(referenceId);
            item.setAbstractAcademicWork(academicWorkPaper);
        } else if (Common.SOFT.getValue().equals(type)) {
            AcademicWorkSoft academicWorkSoft = academicWorkSoftMapper.selectOneById(referenceId);
            item.setAbstractAcademicWork(academicWorkSoft);
        } else if (Common.PATENT.getValue().equals(type)) {
            AcademiciWorkPatent studentAcademiciWorkPatent = academicWorkPatentMapper.selectOneById(referenceId);
            item.setAbstractAcademicWork(studentAcademiciWorkPatent);
        }
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateAcademicWorkAudit(List<AcademicWorkAudit> audits)  {
        audits.forEach(audit -> {
            boolean update = UpdateChain.of(AcademicWorkAudit.class)
                    .set(ACADEMIC_WORK_AUDIT.STATE, audit.getState(), StringUtils::hasLength)
                    .set(ACADEMIC_WORK_AUDIT.REJECT_REASON, audit.getRejectReason())
                    .set(ACADEMIC_WORK_AUDIT.OPERATOR_ID, audit.getOperatorId(), StringUtils::hasLength)
                    .set(ACADEMIC_WORK_AUDIT.OPERATOR_TIME, LocalDate.now())
                    .where(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(audit.getAcademicWorkId()))
                    .update();
            if (!update)
                throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        });
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<StudentAcademicWorkItem>> getAllAcademicWork(AcademicWorkQuery query) {
        CompletableFuture<Page<StudentAcademicWorkItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);

            Page<String> idPage = QueryChain.of(AcademicWork.class)
                    .select(ACADEMIC_WORK.ACADEMIC_WORK_ID)
                    .from(ACADEMIC_WORK)
                    .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                    .innerJoin(ACADEMIC_WORK_AUDIT)
                    .on(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID))
                    .where(
                            USER.USERNAME.likeLeft(query.getSearch())
                                    .or(USER.REAL_NAME.likeLeft(query.getSearch()))
                                    .or(ACADEMIC_WORK.WORK_NAME.likeLeft(query.getSearch()))
                    )
                    .and(ACADEMIC_WORK_AUDIT.STATE.eq(query.getState()))
                    .and(ACADEMIC_WORK.TYPE.eq(query.getType()))
                    .pageAs(Page.of(pageNo, pageSize), String.class);

            if (idPage.getRecords().isEmpty()) {
                return Page.of(pageNo, pageSize);
            }

            List<StudentAcademicWorkItem> records = QueryChain.of(AcademicWork.class)
                    .select(
                            USER.ALL_COLUMNS,
                            ACADEMIC_WORK.ALL_COLUMNS,
                            ACADEMIC_WORK_AUDIT.ALL_COLUMNS
                    )
                    .from(ACADEMIC_WORK)
                    .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                    .innerJoin(ACADEMIC_WORK_AUDIT).on(
                            ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID)
                    )
                    .where(ACADEMIC_WORK.ACADEMIC_WORK_ID.in(idPage.getRecords()))
                    .listAs(StudentAcademicWorkItem.class);

            records.forEach(this::getStudentAcademicWorkTeamAndDetail);

            return new Page<>(records, pageNo, pageSize, idPage.getTotalRow());
        }, readThreadPool);
        Page<StudentAcademicWorkItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<List<AcademicWorkUser>> getOptionalUserByUsername(String username) {
        CompletableFuture<List<AcademicWorkUser>> future = CompletableFuture.supplyAsync(() -> QueryChain.of(User.class)
                .select(USER.USERNAME, USER.REAL_NAME)
                .where(USER.USERNAME.likeLeft(username))
                .listAs(AcademicWorkUser.class), readThreadPool);
        List<AcademicWorkUser> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    public BaseResponse<AcademicWorkStatGroup> getStat() {
        CompletableFuture<AcademicWorkStatGroup> future = CompletableFuture.supplyAsync(() -> {
            HashMap<String, List<AcademicWorkStatItem>> map = getMap();
            return handler(map);
        }, readThreadPool);
        AcademicWorkStatGroup execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    /**
     * 获取原始数据
     */
    public HashMap<String, List<AcademicWorkStatItem>> getMap() {
        List<AcademicWorkStatItem> records = getAllStudentAcademicWork();
        String paper = Common.PAPER.getValue();
        ArrayList<AcademicWorkStatItem> papers = new ArrayList<>();
        String patent = Common.PATENT.getValue();
        ArrayList<AcademicWorkStatItem> patents = new ArrayList<>();
        String soft = Common.SOFT.getValue();
        ArrayList<AcademicWorkStatItem> softs = new ArrayList<>();
        HashMap<String, List<AcademicWorkStatItem>> map = new HashMap<>();
        records.forEach(it -> {
            String type = it.getType();
            if (paper.equals(type)) {
                papers.add(it);
            } else if (patent.equals(type)) {
                patents.add(it);
            } else if (soft.equals(type)) {
                softs.add(it);
            }
        });
        map.put(paper, papers);
        map.put(patent, patents);
        map.put(soft, softs);
        return map;
    }
    public List<AcademicWorkStatItem> getAllStudentAcademicWork() {
        List<String> ids = QueryChain.of(AcademicWork.class)
                .select(ACADEMIC_WORK.ACADEMIC_WORK_ID)
                .from(ACADEMIC_WORK)
                .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                .innerJoin(ACADEMIC_WORK_AUDIT)
                .on(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID))
                .and(ACADEMIC_WORK_AUDIT.STATE.eq(Common.PASS.getValue()))
                .listAs(String.class);
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<AcademicWorkStatItem> result = QueryChain.of(AcademicWork.class)
                .select(
                        ACADEMIC_WORK.ALL_COLUMNS,
                        ACADEMIC_WORK_AUDIT.STATE
                )
                .from(ACADEMIC_WORK)
                .innerJoin(ACADEMIC_WORK_AUDIT)
                .on(ACADEMIC_WORK_AUDIT.ACADEMIC_WORK_ID.eq(ACADEMIC_WORK.ACADEMIC_WORK_ID))
                .innerJoin(USER).on(USER.USERNAME.eq(ACADEMIC_WORK.USERNAME))
                .where(ACADEMIC_WORK.ACADEMIC_WORK_ID.in(ids))
                .listAs(AcademicWorkStatItem.class);
        result.forEach(this::getAcademicWorkDetail);
        return result;
    }
    public void getAcademicWorkDetail(AcademicWorkStatItem item) {
        String type = item.getType();
        String referenceId = item.getReferenceId();
        if (Common.PAPER.getValue().equals(type)) {
            AcademicWorkPaper academicWorkPaper = academicWorkPaperMapper.selectOneById(referenceId);
            item.setAbstractAcademicWork(academicWorkPaper);
        } else if (Common.SOFT.getValue().equals(type)) {
            AcademicWorkSoft academicWorkSoft = academicWorkSoftMapper.selectOneById(referenceId);
            item.setAbstractAcademicWork(academicWorkSoft);
        } else if (Common.PATENT.getValue().equals(type)) {
            AcademiciWorkPatent studentAcademiciWorkPatent = academicWorkPatentMapper.selectOneById(referenceId);
            item.setAbstractAcademicWork(studentAcademiciWorkPatent);
        }
    }
    /**
     * 处理原始数据, 并转化
     */
    public AcademicWorkStatGroup handler(HashMap<String, List<AcademicWorkStatItem>> map) {
        PaperStat paperStat = new PaperStat("0", "0", "0");
        PatentStat patentStat = new PatentStat("0", "0");
        SoftStat softStat = new SoftStat("0");

        // 统计论文
        List<AcademicWorkStatItem> papers = map.getOrDefault(Common.PAPER.getValue(), Collections.emptyList());
        for (AcademicWorkStatItem item : papers) {
            AcademicWorkPaper academicWorkPaper = (AcademicWorkPaper) item.getAbstractAcademicWork();
            if (academicWorkPaper.getIsMeeting() != null && academicWorkPaper.getIsMeeting()) {
                paperStat.setMeetingNumber(String.valueOf(Integer.parseInt(paperStat.getMeetingNumber()) + 1));
            }
            if (academicWorkPaper.getIsChineseCore() != null && academicWorkPaper.getIsChineseCore()) {
                paperStat.setChineseCoreNumber(String.valueOf(Integer.parseInt(paperStat.getChineseCoreNumber()) + 1));
            }
            if (academicWorkPaper.getIsEI() != null && academicWorkPaper.getIsEI()) {
                paperStat.setEI_Number(String.valueOf(Integer.parseInt(paperStat.getEI_Number()) + 1));
            }
        }

        // 统计专利
        List<AcademicWorkStatItem> patents = map.getOrDefault(Common.PATENT.getValue(), Collections.emptyList());
        for (AcademicWorkStatItem item : patents) {
            AcademiciWorkPatent academiciWorkPatent = (AcademiciWorkPatent) item.getAbstractAcademicWork();
            patentStat.setTotalNumber(String.valueOf(Integer.parseInt(patentStat.getTotalNumber()) + 1));
            if ("授权".equals(academiciWorkPatent.getPublishState())) {
                patentStat.setNumber(String.valueOf(Integer.parseInt(patentStat.getNumber()) + 1));
            }
        }

        // 统计软著
        List<AcademicWorkStatItem> softs = map.getOrDefault(Common.SOFT.getValue(), Collections.emptyList());
        softStat.setNumber(String.valueOf(softs.size()));

        return new AcademicWorkStatGroup(paperStat, patentStat, softStat);
    }

}

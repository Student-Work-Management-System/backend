package edu.guet.studentworkmanagementsystem.service.scholarship.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipItem;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatGroup.ScholarshipStatItem;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatRow;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.scholarship.ScholarshipMapper;
import edu.guet.studentworkmanagementsystem.mapper.scholarship.StudentScholarshipMapper;
import edu.guet.studentworkmanagementsystem.service.scholarship.ScholarshipService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.GradeTableDef.GRADE;
import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.scholarship.table.ScholarshipTableDef.SCHOLARSHIP;
import static edu.guet.studentworkmanagementsystem.entity.po.scholarship.table.StudentScholarshipTableDef.STUDENT_SCHOLARSHIP;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentBasicTableDef.STUDENT_BASIC;

@Service
public class ScholarshipServiceImpl extends ServiceImpl<StudentScholarshipMapper, StudentScholarship> implements ScholarshipService {
    @Autowired
    private ScholarshipMapper scholarshipMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    @Transactional
    public <T> BaseResponse<T> insertScholarship(Scholarship scholarship) {
        int i = scholarshipMapper.insert(scholarship);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateScholarship(Scholarship scholarship) {
        boolean update = UpdateChain.of(Scholarship.class)
                .set(SCHOLARSHIP.SCHOLARSHIP_NAME, scholarship.getScholarshipName(), StringUtils::hasLength)
                .set(SCHOLARSHIP.SCHOLARSHIP_LEVEL, scholarship.getScholarshipLevel(), StringUtils::hasLength)
                .where(SCHOLARSHIP.SCHOLARSHIP_ID.eq(scholarship.getScholarshipId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<Scholarship>> getScholarships() {
        CompletableFuture<List<Scholarship>> future =
                CompletableFuture.supplyAsync(() -> scholarshipMapper.selectAll(), readThreadPool);
        List<Scholarship> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteScholarship(String scholarshipId) {
        int i = scholarshipMapper.deleteById(scholarshipId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentScholarshipItem>> getStudentScholarship(ScholarshipQuery query) {
        CompletableFuture<Page<StudentScholarshipItem>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(StudentScholarship.class)
                    .select(
                            STUDENT_BASIC.STUDENT_ID,
                            STUDENT_BASIC.NAME,
                            SCHOLARSHIP.ALL_COLUMNS,
                            STUDENT_SCHOLARSHIP.STUDENT_SCHOLARSHIP_ID,
                            STUDENT_SCHOLARSHIP.AWARD_YEAR,
                            MAJOR.MAJOR_NAME,
                            GRADE.GRADE_NAME
                    )
                    .from(STUDENT_SCHOLARSHIP)
                    .innerJoin(SCHOLARSHIP).on(SCHOLARSHIP.SCHOLARSHIP_ID.eq(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID))
                    .innerJoin(STUDENT_BASIC).on(STUDENT_BASIC.STUDENT_ID.eq(STUDENT_SCHOLARSHIP.STUDENT_ID))
                    .innerJoin(MAJOR).on(MAJOR.MAJOR_ID.eq(STUDENT_BASIC.MAJOR_ID))
                    .innerJoin(GRADE).on(GRADE.GRADE_ID.eq(STUDENT_BASIC.GRADE_ID))
                    .where(
                            STUDENT_BASIC.NAME.likeLeft(query.getSearch())
                                    .or(STUDENT_BASIC.STUDENT_ID.likeLeft(query.getSearch()))
                    )
                    .and(STUDENT_BASIC.GRADE_ID.eq(query.getGradeId()))
                    .and(STUDENT_BASIC.MAJOR_ID.eq(query.getMajorId()))
                    .and(SCHOLARSHIP.SCHOLARSHIP_LEVEL.eq(query.getScholarshipLevel()))
                    .and(STUDENT_SCHOLARSHIP.AWARD_YEAR.eq(query.getAwardYear()))
                    .pageAs(Page.of(pageNo, pageSize), StudentScholarshipItem.class);
        }, readThreadPool);
        Page<StudentScholarshipItem> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentScholarship(ValidateList<StudentScholarship> studentScholarship) {
        int i = mapper.insertBatch(studentScholarship);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentScholarship(StudentScholarship studentScholarship) {
        boolean update = UpdateChain.of(StudentScholarship.class)
                .set(STUDENT_SCHOLARSHIP.AWARD_YEAR, studentScholarship.getAwardYear(), StringUtils::hasLength)
                .set(STUDENT_SCHOLARSHIP.SCHOLARSHIP_ID, studentScholarship.getScholarshipId(), StringUtils::hasLength)
                .where(STUDENT_SCHOLARSHIP.STUDENT_SCHOLARSHIP_ID.eq(studentScholarship.getStudentScholarshipId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentScholarship(String studentScholarshipId) {
        QueryWrapper wrapper = QueryWrapper.create().where(STUDENT_SCHOLARSHIP.STUDENT_SCHOLARSHIP_ID.eq(studentScholarshipId));
        int i = mapper.deleteByQuery(wrapper);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<List<StudentScholarshipStatGroup>> getStat(ScholarshipStatQuery query) {
        CompletableFuture<List<StudentScholarshipStatGroup>> future = CompletableFuture.supplyAsync(() -> {
            List<StudentScholarshipStatRow> rows = mapper.getStat(query);

            // 先按年级分组
            Map<String, List<StudentScholarshipStatRow>> gradeMap = new HashMap<>();
            for (StudentScholarshipStatRow row : rows) {
                gradeMap.computeIfAbsent(row.getGradeName(), k -> new ArrayList<>()).add(row);
            }

            List<StudentScholarshipStatGroup> groups = new ArrayList<>();

            for (Map.Entry<String, List<StudentScholarshipStatRow>> gradeEntry : gradeMap.entrySet()) {
                String gradeName = gradeEntry.getKey();
                List<StudentScholarshipStatRow> gradeRows = gradeEntry.getValue();

                // 每个年级内部按专业分组
                Map<String, List<StudentScholarshipStatRow>> majorMap = new HashMap<>();
                for (StudentScholarshipStatRow row : gradeRows) {
                    majorMap.computeIfAbsent(row.getMajorName(), k -> new ArrayList<>()).add(row);
                }

                List<StudentScholarshipStatGroup.StudentScholarshipStatItem> majorItems = new ArrayList<>();

                for (Map.Entry<String, List<StudentScholarshipStatRow>> majorEntry : majorMap.entrySet()) {
                    String majorName = majorEntry.getKey();
                    List<StudentScholarshipStatRow> majorRows = majorEntry.getValue();

                    // 按【奖学金名+级别】分组
                    Map<String, ScholarshipStatItem> scholarshipMap = new LinkedHashMap<>();
                    for (StudentScholarshipStatRow row : majorRows) {
                        String key = row.getScholarshipName() + "-" + row.getScholarshipLevel();
                        ScholarshipStatItem item = scholarshipMap.get(key);
                        if (item == null) {
                            item = ScholarshipStatItem.builder()
                                    .scholarshipName(row.getScholarshipName())
                                    .scholarshipLevel(row.getScholarshipLevel())
                                    .total(row.getTotal())
                                    .build();
                            scholarshipMap.put(key, item);
                        } else {
                            // 如果已经有了，就累加 total
                            int currentTotal = Integer.parseInt(item.getTotal());
                            int addTotal = Integer.parseInt(row.getTotal());
                            item.setTotal(String.valueOf(currentTotal + addTotal));
                        }
                    }

                    List<ScholarshipStatItem> scholarshipItems = new ArrayList<>(scholarshipMap.values());

                    StudentScholarshipStatGroup.StudentScholarshipStatItem majorItem = StudentScholarshipStatGroup.StudentScholarshipStatItem.builder()
                            .majorName(majorName)
                            .scholarshipStatItems(scholarshipItems)
                            .build();
                    majorItems.add(majorItem);
                }

                StudentScholarshipStatGroup group = StudentScholarshipStatGroup.builder()
                        .gradeName(gradeName)
                        .studentScholarshipStatItems(majorItems)
                        .build();
                groups.add(group);
            }

            return groups;
        }, readThreadPool);

        List<StudentScholarshipStatGroup> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

}

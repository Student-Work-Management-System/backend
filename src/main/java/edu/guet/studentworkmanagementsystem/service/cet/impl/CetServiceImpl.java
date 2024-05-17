package edu.guet.studentworkmanagementsystem.service.cet.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import static com.mybatisflex.core.query.QueryMethods.*;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cet.*;
import edu.guet.studentworkmanagementsystem.entity.po.cet.StudentCet;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.CetStatistics;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.CetVO;
import edu.guet.studentworkmanagementsystem.entity.vo.cet.StudentCetVO;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.cet.StudentCetMapper;
import edu.guet.studentworkmanagementsystem.network.CetFeign;
import edu.guet.studentworkmanagementsystem.service.cet.CetService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

import static edu.guet.studentworkmanagementsystem.common.Majors.majorName2MajorId;
import static edu.guet.studentworkmanagementsystem.entity.po.cet.table.StudentCetTableDef.STUDENT_CET;
import static edu.guet.studentworkmanagementsystem.entity.po.major.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class CetServiceImpl extends ServiceImpl<StudentCetMapper, StudentCet> implements CetService {
    @Autowired
    private CetFeign cetFeign;
    @Override
    @Transactional
    public <T> BaseResponse<T> importCETScore(InsertCetDTOList insertCetDTOList) {
        int size = insertCetDTOList.getInsertStudentCetDTOList().size();
        List<StudentCet> collect = insertCetDTOList.getInsertStudentCetDTOList()
                .stream()
                .map(this::convertToEntity)
                .toList();
        int i = mapper.insertBatch(collect);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentCet(InsertStudentCetDTO insertStudentCetDTO) {
        StudentCet studentCet = convertToEntity(insertStudentCetDTO);
        int i = mapper.insert(studentCet);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    public BaseResponse<Page<StudentCetVO>> getAllRecord(CETQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentCetVO> studentCetVOPage = QueryChain.of(StudentCet.class)
                .select(distinct(STUDENT.STUDENT_ID), STUDENT.NAME, STUDENT.GRADE, MAJOR.MAJOR_NAME)
                .from(STUDENT)
                .innerJoin(STUDENT_CET).on(STUDENT_CET.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .where(STUDENT.STUDENT_ID.like(query.getSearch()).or(STUDENT.NAME.like(query.getSearch())))
                .and(STUDENT.MAJOR_ID.eq(query.getMajorId()))
                .and(STUDENT.GRADE.eq(query.getGrade()))
                .pageAs(Page.of(pageNo, pageSize), StudentCetVO.class);
        studentCetVOPage.getRecords().forEach(item -> {
            String studentId = item.getStudentId();
            List<CetVO> list = QueryChain.of(StudentCet.class)
                    .select(STUDENT_CET.ALL_COLUMNS)
                    .from(STUDENT_CET)
                    .where(STUDENT_CET.STUDENT_ID.eq(studentId))
                    .listAs(CetVO.class);
            item.setCets(list);
        });
        return ResponseUtil.success(studentCetVOPage);
    }
    @Override
    public BaseResponse<List<String>> getOptionalExamDate() {
        List<String> collect = QueryChain.of(StudentCet.class)
                .select(STUDENT_CET.EXAM_DATE)
                .from(STUDENT_CET)
                .list()
                .stream()
                .map(StudentCet::getExamDate)
                .toList();
        return ResponseUtil.success(collect);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentCET(UpdateStudentCetDTO updateStudentCetDTO) {
        boolean update = UpdateChain.of(StudentCet.class)
                .set(STUDENT_CET.STUDENT_ID, updateStudentCetDTO.getStudentId(), StringUtils::hasLength)
                .set(STUDENT_CET.CERTIFICATE_NUMBER, updateStudentCetDTO.getCertificateNumber(), StringUtils::hasLength)
                .set(STUDENT_CET.SCORE, updateStudentCetDTO.getScore(), Objects::nonNull)
                .set(STUDENT_CET.EXAM_DATE, updateStudentCetDTO.getExamDate(), StringUtils::hasLength)
                .set(STUDENT_CET.EXAM_TYPE, updateStudentCetDTO.getExamType(), StringUtils::hasLength)
                .where(StudentCet::getStudentCetId).eq(updateStudentCetDTO.getStudentCetId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentCET(String studentCETId) {
        int i = mapper.deleteById(studentCETId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<HashMap<String, CetStatistics>> getCetStatistics(CetStatQuery query) {
        List<String> majorIds = query.getMajorIds();
        if (majorIds.isEmpty()) {
            int key = 1;
            while (key <= 6) {
                majorIds.add(String.valueOf(key));
                key++;
            }
        }
        Map<String, Object> map = cetFeign.exportOnlyStat(query);
        Set<String> keys = majorName2MajorId.keySet();
        HashMap<String, CetStatistics> result = new HashMap<>();
        keys.forEach(item -> {
            if (map.containsKey(item)) {
                CetStatistics cetStatistics = new CetStatistics();
                HashMap<String, Object> dataMap = (HashMap<String, Object>) map.get(item);
                cetStatistics.setCet4((HashMap<String, Object>) dataMap.get("cet4"));
                cetStatistics.setCet6((HashMap<String, Object>) dataMap.get("cet6"));
                result.put(item, cetStatistics);
            }
        });
        return ResponseUtil.success(result);
    }

    private StudentCet convertToEntity(InsertStudentCetDTO insertStudentCetDTO) {
        StudentCet studentCet = new StudentCet();
        BeanUtils.copyProperties(insertStudentCetDTO, studentCet);
        return studentCet;
    }
}

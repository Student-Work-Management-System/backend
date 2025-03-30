package edu.guet.studentworkmanagementsystem.service.punlishment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentList;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.PunishmentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentDTO;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentItem;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.punlishment.PunishmentMapper;
import edu.guet.studentworkmanagementsystem.service.punlishment.PunishmentService;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static edu.guet.studentworkmanagementsystem.entity.po.other.table.MajorTableDef.MAJOR;
import static edu.guet.studentworkmanagementsystem.entity.po.punishment.table.StudentPunishmentTableDef.STUDENT_PUNISHMENT;
import static edu.guet.studentworkmanagementsystem.entity.po.student.table.StudentTableDef.STUDENT;

@Service
public class PunishmentServiceImpl extends ServiceImpl<PunishmentMapper, StudentPunishment> implements PunishmentService {
    @Override
    @Transactional
    public <T> BaseResponse<T> importStudentPunishment(PunishmentList punishmentList) {
        List<StudentPunishment> punishments = punishmentList.getPunishments();
        int size = punishments.size();
        int i = mapper.insertBatch(punishments);
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }
    @Override
    @Transactional
    public <T> BaseResponse<T> insertStudentPunishment(StudentPunishment studentPunishment) {
        int i = mapper.insert(studentPunishment);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<StudentPunishmentItem>> getAllStudentPunishment(PunishmentQuery query) {
        Integer pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getPageSize()).orElse(50);
        Page<StudentPunishmentItem> studentPunishmentVOPage = QueryChain.of(StudentPunishment.class)
                .select(STUDENT_PUNISHMENT.ALL_COLUMNS, STUDENT.ALL_COLUMNS, MAJOR.ALL_COLUMNS)
                .from(STUDENT_PUNISHMENT)
                .innerJoin(STUDENT).on(STUDENT_PUNISHMENT.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .innerJoin(MAJOR).on(STUDENT.MAJOR_ID.eq(MAJOR.MAJOR_ID))
                .pageAs(Page.of(pageNo, pageSize), StudentPunishmentItem.class);
        return ResponseUtil.success(studentPunishmentVOPage);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteStudentPunishment(String studentPunishmentId) {
        int i = mapper.deleteById(studentPunishmentId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateStudentPunishment(StudentPunishmentDTO studentPunishmentDTO) {
        boolean update = UpdateChain.of(StudentPunishment.class)
                .set(StudentPunishment::getStudentId, studentPunishmentDTO.getStudentId(), StringUtils.hasLength(studentPunishmentDTO.getStudentId()))
                .set(StudentPunishment::getPunishmentDate, studentPunishmentDTO.getPunishmentDate(), !Objects.isNull(studentPunishmentDTO.getPunishmentDate()))
                .set(StudentPunishment::getPunishmentLevel, studentPunishmentDTO.getPunishmentLevel(), StringUtils.hasLength(studentPunishmentDTO.getPunishmentLevel()))
                .set(StudentPunishment::getPunishmentReason, studentPunishmentDTO.getPunishmentReason(), StringUtils.hasLength(studentPunishmentDTO.getPunishmentReason()))
                .where(StudentPunishment::getStudentPunishmentId).eq(studentPunishmentDTO.getStudentPunishmentId())
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

}

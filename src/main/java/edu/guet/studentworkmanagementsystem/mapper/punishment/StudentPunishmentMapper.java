package edu.guet.studentworkmanagementsystem.mapper.punishment;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.punishment.StudentPunishmentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import edu.guet.studentworkmanagementsystem.entity.vo.punishment.StudentPunishmentStatRow;

import java.util.List;

public interface StudentPunishmentMapper extends BaseMapper<StudentPunishment> {
    List<StudentPunishmentStatRow> getStat(StudentPunishmentStatQuery query);
}

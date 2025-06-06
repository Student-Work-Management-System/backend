package edu.guet.studentworkmanagementsystem.mapper.povertyAssistance;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.PovertyAssistanceStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.PovertyAssistanceStatRow;

import java.util.List;

public interface PovertyAssistanceMapper extends BaseMapper<PovertyAssistance> {
    List<PovertyAssistanceStatRow> getPovertyAssistanceStat(PovertyAssistanceStatQuery query);
}

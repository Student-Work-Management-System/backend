package edu.guet.studentworkmanagementsystem.mapper.povertyAssistance;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.PovertyAssistanceStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance.PovertyAssistanceStatusItem;

import java.util.List;

public interface PovertyAssistanceMapper extends BaseMapper<PovertyAssistance> {
    List<PovertyAssistanceStatusItem> getPovertyAssistanceStatus(PovertyAssistanceStatusQuery query);
}

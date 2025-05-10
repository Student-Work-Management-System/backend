package edu.guet.studentworkmanagementsystem.mapper.precaution;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.PrecautionStatRow;

import java.util.List;

public interface PrecautionMapper extends BaseMapper<StudentPrecaution> {
    List<PrecautionStatRow> getStat(PrecautionStatQuery query);
}

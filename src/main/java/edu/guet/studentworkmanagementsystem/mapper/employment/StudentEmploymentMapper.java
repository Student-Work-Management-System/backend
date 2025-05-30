package edu.guet.studentworkmanagementsystem.mapper.employment;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.employment.StudentEmployment;
import edu.guet.studentworkmanagementsystem.entity.vo.employment.StudentEmploymentStatRow;

import java.util.List;

public interface StudentEmploymentMapper extends BaseMapper<StudentEmployment> {
    List<StudentEmploymentStatRow> getStat(EmploymentStatQuery query);
}

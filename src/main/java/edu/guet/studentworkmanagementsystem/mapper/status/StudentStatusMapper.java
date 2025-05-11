package edu.guet.studentworkmanagementsystem.mapper.status;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.status.StudentStatusStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import edu.guet.studentworkmanagementsystem.entity.vo.status.StudentStatusStatRow;

import java.util.List;

public interface StudentStatusMapper extends BaseMapper<StudentStatus> {
    List<StudentStatusStatRow> getStat(StudentStatusStatQuery query);
}

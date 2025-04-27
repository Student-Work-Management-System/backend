package edu.guet.studentworkmanagementsystem.mapper.scholarship;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.ScholarshipStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.scholarship.StudentScholarship;
import edu.guet.studentworkmanagementsystem.entity.vo.scholarship.StudentScholarshipStatRow;

import java.util.List;

public interface StudentScholarshipMapper extends BaseMapper<StudentScholarship> {
    List<StudentScholarshipStatRow> getStat(ScholarshipStatQuery query);
}

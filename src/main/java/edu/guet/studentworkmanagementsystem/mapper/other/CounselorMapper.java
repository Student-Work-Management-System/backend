package edu.guet.studentworkmanagementsystem.mapper.other;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.po.other.Counselor;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CounselorMapper extends BaseMapper<Counselor> {
    List<CounselorItem> getCounselors(
            @Param("search") String search,
            @Param("gradeId") String gradeId,
            @Param("degreeId") String degreeId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );
}

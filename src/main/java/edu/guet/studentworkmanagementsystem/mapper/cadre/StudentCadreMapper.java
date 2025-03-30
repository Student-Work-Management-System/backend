package edu.guet.studentworkmanagementsystem.mapper.cadre;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatItem;

import java.util.List;


public interface StudentCadreMapper extends BaseMapper<StudentCadre> {
    List<StudentCadreStatItem> getCadreStatus(CadreStatQuery query);
}

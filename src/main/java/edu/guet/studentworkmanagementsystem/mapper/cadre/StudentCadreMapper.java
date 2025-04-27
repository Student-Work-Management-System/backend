package edu.guet.studentworkmanagementsystem.mapper.cadre;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatGroup;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatRow;

import java.util.List;


public interface StudentCadreMapper extends BaseMapper<StudentCadre> {
    List<StudentCadreStatRow> getCadreStatus(CadreStatQuery query);
}

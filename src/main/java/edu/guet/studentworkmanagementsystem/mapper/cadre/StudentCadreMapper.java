package edu.guet.studentworkmanagementsystem.mapper.cadre;

import com.mybatisflex.core.BaseMapper;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatusItem;

import java.util.List;


public interface StudentCadreMapper extends BaseMapper<StudentCadre> {
    List<StudentCadreStatusItem> getCadreStatus(CadreStatusQuery query);
}

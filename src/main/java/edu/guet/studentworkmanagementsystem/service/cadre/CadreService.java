package edu.guet.studentworkmanagementsystem.service.cadre;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.*;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreItem;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreStatGroup;

import java.util.List;

public interface CadreService extends IService<StudentCadre> {
    /**
     * 对象添加职位
     */
    <T> BaseResponse<T> insertCadre(Cadre cadre);
    /**
     * 修改职位的信息
     */
    <T> BaseResponse<T> updateCadre(Cadre cadre);
    /**
     * 获取所有的职位
     * @return 职位清单
     */
    BaseResponse<List<Cadre>> getAllCadres();
    /**
     * 删除岗位记录(需要考虑外检约束)
     * @param cadreId 岗位记录id
     */
    <T> BaseResponse<T> deleteCadre(String cadreId);
    /**
     * 在导入完成职位信息后才能安排学生任职信息
     */
    BaseResponse<StudentCadre> arrangePosition(StudentCadre studentCadre);
    BaseResponse<StudentCadre> arrangePositions(ValidateList<StudentCadre> studentCadreList);
    /**
     * 修改学生任职信息(不能变动职位,若需变动实现下方)
     */
    <T> BaseResponse<T> updateStudentCadre(StudentCadre studentCadre);
    /**
     * 删除学生任职记录
     * @param studentCadreId 需要删除的记录的id
     */
    <T> BaseResponse<T> deleteStudentCadre(String studentCadreId);
    /**
     * 分页查询学生任职记录
     * <br/>
     * @param query 查询参数
     * @return 学生任职记录
     */
    BaseResponse<Page<StudentCadreItem>> getAllStudentCadre(CadreQuery query);
    BaseResponse<List<StudentCadreStatGroup>> getCadreStat(CadreStatQuery query);
}

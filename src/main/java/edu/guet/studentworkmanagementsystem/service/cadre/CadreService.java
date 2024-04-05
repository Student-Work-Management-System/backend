package edu.guet.studentworkmanagementsystem.service.cadre;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.*;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.vo.cadre.StudentCadreVO;

import java.util.List;

public interface CadreService extends IService<StudentCadre> {
    /**
     * 批量导入职位信息
     * @param cadres 职位信息列表
     */
    <T> BaseResponse<T> importCadres(List<Cadre> cadres);
    /**
     * 对象添加职位
     * @param cadre 职位记录对象
     */
    <T> BaseResponse<T> insertCadre(Cadre cadre);
    /**
     * 修改职位的信息
     * @param cadreDTO 待修改的职位信息
     */
    <T> BaseResponse<T> updateCadre(CadreDTO cadreDTO);
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
     * @param insertStudentCadreDTO 学生任职记录对象
     */
    BaseResponse<StudentCadre> arrangePositions(InsertStudentCadreDTO insertStudentCadreDTO);
    /**
     * 修改学生任职信息(不能变动职位,若需变动实现下方)
     * @param updateStudentCadreDTO 修改需要传递的对象, 某一为空则不修改该属性
     */
    <T> BaseResponse<T> updateStudentCadre(UpdateStudentCadreDTO updateStudentCadreDTO);
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
    BaseResponse<Page<StudentCadreVO>> getAllStudentAcademicWork(CadreQuery query);
}

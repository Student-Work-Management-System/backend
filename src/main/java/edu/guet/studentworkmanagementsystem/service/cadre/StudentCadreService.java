package edu.guet.studentworkmanagementsystem.service.cadre;

import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.CadreDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.StudentCadreDTO;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentCadreService extends IService<StudentCadre> {
    /**
     * 使用文件导入职位信息
     * @param multipartFile 文件源
     * @return 存入数据库中的职位信息(包含id返回)
     */
    BaseResponse<List<Cadre>> importCadres(MultipartFile multipartFile);
    /**
     * 对象添加职位
     * @param cadre 职位记录对象
     */
    BaseResponse<Cadre> insertCadre(Cadre cadre);
    /**
     * 修改职位的信息
     * @param cadreDTO 待修改的职位信息
     */
    <T> BaseResponse<T> updateCadre(CadreDTO cadreDTO);
    /**
     * 删除岗位记录(需要考虑外检约束)
     * @param cadreId 岗位记录id
     */
    <T> BaseResponse<T> deleteCadre(String cadreId);
    /**
     * 在导入完成职位信息后才能安排学生任职信息
     * @param studentCadreDTO 前端传递的学生任职记录对象
     */
    <T> BaseResponse<T> arrangePositions(StudentCadreDTO studentCadreDTO);
    /**
     * 修改学生任职信息(不能变动职位,若需变动实现下方)
     * @param studentCadreDTO 修改需要传递的对象, 某一为空则不修改该属性
     */
    <T> BaseResponse<T> updateStudentCadreInfo(StudentCadreDTO studentCadreDTO);
    /**
     * 学生任职修正
     * @param studentId 学号
     * @param oldCadreId 原职位id
     * @param newCadreId 新职位id
     */
    <T> BaseResponse<T> updateStudentCadre(String studentId, String oldCadreId, String newCadreId);
    /**
     * 删除学生任职记录
     * @param studentId 学号
     * @param cadreId 岗位id
     */
    <T> BaseResponse<T> deleteStudentCadre(String studentId, String cadreId);
    // todo: 查询：可以按年级、专业、职位筛选，除此之外，还可以使用学号、姓名进行模糊查询
}

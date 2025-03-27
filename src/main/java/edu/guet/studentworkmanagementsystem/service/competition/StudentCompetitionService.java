package edu.guet.studentworkmanagementsystem.service.competition;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionWithMember;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionItem;

import java.util.List;

public interface StudentCompetitionService extends IService<StudentCompetition> {
    /**
     * 学生上报获奖(设计为上报后不允许修改, 可以重新上报)
     */
    <T> BaseResponse<T> insertStudentCompetition(StudentCompetitionWithMember studentCompetitionWithMember);
    /**
     * 学生获取自己上报的奖项记录
     */
    BaseResponse<List<StudentCompetitionItem>> getOwnStudentCompetition();
    /**
     * 删除学生获奖记录
     * @param studentCompetitionId 学生竞赛id
     */
    <T> BaseResponse<T> deleteStudentCompetition(String studentCompetitionId);
    /**
     * 学生上报记录
     */
    BaseResponse<Page<StudentCompetitionItem>> getStudentCompetitions(StudentCompetitionQuery query);
}

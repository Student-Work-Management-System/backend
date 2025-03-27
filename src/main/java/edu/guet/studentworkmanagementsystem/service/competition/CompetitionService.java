package edu.guet.studentworkmanagementsystem.service.competition;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.*;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;

public interface CompetitionService extends IService<StudentCompetition> {
    /**
     * 批量导入竞赛信息
     */
    <T> BaseResponse<T> importCompetition(ValidateList<Competition> competitions);
    /**
     * 对象添加竞赛
     * @param competition 竞赛记录对象
     */
    <T> BaseResponse<T> insertCompetition(Competition competition);
    /**
     * 修改竞赛的信息
     * @param competition 待修改的竞赛信息
     */
    <T> BaseResponse<T> updateCompetition(Competition competition);
    /**
     * 获取所有的竞赛
     * @return 竞赛清单
     */
    BaseResponse<Page<Competition>> getAllCompetitions(CompetitionQuery query);
    /**
     * 删除竞赛(需要考虑外检约束)
     * @param competitionId 竞赛记录id
     */
    <T> BaseResponse<T> deleteCompetition(String competitionId);
    boolean competitionNatureIsSolo(String competitionId);
}


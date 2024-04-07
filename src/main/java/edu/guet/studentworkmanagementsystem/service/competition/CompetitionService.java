package edu.guet.studentworkmanagementsystem.service.competition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionAuditDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionList;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.StudentCompetitionDTO;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import edu.guet.studentworkmanagementsystem.entity.po.competition.Members;
import edu.guet.studentworkmanagementsystem.entity.po.competition.StudentCompetition;
import edu.guet.studentworkmanagementsystem.entity.vo.competition.StudentCompetitionVO;

import java.util.List;

public interface CompetitionService extends IService<StudentCompetition> {
    /**
     * 批量导入竞赛信息
     * @param competitionList 竞赛列表
     * @return 存入数据库中的竞赛信息(包含id返回)
     */
    BaseResponse<List<Competition>> importCompetition(CompetitionList competitionList);
    /**
     * 对象添加竞赛
     * @param competition 竞赛记录对象
     */
    BaseResponse<Competition> insertCompetition(Competition competition);
    /**
     * 修改竞赛的信息
     * @param competition 待修改的竞赛信息
     */
    <T> BaseResponse<T> updateCompetition(Competition competition);
    /**
     * 获取所有的竞赛
     * @return 竞赛清单
     */
    BaseResponse<Page<Competition>> getAllCompetitions(int pageNo, int pageSize);
    /**
     * 删除竞赛(需要考虑外检约束)
     * @param competitionId 竞赛记录id
     */
    <T> BaseResponse<T> deleteCompetition(String competitionId);
    /**
     * 学生上报获奖(设计为上报后不允许修改, 可以重新上报)
     * <br/>
     * 无审核人相关信息, 审核状态设置为默认: "审核中"
     * 转为{@link StudentCompetition 数据库存储类}时可以调用{@link StudentCompetition#StudentCompetition(StudentCompetitionDTO) 特殊构造函数}
     * <br/>
     * 若后续审核通过则从数据库取出(json), 再转换为对象取出学号存入认领表
     * @param studentCompetitionDTO 学生上报获奖
     */
    <T> BaseResponse<T> insertStudentCompetition(StudentCompetitionDTO studentCompetitionDTO) throws JsonProcessingException;
    /**
     * 学生获取自己上报的奖项记录(包括所有状态的上报记录)
     * @param studentId 学号
     * @return 该学生所上报过的所有竞赛记录
     */
    BaseResponse<List<StudentCompetitionVO>> getOwnStudentCompetition(String studentId);
    /**
     * 审核学生竞赛结果
     * <br/>
     * 通过 竞赛id 和 队长学号/参赛者学号 定位数据, 修改审核结果。<br/>
     * 拒绝 -> 修改状态 并 填入拒绝理由 <br/>
     * 通过 -> 修改状态 并 存入认领表
     * @param competitionAuditDTO 竞赛结果审核对象
     */
    <T> BaseResponse<T> auditStudentCompetition(CompetitionAuditDTO competitionAuditDTO) throws JsonProcessingException;
    /**
     * 删除学生获奖记录(同时要删除在认领表中的记录)
     * @param studentCompetitionId 学生竞赛id
     */
    <T> BaseResponse<T> deleteStudentCompetition(String studentCompetitionId);
    /**
     * (审核人用)分页查询学生上报记录, 默认只查询状态为 待审核 的上报记录
     * <br/>
     * 审核人在前端不展示, 拒绝理由只有状态未 未通过 时才展示
     * @param query 查询参数
     * @return 上报结果
     */
    BaseResponse<Page<StudentCompetitionVO>> getAllStudentCompetition(CompetitionQuery query);
}


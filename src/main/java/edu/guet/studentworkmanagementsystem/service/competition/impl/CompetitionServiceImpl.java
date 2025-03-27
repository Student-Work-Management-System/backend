package edu.guet.studentworkmanagementsystem.service.competition.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.competition.CompetitionQuery;
import edu.guet.studentworkmanagementsystem.entity.po.competition.*;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.competition.CompetitionMapper;
import edu.guet.studentworkmanagementsystem.mapper.competition.CompetitionAuditMapper;
import edu.guet.studentworkmanagementsystem.mapper.competition.StudentCompetitionMapper;
import edu.guet.studentworkmanagementsystem.service.competition.CompetitionService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.competition.table.CompetitionTableDef.COMPETITION;

@Service
@Slf4j
public class CompetitionServiceImpl extends ServiceImpl<StudentCompetitionMapper, StudentCompetition> implements CompetitionService {
    @Autowired
    private CompetitionMapper competitionMapper;
    @Autowired
    private CompetitionAuditMapper auditMapper;
    @Qualifier("readThreadPool")
    @Autowired
    private ThreadPoolTaskExecutor readThreadPool;

    @Override
    @Transactional
    public <T> BaseResponse<T> importCompetition(ValidateList<Competition> competitions) {
        int i = competitionMapper.insertBatch(competitions);
        int size = competitions.size();
        if (i == size)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> insertCompetition(Competition competition) {
        int i = competitionMapper.insert(competition);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updateCompetition(Competition competition) {
        boolean update = UpdateChain.of(Competition.class)
                .set(COMPETITION.COMPETITION_NAME, competition.getCompetitionName(), StringUtils::hasLength)
                .set(COMPETITION.COMPETITION_NATURE, competition.getCompetitionNature(), !Objects.isNull(competition.getCompetitionNature()))
                .set(COMPETITION.COMPETITION_TYPE, competition.getCompetitionType(), StringUtils::hasLength)
                .set(COMPETITION.COMMENT, competition.getComment(), StringUtils::hasLength)
                .where(COMPETITION.COMPETITION_ID.eq(competition.getCompetitionId()))
                .update();
        if (update)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public BaseResponse<Page<Competition>> getAllCompetitions(CompetitionQuery query) {
        CompletableFuture<Page<Competition>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(Competition.class)
                    .select(COMPETITION.ALL_COLUMNS)
                    .from(COMPETITION)
                    .where(COMPETITION.COMPETITION_NAME.like(query.getSearch()))
                    .and(COMPETITION.COMPETITION_NATURE.eq(query.getCompetitionNature()))
                    .and(COMPETITION.COMPETITION_TYPE.eq(query.getCompetitionType()))
                    .page(Page.of(pageNo, pageSize));
        }, readThreadPool);
        Page<Competition> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deleteCompetition(String competitionId) {
        int i = competitionMapper.deleteById(competitionId);
        if (i > 0)
            return ResponseUtil.success();
        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
    }

    @Override
    public boolean competitionNatureIsSolo(String competitionId) {
        // 表中获取, 不可能为null
        int nature = QueryChain.of(Competition.class)
                .where(COMPETITION.COMPETITION_ID.eq(competitionId))
                .one()
                .getCompetitionNature().intValue();
        return nature == 1;
    }

}

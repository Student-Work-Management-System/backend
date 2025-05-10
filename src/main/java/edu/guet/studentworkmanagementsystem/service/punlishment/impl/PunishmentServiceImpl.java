package edu.guet.studentworkmanagementsystem.service.punlishment.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import edu.guet.studentworkmanagementsystem.common.BaseQuery;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.Punishment;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.mapper.punishment.PunishmentMapper;
import edu.guet.studentworkmanagementsystem.service.punlishment.PunishmentService;
import edu.guet.studentworkmanagementsystem.utils.FutureExceptionExecute;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static edu.guet.studentworkmanagementsystem.entity.po.punishment.table.PunishmentTableDef.PUNISHMENT;

@Service
public class PunishmentServiceImpl extends ServiceImpl<PunishmentMapper, Punishment> implements PunishmentService {
    private final ThreadPoolTaskExecutor readThreadPool;

    public PunishmentServiceImpl(@Qualifier("readThreadPool") ThreadPoolTaskExecutor readThreadPool) {
        this.readThreadPool = readThreadPool;
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> addPunishmentItem(Punishment punishment) {
        int i = mapper.insert(punishment);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> updatePunishmentItem(Punishment punishment) {
        boolean update = UpdateChain.of(Punishment.class)
                .set(PUNISHMENT.PUNISHMENT_NAME, punishment.getPunishmentName(), StringUtils::hasLength)
                .where(PUNISHMENT.PUNISHMENT_ID.eq(punishment.getPunishmentId()))
                .update();
        if (!update)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    @Transactional
    public <T> BaseResponse<T> deletePunishmentItem(String punishmentId) {
        int i = mapper.deleteById(punishmentId);
        if (i <= 0)
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return ResponseUtil.success();
    }

    @Override
    public BaseResponse<Page<Punishment>> getPunishments(BaseQuery query) {
        CompletableFuture<Page<Punishment>> future = CompletableFuture.supplyAsync(() -> {
            int pageNo = Optional.ofNullable(query.getPageNo()).orElse(1);
            int pageSize = Optional.ofNullable(query.getPageSize()).orElse(10);
            return QueryChain.of(Punishment.class)
                    .select(PUNISHMENT.ALL_COLUMNS)
                    .from(PUNISHMENT)
                    .page(Page.of(pageNo, pageSize));
        }, readThreadPool);
        Page<Punishment> execute = FutureExceptionExecute.fromFuture(future).execute();
        return ResponseUtil.success(execute);
    }
}

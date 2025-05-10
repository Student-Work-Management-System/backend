package edu.guet.studentworkmanagementsystem.service.punlishment;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseQuery;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.po.punishment.Punishment;

public interface PunishmentService extends IService<Punishment> {
    <T> BaseResponse<T> addPunishmentItem(Punishment punishment);
    <T> BaseResponse<T> updatePunishmentItem(Punishment punishment);
    <T> BaseResponse<T> deletePunishmentItem(String punishmentId);
    BaseResponse<Page<Punishment>> getPunishments(BaseQuery query);
}

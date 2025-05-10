package edu.guet.studentworkmanagementsystem.service.precaution;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionItem;
import edu.guet.studentworkmanagementsystem.entity.vo.precaution.StudentPrecautionStatGroup;
import java.util.List;


public interface PrecautionService extends IService<StudentPrecaution> {
    <T> BaseResponse<T> importPrecaution(ValidateList<StudentPrecaution> precautions);
    <T> BaseResponse<T> insertPrecaution(StudentPrecaution precaution);
    <T> BaseResponse<T> updatePrecaution(StudentPrecaution precaution);
    <T> BaseResponse<T> deletePrecaution(String studentPrecautionId);
    <T> BaseResponse<T> handlePrecaution(String studentPrecautionId);
    BaseResponse<Page<StudentPrecautionItem>> getAllRecords(PrecautionQuery query);
    BaseResponse<List<StudentPrecautionStatGroup>> getStat(PrecautionStatQuery query);
}

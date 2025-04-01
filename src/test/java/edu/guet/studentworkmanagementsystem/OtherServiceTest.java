package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OtherServiceTest {
    @Autowired
    private OtherService otherService;
    @Test
    public void getAllCounselors() {
        CounselorQuery query = CounselorQuery.builder()
                .search("")
                .pageNo(1)
                .pageSize(10)
                .build();
        BaseResponse<Page<CounselorItem>> allCounselors = otherService.getAllCounselors(query);
        List<CounselorItem> records = allCounselors.getData().getRecords();
        System.out.println(records);
    }
}

package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.other.CounselorRequest;
import edu.guet.studentworkmanagementsystem.entity.vo.other.CounselorItem;
import edu.guet.studentworkmanagementsystem.entity.vo.other.UserWithCounselorRole;
import edu.guet.studentworkmanagementsystem.service.other.OtherService;
import edu.guet.studentworkmanagementsystem.utils.SetUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Test
    void testSet() {
        HashSet<String> set1 = new HashSet<>();
        set1.add("2019");
        // set1.add("2020");
        set1.add("2023");
        HashSet<String> set2 = new HashSet<>();
        set2.add("2020");
        set2.add("2021");
        set2.add("2022");
        Set<String> intersection = SetUtil.intersection(set1, set2);
        System.out.println(intersection); // ["2020"]
        Set<String> remove = SetUtil.difference(set2, set1);
        System.out.println(remove); // ["2021", "2022"]
        Set<String> add = SetUtil.difference(set1, intersection);
        System.out.println(add); // ["2019", "2023"]
    }

    @Test
    void testUpdateCounselor() {
        CounselorRequest build = CounselorRequest.builder()
                .uid("141")
                .chargeGrade(Set.of("1", "3", "4"))
                .build();
        BaseResponse<Object> response = otherService.updateCounselor(build);
    }

    @Test
    void getOptionalCounselor() {
        BaseResponse<List<UserWithCounselorRole>> optionalCounselors = otherService.getOptionalCounselors();
        List<UserWithCounselorRole> data = optionalCounselors.getData();
        System.out.println(optionalCounselors.getMessage());
    }
}

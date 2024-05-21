package edu.guet.studentworkmanagementsystem.network;

import edu.guet.studentworkmanagementsystem.entity.dto.employment.EmploymentStatQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(url = "http://10.33.9.182:8080/emp_stat", name = "employment")
public interface EmploymentFeign {
    @PostMapping("/export_only_stat")
    HashMap<String, Object> exportOnlyStat(@RequestBody EmploymentStatQuery query);
    @PostMapping("/export_with_stat")
    byte[] exportWithStat(@RequestBody EmploymentStatQuery query);
}

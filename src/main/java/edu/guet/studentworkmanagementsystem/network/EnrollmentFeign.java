package edu.guet.studentworkmanagementsystem.network;

import edu.guet.studentworkmanagementsystem.entity.dto.enrollment.EnrollmentStatQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(url = "http://10.33.9.182:8080/enrollment_stat", name = "enrollment")
public interface EnrollmentFeign {
    @PostMapping("/export_with_stat")
    byte[] exportWithStat(@RequestBody EnrollmentStatQuery query);
    @PostMapping("/export_only_stat")
    HashMap<String, Object> exportOnlyStat(@RequestBody EnrollmentStatQuery query);
}

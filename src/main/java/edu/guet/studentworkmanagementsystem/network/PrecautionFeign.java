package edu.guet.studentworkmanagementsystem.network;

import edu.guet.studentworkmanagementsystem.entity.dto.precaution.PrecautionStatQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(name = "precaution", url = "http://10.33.9.182:8080/acawarning_stat")
public interface PrecautionFeign {
    @PostMapping("/export_only_stat")
    HashMap<String, Object> exportOnlyStat(@RequestBody PrecautionStatQuery query);
    @PostMapping("/export_with_stat")
    byte[] exportWithStat(@RequestBody PrecautionStatQuery query);
}

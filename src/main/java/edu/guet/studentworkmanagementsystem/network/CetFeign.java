package edu.guet.studentworkmanagementsystem.network;

import edu.guet.studentworkmanagementsystem.entity.dto.cet.CetStatQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(url = "http://10.33.9.182:8080/cet_stat", name = "cet")
public interface CetFeign {
    @PostMapping("/export_only_stat")
    Map<String,Object> exportOnlyStat(@RequestBody CetStatQuery query);
}

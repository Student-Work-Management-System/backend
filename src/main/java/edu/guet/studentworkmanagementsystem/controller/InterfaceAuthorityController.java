package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.InterfaceAuthority;
import edu.guet.studentworkmanagementsystem.service.interfaceAuthority.InterfaceAuthorityService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/interface")
public class InterfaceAuthorityController {
    @Autowired
    private InterfaceAuthorityService interfaceAuthorityService;
    @PermitAll
    @GetMapping("/prefix/{prefix}")
    public BaseResponse<List<InterfaceAuthority>> getInterfaceAuthorities(@PathVariable String prefix) {
        return interfaceAuthorityService.getInterfaceAuthorities(prefix);
    }
    @PermitAll
    @GetMapping("/optional")
    public BaseResponse<List<String>> getOptional() {
        return interfaceAuthorityService.getOptional();
    }
}

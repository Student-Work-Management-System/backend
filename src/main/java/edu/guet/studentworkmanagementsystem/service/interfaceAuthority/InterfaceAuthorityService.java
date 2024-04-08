package edu.guet.studentworkmanagementsystem.service.interfaceAuthority;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.entity.vo.authority.InterfaceAuthority;

import java.util.List;

public interface InterfaceAuthorityService {
    BaseResponse<List<InterfaceAuthority>> getInterfaceAuthorities(String prefix);
    BaseResponse<List<String>> getOptional();
}

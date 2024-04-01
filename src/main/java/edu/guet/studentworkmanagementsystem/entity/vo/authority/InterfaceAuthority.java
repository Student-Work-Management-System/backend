package edu.guet.studentworkmanagementsystem.entity.vo.authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceAuthority implements Serializable {
    private String interfaceName;
    private List<String> requireAuthorities;
}

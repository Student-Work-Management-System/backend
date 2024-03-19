package edu.guet.studentworkmanagementsystem.securiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class SystemAuthority implements GrantedAuthority {
    @JsonIgnore
    private final String permissionName;
    private final String permissionDescription;
    public SystemAuthority(String permissionName, String permissionDescription) {
        this.permissionDescription = permissionDescription;
        this.permissionName = permissionName;
    }
    @Override
    public String getAuthority() {
        return permissionName;
    }
}

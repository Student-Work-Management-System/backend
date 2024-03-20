package edu.guet.studentworkmanagementsystem.securiy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemAuthority implements GrantedAuthority {
    private String authority;
    private String permissionDesc;
    @Override
    public String getAuthority() {
        return authority;
    }
}

package edu.guet.studentworkmanagementsystem.entity.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.guet.studentworkmanagementsystem.entity.po.user.Role;
import edu.guet.studentworkmanagementsystem.entity.po.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVO implements Serializable {
    private String uid;
    private String username;
    private String realName;
    private String email;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private List<Role> roles = null;
    public UserDetailVO(User user) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}

package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserDTO;
import edu.guet.studentworkmanagementsystem.entity.dto.user.UpdateUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id(keyType = KeyType.Auto)
    private String uid;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String password;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private boolean enabled;
    public User(RegisterUserDTO registerUserDTO) {
        this.username = registerUserDTO.getUsername();
        this.realName = registerUserDTO.getRealName();
        this.email = registerUserDTO.getEmail();
        this.password = registerUserDTO.getPassword();
        this.createdAt = LocalDate.now();
        this.enabled = true;
    }
}

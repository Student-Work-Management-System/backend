package edu.guet.studentworkmanagementsystem.entity.po.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Table("user")
@Data
@AllArgsConstructor
public class User {
    @Id(keyType = KeyType.Auto)
    private String uid;
    @Size(max = 32, message = "最长为32字符")
    private String username;
    @Size(max = 32, message = "最长为32字符")
    private String realName;
    @Size(min = 11, max = 11, message = "电话号码应11位")
    private String phone;
    @Size(min = 6, max = 20, message = "密码最短6位, 最长20位")
    @JsonIgnore
    private String password;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}

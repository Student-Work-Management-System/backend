package edu.guet.studentworkmanagementsystem.entity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery implements Serializable {
    private String keyword;
    private Boolean enabled = true;
}

package edu.guet.studentworkmanagementsystem.entity.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindBackPasswordVO implements Serializable {
    String email;
}

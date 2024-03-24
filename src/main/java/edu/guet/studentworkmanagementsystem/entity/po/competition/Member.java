package edu.guet.studentworkmanagementsystem.entity.po.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {
    private int order;
    private String studentId;
    private String realName;
}

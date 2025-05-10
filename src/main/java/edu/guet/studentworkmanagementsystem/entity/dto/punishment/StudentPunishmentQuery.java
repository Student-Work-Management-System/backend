package edu.guet.studentworkmanagementsystem.entity.dto.punishment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentQuery implements Serializable {
    private String search;
    private String gradeId;
    private String majorId;
    private String punishmentId;
    private Integer pageNo;
    private Integer pageSize;
}

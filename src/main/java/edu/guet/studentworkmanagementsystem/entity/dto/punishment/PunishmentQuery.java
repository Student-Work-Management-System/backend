package edu.guet.studentworkmanagementsystem.entity.dto.punishment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunishmentQuery implements Serializable {
    private String studentId;
    private String name;
    private Integer pageNo;
    private Integer pageSize;
}

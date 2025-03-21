package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadreQuery implements Serializable {
    private String search;
    private String gradeId;
    private String majorId;
    private String cadreLevel;
    private String appointmentStartTerm;
    private String appointmentEndTerm;
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}

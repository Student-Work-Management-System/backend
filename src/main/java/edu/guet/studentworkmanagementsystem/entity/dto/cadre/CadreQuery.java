package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadreQuery implements Serializable {
    private String studentId;
    private String name;
    private String grade;
    private String majorId;
    private String cadrePosition;
    private String cadreLevel;
    private String appointmentStartTerm;
    private String appointmentEndTerm;
    private Integer pageNo;
    private Integer pageSize;
}

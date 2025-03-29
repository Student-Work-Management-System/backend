package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCompetitionQuery implements Serializable {
    private String search;
    private String majorId;
    private String gradeId;
    private String degreeId;
    private String competitionNature;
    private String competitionType;
    private String level;
    private LocalDate start;
    private LocalDate end;
    private String state;
    private Integer pageNo;
    private Integer pageSize;
}

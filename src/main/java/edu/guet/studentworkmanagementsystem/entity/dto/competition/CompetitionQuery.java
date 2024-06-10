package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionQuery implements Serializable {
    private String search;
    private String grade;
    private String majorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean enabled;
    private String state;
    private Integer pageNo;
    private Integer pageSize;
}

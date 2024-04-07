package edu.guet.studentworkmanagementsystem.entity.dto.competition;

import edu.guet.studentworkmanagementsystem.entity.po.competition.Competition;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionList implements Serializable {
    @Valid
    private List<Competition> competitions;
}

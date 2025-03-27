package edu.guet.studentworkmanagementsystem.entity.po.competition;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("student_competition_team")
public class StudentCompetitionTeam {
    @Id
    private String studentCompetitionId;
    @Id
    private String studentId;
}

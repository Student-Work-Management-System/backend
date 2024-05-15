package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCompetitionPassedRecord implements Serializable {
    private String studentId;
    private String studentName;
    private List<StudentCompetitionVO> awards;
}

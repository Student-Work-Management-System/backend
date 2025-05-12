package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkStatGroup {
    private PaperStat paperStat;
    private PatentStat patentStat;
    private SoftStat softStat;
}

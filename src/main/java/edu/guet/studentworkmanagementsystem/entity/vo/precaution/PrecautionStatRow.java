package edu.guet.studentworkmanagementsystem.entity.vo.precaution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecautionStatRow {
    private String gradeName;
    private String majorName;
    private String term;
    private String levelCode;
    private String handledNumber;
    private String allNumber;
}

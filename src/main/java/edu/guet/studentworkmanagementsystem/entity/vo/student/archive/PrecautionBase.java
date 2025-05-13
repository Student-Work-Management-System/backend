package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecautionBase {
    private String levelCode;
    private String term;
    private String detail;
}

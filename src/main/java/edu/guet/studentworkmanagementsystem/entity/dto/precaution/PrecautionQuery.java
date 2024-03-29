package edu.guet.studentworkmanagementsystem.entity.dto.precaution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecautionQuery implements Serializable {
    private String grade;
    private String majorId;
    private String schoolPrecautionLevel;
    private Integer pageNo;
    private Integer pageSize;
}

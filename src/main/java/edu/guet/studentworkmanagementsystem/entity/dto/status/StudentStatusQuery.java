package edu.guet.studentworkmanagementsystem.entity.dto.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatusQuery implements Serializable {
    private String search;
    private String gradeId;
    private String majorId;
    private Integer pageNo;
    private Integer pageSize;
}

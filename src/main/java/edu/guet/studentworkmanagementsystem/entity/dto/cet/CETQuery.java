package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CETQuery implements Serializable {
    private String majorId;
    private String grade;
    private String classNo;
    private Integer pageNo;
    private Integer pageSize;
}

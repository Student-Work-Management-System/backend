package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CetQuery implements Serializable {
    private String search;
    private String majorId;
    private String grade;
    private String examDate;
    private String examType;
    private Boolean enabled;
    private Integer pageNo;
    private Integer pageSize;
}

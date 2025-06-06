package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkQuery implements Serializable {
    private String search;
    private String state;
    private String type;
    private Integer pageNo;
    private Integer pageSize;
}

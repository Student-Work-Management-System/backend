package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CETQuery implements Serializable {
    private String majorIn;
    private String grade;
    private String classNo;
}

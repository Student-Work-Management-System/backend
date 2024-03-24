package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PovertyAssistanceQuery implements Serializable {
    private String grade;
    private String majorIn;
}

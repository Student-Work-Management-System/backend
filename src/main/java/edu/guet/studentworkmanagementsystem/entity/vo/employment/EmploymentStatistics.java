package edu.guet.studentworkmanagementsystem.entity.vo.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentStatistics {
    private HashMap<String, Integer> graduationStatus;
    private HashMap<String, Integer> jobLocation;
    private HashMap<String, Integer> jobIndustry;
    private String salary;
}

package edu.guet.studentworkmanagementsystem.entity.vo.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentStatItem {
    private HashMap<String, Object> graduationStatus;
    private HashMap<String, Object> jobLocation;
    private HashMap<String, Object> jobIndustry;
    private String salary;
}

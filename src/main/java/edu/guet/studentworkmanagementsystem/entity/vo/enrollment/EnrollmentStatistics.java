package edu.guet.studentworkmanagementsystem.entity.vo.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentStatistics implements Serializable {
    private HashMap<String, Object> origin;
    private HashMap<String, Object> enrollmentState;
    private HashMap<String, HashMap<String, Object>> regionScores;
}

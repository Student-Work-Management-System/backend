package edu.guet.studentworkmanagementsystem.entity.vo.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CounselorItem {
    private String uid;
    private String counselorUsername;
    private String counselorName;
    private String counselorPhone;
    private Set<String> chargeGrade;
    private Set<String> chargeDegree;
}

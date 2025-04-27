package edu.guet.studentworkmanagementsystem.entity.vo.scholarship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentScholarshipStatGroup {
    private String gradeName;
    private List<StudentScholarshipStatItem> studentScholarshipStatItems;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentScholarshipStatItem {
        private String majorName;
        private List<ScholarshipStatItem> scholarshipStatItems;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScholarshipStatItem {
        private String scholarshipName;
        private String scholarshipLevel;
        private String total;
    }
}

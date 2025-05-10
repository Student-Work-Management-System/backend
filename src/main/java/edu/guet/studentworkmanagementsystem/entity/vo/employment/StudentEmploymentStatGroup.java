package edu.guet.studentworkmanagementsystem.entity.vo.employment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentEmploymentStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorName;
        private List<StudentEmploymentGroup> employments;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentEmploymentGroup {
        private String whereabouts;
        private String number;
    }
}

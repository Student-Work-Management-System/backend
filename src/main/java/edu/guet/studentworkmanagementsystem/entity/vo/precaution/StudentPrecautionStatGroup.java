package edu.guet.studentworkmanagementsystem.entity.vo.precaution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentPrecautionStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorName;
        private List<TermGroup> terms;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TermGroup {
        private String term;
        private List<LevelGroup> levels;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LevelGroup {
        private String levelCode;
        private String handledNumber;
        private String allNumber;
    }
}


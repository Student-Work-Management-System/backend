package edu.guet.studentworkmanagementsystem.entity.vo.povertyAssistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PovertyAssistanceStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorName;
        private List<LevelGroup> povertyLevels;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LevelGroup {
        private String povertyLevel;
        private String number;
    }
}

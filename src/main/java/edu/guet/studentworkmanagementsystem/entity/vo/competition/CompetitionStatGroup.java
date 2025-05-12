package edu.guet.studentworkmanagementsystem.entity.vo.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorId;
        private List<TypeGroup> types;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TypeGroup {
        private String type;
        private List<CompetitionGroup> competitions;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompetitionGroup {
        private String competitionTotalName;
        private List<LevelGroup> levels;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LevelGroup {
        private String level;
        private String count;
    }
}

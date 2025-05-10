package edu.guet.studentworkmanagementsystem.entity.vo.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorName;
        private StatData stat;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatData {
        private Long totalCount;
        private Long normalCount;
        private Long suspendCount;
        private Long militaryCount;
        private Long returnCount;
        private Long transferInCount;
        private Long transferOutCount;
        private Long dropOfEnrollmentCount;
        private Long retainEnrollmentCount;
        private Long graduationCount;
        private Long gradCount;
        private Long droppedCount;
        private Long rechristenCount;
        private Long deathCount;

        private Long maleCount;
        private Long femaleCount;

        private Long massCount;
        private Long leagueCount;
        private Long partyCount;
        private Long prepareCount;

        private Long disabilityCount;
        private Long minorityCount;
    }
}


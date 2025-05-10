package edu.guet.studentworkmanagementsystem.entity.vo.punishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup{
        private String majorName;
        private List<PunishmentGroup> punishments;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PunishmentGroup{
        private String punishmentName;
        private String number;
    }
}

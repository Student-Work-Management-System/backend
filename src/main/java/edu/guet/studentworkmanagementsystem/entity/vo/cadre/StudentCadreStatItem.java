package edu.guet.studentworkmanagementsystem.entity.vo.cadre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCadreStatItem {
    private String majorName;
    private List<CadreStatItem> cadreStatItems;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CadreStatItem {
        private String cadreName;
        private String cadreBelong;
        private String appointmentTime;
    }
}

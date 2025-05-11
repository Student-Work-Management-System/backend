package edu.guet.studentworkmanagementsystem.entity.vo.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatusStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorName;
        private List<StatusGroup> status;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusGroup {
        private String statusName;
        private String number;
    }
}

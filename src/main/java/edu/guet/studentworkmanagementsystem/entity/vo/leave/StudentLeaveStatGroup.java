package edu.guet.studentworkmanagementsystem.entity.vo.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveStatGroup {
    private String gradeName;
    private List<MajorGroup> majors;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MajorGroup {
        private String majorName;
        private List<StudentLeaveStat> studentLeaves;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentLeaveStat {
        private String type;
        private String destroyedNumber;
        private String internshipNumber;
        private String totalNumber;
    }
}

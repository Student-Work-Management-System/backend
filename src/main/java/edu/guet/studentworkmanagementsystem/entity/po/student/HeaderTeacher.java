package edu.guet.studentworkmanagementsystem.entity.po.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeaderTeacher {
    private String headerTeacherUsername;
    private String headerTeacherRealName;
    private String headerTeacherPhone;
}

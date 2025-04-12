package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkUser {
    private String username;
    private String realName;
}

package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusBase {
    private String statusName;
    private String log;
    private String modifiedTime;
}

package edu.guet.studentworkmanagementsystem.entity.vo.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentStatusItem implements Serializable {
    private String studentStatusId;
    private String studentId;
    private String name;
    private String majorName;
    private String gradeName;
    private String statusName;
    private String log;
    private String modifiedTime;
}

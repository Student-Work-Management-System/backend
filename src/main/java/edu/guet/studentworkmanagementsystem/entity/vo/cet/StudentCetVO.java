package edu.guet.studentworkmanagementsystem.entity.vo.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCetVO implements Serializable {
    private String studentCetId;
    private String studentId;
    private String name;
    private String majorName;
    private String grade;
    private List<CetVO> cets;
}

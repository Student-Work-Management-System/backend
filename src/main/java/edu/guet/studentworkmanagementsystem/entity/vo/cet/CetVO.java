package edu.guet.studentworkmanagementsystem.entity.vo.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CetVO implements Serializable {
    private String studentCetId;
    private Long score;
    private String examDate;
    private String examType;
    private String certificateNumber;
}

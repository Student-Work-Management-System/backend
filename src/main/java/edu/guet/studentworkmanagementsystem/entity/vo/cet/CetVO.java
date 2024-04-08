package edu.guet.studentworkmanagementsystem.entity.vo.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CetVO implements Serializable {
    private Long score;
    private String examDate;
    private String certificateNumber;
    private String examType;
}

package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private int order;
    private String studentId;
    private String authorName;
}

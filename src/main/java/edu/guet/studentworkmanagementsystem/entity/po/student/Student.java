package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

@Table("student")
public class Student {
    @Id(keyType = KeyType.Auto)
    private String studentId;
    @Id
    private String idNumber;
    @Id
    private String name;
}

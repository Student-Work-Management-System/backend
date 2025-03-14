package edu.guet.studentworkmanagementsystem.entity.po.student;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("degree")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Degree {
    @Id(keyType = KeyType.Auto)
    private String degreeId;
    private String degreeName;
}

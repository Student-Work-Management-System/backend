package edu.guet.studentworkmanagementsystem.entity.po.other;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("grade")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    @Id(keyType = KeyType.Auto)
    private String gradeId;
    private String gradeName;
}

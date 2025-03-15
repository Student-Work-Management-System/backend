package edu.guet.studentworkmanagementsystem.entity.po.other;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("politic")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Politic {
    @Id(keyType = KeyType.Auto)
    private String politicId;
    private String politicStatus;
}

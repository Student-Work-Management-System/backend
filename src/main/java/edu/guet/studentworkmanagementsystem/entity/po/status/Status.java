package edu.guet.studentworkmanagementsystem.entity.po.status;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("status")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Id(keyType = KeyType.Auto)
    private String statusId;
    private String statusName;
}

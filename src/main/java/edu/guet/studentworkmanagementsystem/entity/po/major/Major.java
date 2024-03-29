package edu.guet.studentworkmanagementsystem.entity.po.major;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
/**
 * 专业表
 * 2024.3.29创建
 * @author fish
 */
@Table("major")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Major implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String majorId;
    private String majorName;
}

package edu.guet.studentworkmanagementsystem.entity.po.scholarship;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 奖学金 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "scholarship")
public class Scholarship implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "奖学金id不能为空", groups = {UpdateGroup.class})
    private String scholarshipId;
    /**
     * 奖学金名称
     */
    @NotBlank(message = "奖学金名不能为空", groups = {InsertGroup.class})
    private String scholarshipName;
    /**
     * 奖学金级别
     */
    @NotBlank(message = "奖学金级别不能为空", groups = {InsertGroup.class})
    private String scholarshipLevel;
}

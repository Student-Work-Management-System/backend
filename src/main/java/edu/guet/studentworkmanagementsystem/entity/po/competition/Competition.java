package edu.guet.studentworkmanagementsystem.entity.po.competition;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * 竞赛记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "competition")
public class Competition implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "竞赛id不能为空", groups = {UpdateGroup.class})
    private String competitionId;
    /**
     * 竞赛名称
     */
    @NotBlank(message = "竞赛名不能为空", groups = {InsertGroup.class})
    private String competitionName;
    /**
     * 竞赛性质： 团队/单人
     */
    @Nullable
    @NotBlank(message = "竞赛性质不能为空", groups = {InsertGroup.class})
    @Pattern(regexp = "^(单人|团队)$",groups = {InsertGroup.class, UpdateGroup.class})
    private String competitionNature;
    /**
     * 竞赛级别
     */
    @NotBlank(message = "竞赛级别不能为空", groups = {InsertGroup.class})
    private String competitionLevel;
}

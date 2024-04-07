package edu.guet.studentworkmanagementsystem.entity.po.competition;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 团队成员认领奖项 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_competition_claim")
public class StudentCompetitionClaim implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 学生上报参与的竞赛id
     */
    @Id
    private String studentCompetitionId;
    /**
     * 要认领的学生id
     */
    @Id
    private String studentId;
}

package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 * 学生学术著作作者认领 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_academic_work_claim")
public class StudentAcademicWorkClaim implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long studentAcademicWorkId;

    @Id
    private String studentId;

}

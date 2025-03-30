package edu.guet.studentworkmanagementsystem.entity.po.scholarship;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.scholarship.StudentScholarshipRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 奖学金相关记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_scholarship")
public class StudentScholarship implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String studentScholarshipId;
    private String studentId;
    private String scholarshipId;
    /**
     * 获奖学年, YYYY-YYYY
     */
    private String awardYear;
    public StudentScholarship(StudentScholarshipRequest studentScholarshipRequest) {
        this.studentId = studentScholarshipRequest.getStudentId();
        this.scholarshipId = studentScholarshipRequest.getScholarshipId();
        this.awardYear = studentScholarshipRequest.getAwardYear();
    }
}

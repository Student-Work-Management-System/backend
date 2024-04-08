package edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance.InsertStudentPovertyAssistanceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生贫困资助认定相关记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_poverty_assistance")
public class StudentPovertyAssistance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private String studentPovertyAssistanceId;
    @Id
    private String studentId;
    @Id
    private String povertyAssistanceId;
    /**
     * 认证学年, YYYY-YYYY
     */
    private String assistanceYear;
    public StudentPovertyAssistance(InsertStudentPovertyAssistanceDTO insertStudentPovertyAssistanceDTO) {
        this.studentId = insertStudentPovertyAssistanceDTO.getStudentId();
        this.povertyAssistanceId = insertStudentPovertyAssistanceDTO.getPovertyAssistanceId();
        this.assistanceYear = insertStudentPovertyAssistanceDTO.getAssistanceYear();
    }
}

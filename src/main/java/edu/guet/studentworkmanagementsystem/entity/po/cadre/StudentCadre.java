package edu.guet.studentworkmanagementsystem.entity.po.cadre;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import edu.guet.studentworkmanagementsystem.entity.dto.cadre.StudentCadreDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生任职记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_cadre")
public class StudentCadre implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    private Long studentCadreId;
    @Id
    private String studentId;
    /**
     * 对应的职位id
     */
    @Id
    private Long cadreId;
    /**
     * 任职开始学期
     */
    private String appointmentStartTerm;
    /**
     * 任职结束学期
     */
    private String appointmentEndTerm;
    /**
     * 备注
     */
    private String comment;
    public StudentCadre(StudentCadreDTO studentCadreDTO) {
        this.cadreId = studentCadreDTO.getCadreId();
        this.studentId = studentCadreDTO.getStudentId();
        this.appointmentStartTerm = studentCadreDTO.getAppointmentStartTerm();
        this.appointmentEndTerm = studentCadreDTO.getAppointmentEndTerm();
        this.comment = studentCadreDTO.getComment();
    }
}

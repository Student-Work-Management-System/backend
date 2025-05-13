package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mybatisflex.annotation.Id;
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
 * 学生学术著作作者认领 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "academic_work_audit")
public class AcademicWorkAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @NotBlank(message = "学术作品id不能为空", groups = {UpdateGroup.class})
    private String academicWorkId;
    @NotBlank(message = "审核状态不能为空", groups = {InsertGroup.class, UpdateGroup.class})
    private String state;
    private String rejectReason;
    private String operatorId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String operatorTime;
}

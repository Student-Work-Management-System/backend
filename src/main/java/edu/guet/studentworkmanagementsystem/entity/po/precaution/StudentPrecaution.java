package edu.guet.studentworkmanagementsystem.entity.po.precaution;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 学业预警信息记录 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_precaution")
public class StudentPrecaution implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "学业预警id不能为空", groups = { UpdateGroup.class })
    private String studentPrecautionId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    /**
     * 学业预警等级
     */
    @NotBlank(message = "学业预警等级不能为空", groups = {InsertGroup.class})
    @Pattern(regexp = "^([012])$", message = "只能从红橙黄三种等级中选择")
    private String levelCode;
    /**
     * YYYY-YYYY_term
     */
    @NotBlank(message = "学业预警学期不能为空", groups = {InsertGroup.class})
    @Pattern(regexp = "^2\\d{3}-2\\d{3}_[1|2]$", message = "请输入正确的学业预警学期格式, 例如: 2023-2024_1")
    private String term;
    /**
     * 原因
     */
    @NotBlank(message = "学业预警明细不能为空", groups = {InsertGroup.class})
    private String detail;
    private Boolean status;
    private String handlerId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate handledAt;
}

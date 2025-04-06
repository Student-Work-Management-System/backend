package edu.guet.studentworkmanagementsystem.entity.po.punishment;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 处分信息 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_punishment")
public class StudentPunishment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "id不能为空", groups = {UpdateGroup.class})
    private String studentPunishmentId;
    @NotBlank(message = "学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    /**
     * 处分级别：警告、严重警告、记过、留校查看、开除学籍
     */
    @NotBlank(message = "处分级别不能为空", groups = {InsertGroup.class})
    private String level;
    /**
     * 处分理由
     */
    @NotBlank(message = "处分原因不能为空", groups = {InsertGroup.class})
    private String reason;
    /**
     * 处分日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}

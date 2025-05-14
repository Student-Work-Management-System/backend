package edu.guet.studentworkmanagementsystem.entity.po.status;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学籍 实体类。
 *
 * @author fish
 * @since 2024-03-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "student_status")
public class StudentStatus implements Serializable {
    @Id(keyType = KeyType.Auto)
    @NotBlank(message = "学生学籍状态id不能为空", groups = {UpdateGroup.class})
    private String studentStatusId;
    @NotBlank(message = "学生学号不能为空", groups = {InsertGroup.class})
    private String studentId;
    @NotBlank(message = "学籍id不能为空", groups = {InsertGroup.class})
    private String statusId;
    /**
     * 学籍处理
     */
    private String log;
    /**
     * 变动日期
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedTime;
    private Boolean statusEnabled;
}

package edu.guet.studentworkmanagementsystem.entity.dto.punlishment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentDTO implements Serializable {
    @NotBlank(message = "学生处分记录id不能为空")
    private String studentPunishmentId;
    private String studentId;
    /**
     * 处分级别：警告、严重警告、记过、留校查看、开除学籍
     */
    private String punishmentLevel;
    /**
     * 处分理由
     */
    private String punishmentReason;
    /**
     * 处分日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate punishmentDate;
}

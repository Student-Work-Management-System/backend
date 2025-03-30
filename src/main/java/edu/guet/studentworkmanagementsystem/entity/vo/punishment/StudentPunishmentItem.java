package edu.guet.studentworkmanagementsystem.entity.vo.punishment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentItem implements Serializable {
    private Long studentPunishmentId;
    private String studentId;
    private String name;
    private String majorName;
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

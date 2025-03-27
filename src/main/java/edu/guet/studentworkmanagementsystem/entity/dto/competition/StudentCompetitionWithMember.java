package edu.guet.studentworkmanagementsystem.entity.dto.competition;

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
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCompetitionWithMember implements Serializable {
    @NotBlank(message = "竞赛id不能为空")
    private String competitionId;
    /**
     * 填报人学号/队长学号
     */
    @NotBlank(message = "队长/上报人id不能为空")
    private String headerId;
    /**
     * 证明材料，这里填写文件地址
     */
    @NotBlank(message = "证明材料地址不能为空")
    private String evidence;
    /**
     * 奖项级别，由学生填报
     */
    @NotBlank(message = "奖项级别不能为空")
    private String level;
    /**
     * 获奖日期
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<String> studentIds;
}

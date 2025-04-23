package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentLeaveRequest implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "请假类型不能为空")
    private String type;
    @NotBlank(message = "请假理由不能为空")
    private String reason;
    @NotBlank(message = "目的地不能为空")
    private String target;
    @NotBlank(message = "详细地址不能为空")
    private String targetDetail;
    @NotNull(message = "当前是否实习不能为空")
    private boolean internship;
    /**
     * 请假时间, YYYY-MM-DD
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "请假起始日期不能为空")
    private LocalDate startDay;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "请假结束日期不能为空")
    private LocalDate endDay;
    private List<String> evidences;
    @NotBlank(message = "指派辅导员不能为空")
    private String counselorId;
}

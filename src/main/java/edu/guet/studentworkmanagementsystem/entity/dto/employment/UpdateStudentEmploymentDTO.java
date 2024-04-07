package edu.guet.studentworkmanagementsystem.entity.dto.employment;

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
public class UpdateStudentEmploymentDTO implements Serializable {
    @NotBlank(message = "学生就业信息id不能为空")
    private String studentEmploymentId;
    private String studentId;
    private String graduationState;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate graduationYear;
    private String whereabouts;
    private String jobNature;
    private String jobIndustry;
    private String jobLocation;
    private String category;
    private String salary;
}

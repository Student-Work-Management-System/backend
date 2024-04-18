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
public class InsertStudentEmploymentDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    private String graduationState;
    private String graduationYear;
    private String whereabouts;
    private String jobNature;
    private String jobIndustry;
    private String jobLocation;
    private String category;
    private String salary;
}

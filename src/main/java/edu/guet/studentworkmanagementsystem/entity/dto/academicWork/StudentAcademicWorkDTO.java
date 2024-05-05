package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicWorkDTO implements Serializable {
    @NotBlank(message = "学号不能为空")
    private String studentId;
    @NotBlank(message = "学生作品名称不能为空")
    private String academicWorkName;
    @Pattern(regexp = "^(123)$", message = "仅支持三种类型: 论文(1)、专利(2)和软著(3)")
    private String academicWorkType;
    private Long additionalInfoId;
    @NotNull(message = "作者不能为空")
    private List<Author> authors;
    @NotBlank(message = "证明材料地址不能为空")
    private String evidence;
    @NotNull(message = "学生作品信息不能为空")
    private AcademicWork academicWork;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate uploadTime;
}

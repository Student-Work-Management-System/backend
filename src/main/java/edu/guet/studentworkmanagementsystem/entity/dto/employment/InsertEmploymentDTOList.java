package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@NotNull
public class InsertEmploymentDTOList {
    @Valid
    private List<InsertStudentEmploymentDTO> insertStudentEmploymentDTOList;
}

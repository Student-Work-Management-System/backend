package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertEmploymentDTOList {
    @Valid
    private List<InsertStudentEmploymentDTO> insertStudentEmploymentDTOList;
}

package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertStudentCadreList implements Serializable {
    @Valid
    private List<InsertStudentCadreDTO> insertStudentCadreDTOList;
}

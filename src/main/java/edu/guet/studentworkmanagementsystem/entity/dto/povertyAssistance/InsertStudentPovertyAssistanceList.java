package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertStudentPovertyAssistanceList implements Serializable {
    @Valid
    private List<InsertStudentPovertyAssistanceDTO> list;
}

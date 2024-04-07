package edu.guet.studentworkmanagementsystem.entity.dto.scholarship;

import edu.guet.studentworkmanagementsystem.entity.po.scholarship.Scholarship;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipList implements Serializable {
    @Valid
    private List<Scholarship> scholarships;
}

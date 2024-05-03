package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicWorkList implements Serializable {
    @Valid
    private List<StudentAcademicWorkDTO> studentAcademicWorks;
}

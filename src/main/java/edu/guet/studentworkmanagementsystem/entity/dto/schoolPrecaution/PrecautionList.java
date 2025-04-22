package edu.guet.studentworkmanagementsystem.entity.dto.schoolPrecaution;

import edu.guet.studentworkmanagementsystem.entity.po.precaution.StudentPrecaution;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecautionList implements Serializable {
    @Valid
    private List<StudentPrecaution> precautions;
}

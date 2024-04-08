package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NotBlank
public class CadreList implements Serializable {
    @Valid
    private List<Cadre> cadres;
}

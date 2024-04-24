package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadreList implements Serializable {
    @Valid
    private List<Cadre> cadres;
}

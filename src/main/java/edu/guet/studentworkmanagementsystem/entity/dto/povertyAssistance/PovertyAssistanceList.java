package edu.guet.studentworkmanagementsystem.entity.dto.povertyAssistance;

import edu.guet.studentworkmanagementsystem.entity.po.povertyAssistance.PovertyAssistance;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PovertyAssistanceList implements Serializable {
    @Valid
    private List<PovertyAssistance> povertyAssistanceList;
}

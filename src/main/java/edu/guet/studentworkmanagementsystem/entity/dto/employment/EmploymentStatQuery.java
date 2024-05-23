package edu.guet.studentworkmanagementsystem.entity.dto.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentStatQuery implements Serializable {
    private List<String> graduationYears;
    private List<String> majorIds;
    private Boolean enabled = true;
}

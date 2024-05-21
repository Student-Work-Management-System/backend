package edu.guet.studentworkmanagementsystem.entity.dto.precaution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrecautionStatQuery implements Serializable {
    private List<String> precautionTimes;
    private List<String> majorIds;
}

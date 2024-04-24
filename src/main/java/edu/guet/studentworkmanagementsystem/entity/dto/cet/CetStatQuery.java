package edu.guet.studentworkmanagementsystem.entity.dto.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CetStatQuery implements Serializable {
    private List<String> examTimes;
    private List<String> majorIds;
}

package edu.guet.studentworkmanagementsystem.entity.vo.cet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CetStatistics implements Serializable {
    private HashMap<String, Object> cet4;
    private HashMap<String, Object> cet6;
}

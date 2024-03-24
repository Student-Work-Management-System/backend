package edu.guet.studentworkmanagementsystem.entity.dto.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusQuery implements Serializable {
    private String state;
    private String handle;
}

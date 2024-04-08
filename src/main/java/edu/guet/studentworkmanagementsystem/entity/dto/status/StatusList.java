package edu.guet.studentworkmanagementsystem.entity.dto.status;

import edu.guet.studentworkmanagementsystem.entity.po.status.StudentStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusList implements Serializable {
    @Valid
    private List<StudentStatus> statusList;
}

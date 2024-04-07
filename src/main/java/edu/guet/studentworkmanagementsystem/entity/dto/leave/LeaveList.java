package edu.guet.studentworkmanagementsystem.entity.dto.leave;

import edu.guet.studentworkmanagementsystem.entity.po.leave.StudentLeave;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveList implements Serializable {
    @Valid
    private List<StudentLeave> studentLeaves;
}

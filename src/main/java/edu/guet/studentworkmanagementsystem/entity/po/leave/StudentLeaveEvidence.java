package edu.guet.studentworkmanagementsystem.entity.po.leave;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("student_leave_evidence")
public class StudentLeaveEvidence {
    private String evidenceId;
    private String leaveId;
    private String path;
}

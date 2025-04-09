package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AcademicWork;
import com.mybatisflex.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentAcademicWorkItem implements Serializable {
    private String studentAcademicWorkId;
    private String username;
    private String realName;
    private String workName;
    private String type;
    private String referenceId;
    @Column(ignore = true)
    private AcademicWork academicWork;
    private List<StudentAcademicWorkMemberItem> team;
    private String operatorId;
    private String operatorTime;
    private String evidence;
    private String state;
    private String rejectReason;
}

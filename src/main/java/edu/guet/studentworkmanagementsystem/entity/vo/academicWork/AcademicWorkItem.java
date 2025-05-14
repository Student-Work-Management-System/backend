package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AbstractAcademicWork;
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
public class AcademicWorkItem implements Serializable {
    private String academicWorkId;
    private String username;
    private String realName;
    private String workName;
    private String type;
    private String referenceId;
    @Column(ignore = true)
    private AbstractAcademicWork academicWork;
    private List<AcademicWorkMemberItem> team;
    private String operatorId;
    private String operatorTime;
    private String evidence;
    private String state;
    private String rejectReason;
}

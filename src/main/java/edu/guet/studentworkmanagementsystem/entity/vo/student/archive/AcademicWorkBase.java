package edu.guet.studentworkmanagementsystem.entity.vo.student.archive;

import com.mybatisflex.annotation.Column;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AbstractAcademicWork;
import edu.guet.studentworkmanagementsystem.entity.vo.academicWork.AcademicWorkMemberItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkBase {
    private String studentAcademicWorkId;
    private String realName;
    private String workName;
    private String type;
    private String referenceId;
    @Column(ignore = true)
    private AbstractAcademicWork abstractAcademicWork;
    private List<AcademicWorkMemberItem> team;
    private String evidence;
}

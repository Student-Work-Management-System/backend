package edu.guet.studentworkmanagementsystem.entity.vo.academicWork;

import com.mybatisflex.annotation.Column;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AbstractAcademicWork;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicWorkStatItem {
    private String academicWorkId;
    private String type;
    private String referenceId;
    @Column(ignore = true)
    private AbstractAcademicWork academicWork;
}

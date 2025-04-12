package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("student_academic_work_member")
public class StudentAcademicWorkMember {
    @Id(keyType = KeyType.Auto)
    private String memberId;
    private String studentAcademicWorkId;
    private String memberOrder;
    private String username;
}

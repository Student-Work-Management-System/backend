package edu.guet.studentworkmanagementsystem.entity.po.other;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("counselor")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Counselor {
    @Id
    private String uid;
    @Id
    private String gradeId;
}

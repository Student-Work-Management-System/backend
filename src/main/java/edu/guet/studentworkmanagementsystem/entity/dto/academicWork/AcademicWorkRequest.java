package edu.guet.studentworkmanagementsystem.entity.dto.academicWork;

import edu.guet.studentworkmanagementsystem.common.InsertGroup;
import edu.guet.studentworkmanagementsystem.common.UpdateGroup;
import edu.guet.studentworkmanagementsystem.entity.po.academicWork.AbstractAcademicWork;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AcademicWorkRequest implements Serializable {
    @NotBlank(message = "学术作品id不能为空", groups = {UpdateGroup.class})
    private String studentAcademicWorkId;
    @NotBlank(message = "学号 / 工号 不能为空", groups = {InsertGroup.class})
    private String username;
    @NotBlank(message = "学术作品名称不能为空", groups = {InsertGroup.class})
    private String workName;
    private String type;
    @NotNull(message = "作者不能为空", groups = {InsertGroup.class})
    private List<AcademicWorkMemberRequest> team;
    @NotBlank(message = "证明材料地址不能为空", groups = {InsertGroup.class})
    private String evidence;
    @NotNull(message = "学生作品信息不能为空", groups = {InsertGroup.class})
    private AbstractAcademicWork abstractAcademicWork;
}

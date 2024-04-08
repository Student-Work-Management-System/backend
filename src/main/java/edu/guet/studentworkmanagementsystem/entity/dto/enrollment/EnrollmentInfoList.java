package edu.guet.studentworkmanagementsystem.entity.dto.enrollment;

import edu.guet.studentworkmanagementsystem.entity.po.enrollment.EnrollmentInfo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentInfoList implements Serializable {
    @Valid
    private List<EnrollmentInfo> enrollmentInfoList;
}

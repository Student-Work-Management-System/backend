package edu.guet.studentworkmanagementsystem.entity.dto.cadre;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadreDTO implements Serializable {
    @NotNull(message = "职位id不能为空")
    private String cadreId;
    /**
     * 具体职位
     */
    private String cadrePosition;
    /**
     * 职位级别
     */
    private String cadreLevel;
}

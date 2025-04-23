package edu.guet.studentworkmanagementsystem.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseQuery implements Serializable {
    private Integer pageNo;
    private Integer pageSize;
}

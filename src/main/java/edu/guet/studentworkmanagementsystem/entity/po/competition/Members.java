package edu.guet.studentworkmanagementsystem.entity.po.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Members implements Serializable {
    private List<Member> members;
}

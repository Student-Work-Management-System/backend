package edu.guet.studentworkmanagementsystem.entity.po.academicWork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authors implements Serializable {
    List<Author> authors;
}

package edu.guet.studentworkmanagementsystem.entity.po.emial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {
    private String to;
    private String from;
    private String subject;
    private String body;
}

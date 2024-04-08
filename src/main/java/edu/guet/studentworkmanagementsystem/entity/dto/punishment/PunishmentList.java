package edu.guet.studentworkmanagementsystem.entity.dto.punishment;

import edu.guet.studentworkmanagementsystem.entity.po.punishment.StudentPunishment;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PunishmentList implements Serializable {
    @Valid
    private List<StudentPunishment> punishments;
}

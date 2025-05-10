package edu.guet.studentworkmanagementsystem.entity.vo.punishment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPunishmentStatRow {
    private String gradeName;
    private String majorName;
    private String punishmentName;
    private String number;
}

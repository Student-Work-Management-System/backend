package edu.guet.studentworkmanagementsystem.entity.vo.cadre;

import edu.guet.studentworkmanagementsystem.entity.po.cadre.Cadre;
import edu.guet.studentworkmanagementsystem.entity.po.cadre.StudentCadre;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCadreVO implements Serializable {
    private String studentId;
    private String name;
    private String gender;
    private String majorIn;
    private String grade;
    private String cadrePosition;
    private String cadreLevel;
    private String appointmentStartTerm;
    private String appointmentEndTerm;
    private String comment;
    public StudentCadreVO(Student student, Cadre cadre, StudentCadre studentCadre) {
        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.gender = student.getGender();
        this.majorIn = student.getMajorIn();
        this.grade = student.getGrade();
        this.cadrePosition = cadre.getCadrePosition();
        this.cadreLevel = cadre.getCadreLevel();
        this.appointmentStartTerm = studentCadre.getAppointmentStartTerm();
        this.appointmentEndTerm = studentCadre.getAppointmentEndTerm();
        this.comment = studentCadre.getComment();
    }
}

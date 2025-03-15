package edu.guet.studentworkmanagementsystem;

import com.mybatisflex.core.paginate.Page;
import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.common.ValidateList;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentQuery;
import edu.guet.studentworkmanagementsystem.entity.dto.student.StudentStatusQuery;
import edu.guet.studentworkmanagementsystem.entity.po.student.Student;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentStatusItem;
import edu.guet.studentworkmanagementsystem.entity.vo.student.StudentTableItem;
import edu.guet.studentworkmanagementsystem.service.student.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;

    @Test
    void getStudentStatus() {
        StudentStatusQuery query = StudentStatusQuery.builder()
                // .degreeId("1")
                // .gradeId("3")
                .build();
        BaseResponse<List<StudentStatusItem>> studentStatus = studentService.getStudentStatus(query);
        System.out.println(studentStatus.getData());
    }

    @Test
    void addStudent() {
        Student student = createStudent("22", "22", "22222222222222222222222222", "男", "22", "22@qqqq.com", "50", "2", "2021", "群众");
        BaseResponse<Object> response = studentService.addStudent(student);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void addStudents() {
        List<Student> students = mockStudents();
        ValidateList<Student> studentValidateList = new ValidateList<>(students);
        BaseResponse<Object> response = studentService.importStudent(studentValidateList);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void updateStudent() {
        Student student = Student.builder()
                .studentId("22")
                .email("44@qqqq.com")
                .phone("44")
                .fatherName("dwaadw")
                .motherName("weffdsg")
                .build();
        BaseResponse<Object> response = studentService.updateStudent(student);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void deleteStudent() {
        String studentId = "22";
        BaseResponse<Object> response = studentService.deleteStudent(studentId);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void recoveryStudent() {
        String studentId = "22";
        BaseResponse<Object> response = studentService.recoveryStudent(studentId);
        System.out.println(response.getCode() + ": " + response.getMessage());
    }

    @Test
    void getStudent() {
        StudentQuery query = StudentQuery.builder()
                .enabled(true)
                .pageNo(1)
                .pageSize(50)
                .build();
        BaseResponse<Page<StudentTableItem>> students = studentService.getStudents(query);
        System.out.println(students.getCode() + ": " + students.getMessage());
    }

    private List<Student> mockStudents() {
        Student student1 = createStudent("1", "1", "1111111111111111111", "男", "1", "1@qqqq.com", "50", "1", "2024", "群众");
        Student student2 = createStudent("2", "2", "2222222222222222222", "女", "2", "2@qqqq.com", "50", "2", "2022", "群众");
        Student student3 = createStudent("3", "3", "3333333333333333333", "女", "3", "3@qqqq.com", "50", "5", "2021", "中共党员");
        Student student4 = createStudent("4", "4", "4444444444444444444", "男", "4", "4@qqqq.com", "50", "6", "2022", "入党先进分子");
        Student student5 = createStudent("5", "5", "5555555555555555555", "女", "5", "5@qqqq.com", "50", "2", "2020", "入党先进分子");
        Student student6 = createStudent("6", "6", "6666666666666666666", "男", "6", "6@qqqq.com", "50", "3", "2019", "少先队员");
        Student student7 = createStudent("7", "7", "7777777777777777777", "女", "7", "7@qqqq.com", "50", "4", "2022", "群众");
        Student student8 = createStudent("8", "8", "8888888888888888888", "男", "8", "8@qqqq.com", "50", "1", "2021", "群众");
        Student student9 = createStudent("9", "9", "9999999999999999999", "女", "9", "9@qqqq.com", "50", "2", "2020", "中共党员");
        Student student10 = createStudent("10", "10", "10101010101010101010", "女", "10", "10@qqqq.com", "50", "2", "2024", "团员");
        ArrayList<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        students.add(student7);
        students.add(student8);
        students.add(student9);
        students.add(student10);
        return students;
    }

    private Student createStudent(
            String studentId, String name, String idNumber, String gender,
            String phone, String email, String headTeacherUsername, String majorId,
            String grade, String politicsStatus
    ) {
        return Student.builder()
                .studentId(studentId)
                .name(name)
                .idNumber(idNumber)
                .gender(gender)
                .phone(phone)
                .email(email)
                .headTeacherUsername(headTeacherUsername)
                .majorId(majorId)
                .gradeId(grade)
                .politicId(politicsStatus)
                .enabled(true)
                .build();
    }
}

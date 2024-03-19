package edu.guet.studentworkmanagementsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.guet.studentworkmanagementsystem.mapper.*")
public class StudentWorkManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentWorkManagementSystemApplication.class, args);
    }
}

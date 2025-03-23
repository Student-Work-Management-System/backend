package edu.guet.studentworkmanagementsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("edu.guet.studentworkmanagementsystem.mapper.*")
@EnableFeignClients
public class StudentWorkManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentWorkManagementSystemApplication.class, args);
    }
}

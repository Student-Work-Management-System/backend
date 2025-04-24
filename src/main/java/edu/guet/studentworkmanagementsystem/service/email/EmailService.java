package edu.guet.studentworkmanagementsystem.service.email;


public interface EmailService {
    void sendEmail(String to, String code);
    // void findBackEmail();
}

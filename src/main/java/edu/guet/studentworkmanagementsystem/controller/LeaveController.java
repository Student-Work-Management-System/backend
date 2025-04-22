package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.service.leave.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

}

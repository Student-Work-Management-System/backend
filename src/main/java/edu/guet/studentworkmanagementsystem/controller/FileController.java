package edu.guet.studentworkmanagementsystem.controller;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.service.file.FileService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;
    @PreAuthorize("hasAuthority('file:upload')")
    @PostMapping("/upload/{from}")
    public BaseResponse<String> upload(@RequestParam MultipartFile file, @PathVariable String from) throws IOException {
        return fileService.upload(file, from);
    }
    @PermitAll
    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response) {
        fileService.download(filename, response);
    }
}

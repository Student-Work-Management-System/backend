package edu.guet.studentworkmanagementsystem.service.file.impl;

import edu.guet.studentworkmanagementsystem.service.file.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file, String fileFrom) {
        return null;
    }
}

package edu.guet.studentworkmanagementsystem.service.file.impl;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.config.MinioConfig;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.service.file.FileService;
import edu.guet.studentworkmanagementsystem.utils.MinioUtil;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class FileServiceImpl implements FileService {
    private static final String BUCKET_NAME = "student-work-manage-system";
    private static final String COMPETITION = "competition";
    private static final String ACADEMIC = "academic";
    @Autowired
    private MinioUtil minioUtil;
    @Override
    public BaseResponse<String> upload(MultipartFile file, String fileFrom) {
        if (!fileFrom.equals(COMPETITION) && !fileFrom.equals(ACADEMIC))
            throw new ServiceException(ServiceExceptionEnum.SELECT_NOT_IN);
        String newFileName = createNewFileName(file, fileFrom);
        try {
            saveFile(file, newFileName);
            return ResponseUtil.success(newFileName);
        } catch (IOException ioException) {
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }
    @Override
    public void download(String filename, HttpServletResponse response) {
        minioUtil.download(filename, response, BUCKET_NAME);
    }

    private void saveFile(MultipartFile file, String newFileName) throws IOException {
        if (!minioUtil.bucketExists(BUCKET_NAME))
            minioUtil.makeBucket(BUCKET_NAME);
        minioUtil.upload(file, newFileName, BUCKET_NAME);
    }
    private String createNewFileName(MultipartFile file, String fileFrom) {
        return fileFrom + "-" + file.getOriginalFilename();
    }
}

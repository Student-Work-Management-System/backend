package edu.guet.studentworkmanagementsystem.utils;

import cn.hutool.core.io.FastByteArrayOutputStream;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import io.minio.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class MinioUtil {
    @Autowired
    private MinioClient minioClient;
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception exception) {
            return false;
        }
    }
    public void makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }
    public void upload(MultipartFile file, String newFileName, String bucketName) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(newFileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }
    public void download(String filename, HttpServletResponse response, String bucketName) {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(filename).build();
        try (GetObjectResponse objectResponse = minioClient.getObject(getObjectArgs)) {
            byte[] buffer = new byte[1024];
            int len;
            try(FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = objectResponse.read(buffer)) != -1){
                    os.write(buffer, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
                try(ServletOutputStream stream = response.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        }
    }
}

package edu.guet.studentworkmanagementsystem.service.file;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    /**
     * 收取证明材料
     * @param file 表单内的文件(需要存放备份)
     * @param fileFrom 本系统中需要存的文件来源处只有 <strong>竞赛证明文件</strong> 和 <strong>学术著作文件</strong>
     * @return 文件存放的路径(会根据fileFrom来存储)
     */
    BaseResponse<String> upload(MultipartFile file, String fileFrom) throws IOException;
    /**
     * 通过文件名下载minio(oss)内的文件
     * @param filename 文件名
     * @param response 响应
     */
    void download(String filename, HttpServletResponse response);
}

package edu.guet.studentworkmanagementsystem.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 收取证明材料
     * @param file 表单内的文件(需要存放备份)
     * @param fileFrom 本系统中需要存的文件来源处只有 <strong>竞赛证明文件</strong> 和 <strong>学术著作文件</strong>
     * @return 文件存放的路径(会根据fileFrom来存储)
     */
    String upload(MultipartFile file, String fileFrom);
}

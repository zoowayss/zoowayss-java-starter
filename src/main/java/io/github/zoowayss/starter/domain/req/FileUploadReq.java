package io.github.zoowayss.starter.domain.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Data
public class FileUploadReq {

    // 文件流
    private MultipartFile file;

    // 图片base64字符串
    private String fileBase64;

    // 网址
    private String fileUrl;

    // 服务端赋值
    private HttpServletRequest request;

    // 服务端赋值
    private Long uid;
}

package io.github.zoowayss.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "zoowayss")
public class ZProperties {
    /**
     * 是否启用
     */
    private boolean enabled = true;
    
    /**
     * JWT密钥
     */
    private String jwtSecret = "defaultSecret";
    
    /**
     * 响应加密开关
     */
    private boolean responseEncrypt = false;
    
    /**
     * 请求解密开关 
     */
    private boolean requestDecrypt = false;
} 
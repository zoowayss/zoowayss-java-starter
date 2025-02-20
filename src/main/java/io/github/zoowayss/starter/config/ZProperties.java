package io.github.zoowayss.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "z.starter")
public class ZProperties {
    /**
     * 是否启用
     */
    private boolean enabled = true;
    
    // 在这里添加你的配置属性
} 
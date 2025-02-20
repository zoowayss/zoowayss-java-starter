package io.github.zoowayss.starter.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MetadataVo implements Serializable {

    private static final long serialVersionUID = -7539103507691223241L;

    // 0表示读取失败
    private Integer imageWidth = 0;

    // 0表示读取失败
    private Integer imageHeight = 0;

    // 若结果为空，说明识别失败。文件类型，可能会造假，注意安全。
    private String imageType = "";
}

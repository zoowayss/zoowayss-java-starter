package io.github.zoowayss.starter.controller;

import io.github.zoowayss.starter.anno.TokenOptional;
import io.github.zoowayss.starter.domain.dto.TokenUser;
import io.github.zoowayss.starter.domain.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/zoowayss">zoowayss</a>
 * @version 1.0
 * @since 2/20/25 09:50
 */
@RestController
@RequestMapping("/v1/z")
@Slf4j
public class TController {


    @GetMapping
    public ResultVo<?> hello(@TokenOptional TokenUser userInfo) {
        return ResultVo.asSuccess(userInfo);
    }
}
